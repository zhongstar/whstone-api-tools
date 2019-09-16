package com.whstone.utils.obs;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.RuntimeUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.obs.services.ObsClient;
import com.obs.services.exception.ObsException;
import com.obs.services.model.*;
import com.obs.services.model.fs.DropFolderRequest;
import com.obs.services.model.fs.NewFolderRequest;
import com.whstone.utils.cmd.CommandUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


/**
 * Created by zhongkf on 2019/05/28
 */
public class ObsHandler implements AutoCloseable{

    private static final Log log = LogFactory.get();

    private ObsClient obsClient;

    private String endPoint = "https://obs.cn-north-1.myhuaweicloud.com";

    private String ak = "YNMFU9BIUDDXORKJ9API";

    private String sk = "WftQLMWN8SQRpSQiEhs2BLGED6zjb5ywc1WILLXQ";

    private String bucketName = "ebackup-commont";


    public ObsHandler() {
        obsClient = new ObsClient(ak, sk, ObsConfig.getConfig(endPoint));
    }

    public ObsHandler(String endPoint, String ak, String sk) {
        obsClient = new ObsClient(ak, sk, ObsConfig.getConfig(endPoint));
    }

    public ObsHandler(String endPoint, String ak, String sk, String bucketName) throws ObsException {
        this.endPoint = endPoint;
        this.ak = ak;
        this.sk = sk;
        this.bucketName = bucketName;
        try {
            obsClient = new ObsClient(ak, sk, ObsConfig.getConfig(endPoint));
            obsClient.createBucket(bucketName);
        } catch (Exception e) {
            throw new ObsException("无法创建 obs 客户端，请检查网络环境是否畅通。");
        }

    }

    /**
     * 初始化obsutil方法
     *
     * @param endPoint
     * @param ak
     * @param sk
     * @return
     */
    public static void initObsUtil(String endPoint, String ak, String sk) {
        if (!FileUtil.isWindows()) {
            CommandUtil.getLinuxStateExec("chmod -R 777 " + ObsConstant.OBS_ROOT);
        }
        //初始化配置
        String initCmd = String.format(ObsUtilCmd.initObsutil, ak, sk, endPoint);
        if (FileUtil.isWindows()) {
            //windows
            log.info("initCmd for windows :{}", initCmd);
            RuntimeUtil.execForStr(initCmd);
        } else {
            log.info("initCmd for linux :{}", initCmd);
            CommandUtil.getLinuxStateExec(initCmd);

        }
    }


    private boolean checkConnected() throws ObsOperationException {
        String result = CommandUtil.execWinCommand(String.format(ObsUtilCmd.checkConnected, ObsConstant.OBS_WIN64_BIN));
        if (result.contains("Bucket number is:")) {
            return true;
        } else if (result.contains("Http status [403]")) {
            throw new ObsOperationException("访问密钥配置有误");
        } else if (result.contains("A connection attempt failed")) {
            throw new ObsOperationException("无法连接OBS服务，请检查网络环境是否正常");
        }
        return false;
    }

    /**
     * 检查是否存在指定桶
     *
     * @param bucketName
     * @return
     */
    public boolean isExistBucket(String bucketName) {
        return obsClient.headBucket(bucketName);
    }

    /**
     * 获取对象
     *
     * @param objectKey
     * @return
     */
    public ObjectMetadata getObject(String objectKey) {
        return obsClient.getObjectMetadata(bucketName, objectKey);
    }

    /**
     * 获取所有的桶
     *
     * @return
     */
    public List<ObsBucket> listBuckets() {
        return obsClient.listBuckets(new ListBucketsRequest());
    }

    /**
     * 创建桶
     */

    public String createBucket(String bucketName) throws ObsException {
        if (isExistBucket(bucketName)) return bucketName;
        obsClient.createBucket(bucketName);
        return bucketName;
    }

    /**
     * 上传文件对象到指定文件夹（分段,默认）
     *
     * @param file
     * @param target 相对云端桶路径下，/oracle/databaseId/planId/fullbackup/
     * @return
     */
    public boolean uploadObject(String target, File file) {
        if (FileUtil.isDirectory(file)) {
            return false;
        }
        target = handleDir(target);
        String objectkey = target + file.getName();

        long fileSize = file.length();
        long partSize = ObsConfig.PART_SIZE;
        long partCount = fileSize % partSize == 0 ? fileSize / partSize : fileSize / partSize + 1;

        final List<PartEtag> partEtags = Collections.synchronizedList(new ArrayList<>());

        if (partCount > 10000) {
            throw new ObsException("分段总数不能超过10000");
        }

        // 分段上传步骤
        // 初始化分段上传任务（ObsClient.initiateMultipartUpload）
        InitiateMultipartUploadRequest request = new InitiateMultipartUploadRequest(this.bucketName, objectkey);
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.addUserMetadata("property", "property-value");
        metadata.setContentType("text/plain");
        request.setMetadata(metadata);
        InitiateMultipartUploadResult result = obsClient.initiateMultipartUpload(request);
        //上传分段需要
        String uploadId = result.getUploadId();
        //新启线程池，如果作为成员变量，第一次shutdown后第二次调用会报错
        ExecutorService executorService = Executors.newFixedThreadPool(20);

        log.info("开始执行分段上传");
        //并行上传段（ObsClient.uploadPart）
        for (int i = 0; i < partCount; i++) {
            // 分段在文件中的起始位置
            final long offset = i * partSize;
            // 分段大小 最后一段不能小于100k
            final long currPartSize = (i + 1 == partCount) ? fileSize - offset : partSize;
            // 分段号
            final int partNumber = i + 1;
            final String objectKey = objectkey;
            executorService.execute(() -> {
                UploadPartRequest uploadPartRequest = new UploadPartRequest();
                uploadPartRequest.setBucketName(this.bucketName);
                uploadPartRequest.setObjectKey(objectKey);
                uploadPartRequest.setUploadId(uploadId);
                uploadPartRequest.setFile(file);
                uploadPartRequest.setPartSize(currPartSize);
                uploadPartRequest.setOffset(offset);
                uploadPartRequest.setPartNumber(partNumber);

                UploadPartResult uploadPartResult;
                try {
                    uploadPartResult = obsClient.uploadPart(uploadPartRequest);
                    log.info("上传分段: {} 完成", partNumber);
                    partEtags.add(new PartEtag(uploadPartResult.getEtag(), uploadPartResult.getPartNumber()));
                } catch (ObsException e) {
                    e.printStackTrace();
                }
            });
        }

        // 等待上传完成
        executorService.shutdown();
        while (!executorService.isTerminated()) {
            try {
                executorService.awaitTermination(5, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        // 合并段（ObsClient.completeMultipartUpload）或取消分段上传任务(ObsClient.abortMultipartUpload)
        obsClient.completeMultipartUpload(new CompleteMultipartUploadRequest(this.bucketName, objectkey, uploadId, partEtags));
        log.info("分段上传完毕");
        return true;
    }

    /**
     * 上传文件对象到指定文件夹
     *
     * @param file   指定文件
     * @param target 相对云端桶路径下，/oracle/databaseId/planId/fullbackup/ [""-桶的根目录]["/src/xxx/"-指定文件夹下]["/src/aaa.txt"-重命名]
     * @return
     */
    public boolean uploadObject(File file, String target) {
        if (FileUtil.isDirectory(file)) {
            return false;
        }
        if (!target.startsWith("/")) {
            target = "/" + target;
        }
        String uploadObject = String.format(ObsUtilCmd.uploadObject, file.getAbsolutePath(), this.bucketName, target);
        log.info("uploadObject:{}", uploadObject);
        String result = RuntimeUtil.execForStr(uploadObject);
        log.info("上传结果:{}", result);
        return true;
    }

    /**
     * 上传目录以目录下所有文件，递归(指定目录)
     *
     * @param dir /
     * @return
     */
    public boolean uploadFolderWithFile(File dir, String target) {
        if (FileUtil.isFile(dir)) {
            return false;
        }
        if (!target.startsWith("/")) {
            target = "/" + target;
        }
        if (!target.endsWith("/")) {
            target += "/";
        }
        String uploadFolderWithFile = String.format(ObsUtilCmd.uploadFolder, dir.getAbsolutePath(), this.bucketName, target);
        log.info("uploadFolderWithFile:{}", uploadFolderWithFile);

        String result = RuntimeUtil.execForStr(uploadFolderWithFile);
        log.info("上传结果:{}", result);

        return result.contains("Task id is");
    }

    /**
     * 上传目录以目录下所有文件，递归(默认取dir的目录名)
     *
     * @param dir
     * @return
     */
    public boolean uploadFolderWithFile(File dir) {
        if (FileUtil.isFile(dir)) {
            return false;
        }

        String uploadFolderWithFile = String.format(ObsUtilCmd.uploadFolder, dir.getAbsolutePath(), this.bucketName, "");
        log.info("uploadFolderWithFile:{}", uploadFolderWithFile);
        String result = RuntimeUtil.execForStr(uploadFolderWithFile);
        log.info("上传结果:{}", result);
        return result.contains("Task id is");
    }

    /**
     * 创建文件夹，递归创建
     *
     * @param dir
     * @return
     */
    public boolean mkdir(String dir) {
        NewFolderRequest newFolderRequest = new NewFolderRequest();
        newFolderRequest.setBucketName(this.bucketName);
        newFolderRequest.setObjectKey(handleDir(dir));
        obsClient.newFolder(newFolderRequest);
        return true;
    }

    /**
     * 下载指定文件覆盖到本地指定文件中
     *
     * @param keyObject 云端文件名
     * @param localFile 本地存放路径(可以是文件夹路径也可以是指定的文件路径)  "C:\\Users\\zkfcx\\Desktop\\obs\\" + objectKey;
     * @return
     */
    public boolean downloadObject(String path, String keyObject, String localFile) {
        if (!path.endsWith("/")) {
            path += "/";
        }
        if (path.startsWith("/")) {
            path = path.substring(1);
        }
        String downloadObject = String.format(ObsUtilCmd.downloadObject, this.bucketName, path + keyObject, localFile);
        log.info("downloadObject:{}", downloadObject);
        String result = RuntimeUtil.execForStr(downloadObject);
        log.info("下载结果:{}", result);
        return true;
    }

    /**
     * 下载云端指定文件夹到指定目录下(不包括指定文件夹)
     *
     * @param folder        相对 云端桶路径 /fullbackup/
     * @param localFilePath 本地的文件夹路径
     * @return
     */
    public boolean downloadFolder(String folder, String localFilePath) {
        if (!folder.startsWith("/")) {
            folder = "/" + folder;
        }
        File file = new File(localFilePath);
        if (!file.exists()) {
            file.mkdirs();
        }
        String downloadFlatFolder = String.format(ObsUtilCmd.downloadFlatFolder, this.bucketName, folder, localFilePath);
        log.info("downloadFlatFolder:{}", downloadFlatFolder);
        String result;
        if (FileUtil.isWindows()) {
            result = RuntimeUtil.execForStr("cmd /c " + downloadFlatFolder);
        } else {
            result = CommandUtil.execLinuxCommand(downloadFlatFolder);
        }
        log.info("下载结果:{}", result);
        return result.contains("Task id is");
    }

    /**
     * 下载云端指定文件夹到指定目录下(不包括指定文件夹)
     * 列举对象名前缀包含sourcePath的所有对象,使用断点续传下载下下来
     *
     * @param sourcePath 相对 云端桶路径 /fullbackup/
     * @param targetPath 本地的文件夹路径
     * @return
     */
    public boolean downloadFolder2(String sourcePath, String targetPath) {
        if (sourcePath.startsWith("/")) {
            sourcePath = sourcePath.substring(1);
        }
        if (!sourcePath.endsWith("/")) {
            sourcePath = sourcePath + "/";
        }
        File file = new File(targetPath);
        if (!file.exists()) {
            file.mkdirs();
        }

        ListObjectsRequest listObjectsRequest = new ListObjectsRequest();
        listObjectsRequest.setBucketName(this.bucketName);
        listObjectsRequest.setMaxKeys(1000);
        listObjectsRequest.setPrefix(sourcePath);
        ObjectListing result;

        do {
            result = obsClient.listObjects(listObjectsRequest);
            List<ObsObject> downloadLists = result.getObjects();
            downloadLists.stream().forEach(obsObject -> log.info("\t{}", obsObject.getObjectKey()));
            for (ObsObject obsObject : downloadLists) {
                String objectKey = obsObject.getObjectKey();
                //对象在本地的路径
                String localFilePath = targetPath + objectKey.replace(sourcePath, "");
                if (FileUtil.isWindows()) {
                    localFilePath = localFilePath.replaceAll("/", "\\\\");
                }
                if (objectKey.endsWith("/")) {
                    log.info("创建文件夹:{}", localFilePath);
                    FileUtil.mkdir(localFilePath);
                    continue;
                } else {
                    DownloadFileRequest request = new DownloadFileRequest(this.bucketName, objectKey);
                    // 设置下载对象的本地文件路径
                    request.setDownloadFile(localFilePath);
                    // 设置分段下载时的最大并发数
                    request.setTaskNum(ObsConfig.TASK_NUM);
                    // 设置分段大小为10MB
                    request.setPartSize(ObsConfig.PART_SIZE);
                    // 开启断点续传模式
                    request.setEnableCheckpoint(true);
                    // 进行断点续传下载
                    try {
                        File localFile = new File(localFilePath);
                        if (localFile.exists() & localFile.length() == getObject(objectKey).getContentLength()) {
                            log.info("跳过已存在的本地文件:{}", localFilePath);
                            continue;
                        } else {
                            obsClient.downloadFile(request);
                            log.info("下载文件{}成功", localFilePath);
                        }
                    } catch (ObsException e) {
                        if (e.getErrorMessage().contains("SSL peer shut down incorrectly")) {
                            System.setProperty("https.protocols", "TLSv1,TLSv1.1,TLSv1.2,SSLv3");
                            downloadFolder2(sourcePath, targetPath);
                        } else {
                            throw e;
                        }
                    }
                }
            }
            listObjectsRequest.setMarker(result.getNextMarker());
        } while (result.isTruncated());

        return true;
    }

    /**
     * 删除云端指定的所有文件
     *
     * @param keys
     */
    public void deleteObjects(String[] keys) {
        ListVersionsRequest request = new ListVersionsRequest(this.bucketName);
        // 每次批量删除100个对象
        request.setMaxKeys(100);
        ListVersionsResult result;

        do {
            result = obsClient.listVersions(request);

            DeleteObjectsRequest deleteRequest = new DeleteObjectsRequest(this.bucketName);

            for (VersionOrDeleteMarker v : result.getVersions()) {
                if (Arrays.asList(keys).contains(v.getKey())) {
                    deleteRequest.addKeyAndVersion(v.getKey(), v.getVersionId());
                }
            }

            DeleteObjectsResult deleteResult = obsClient.deleteObjects(deleteRequest);
            // 获取删除成功的对象
            log.info("删除成功:{}", deleteResult.getDeletedObjectResults());
            // 获取删除失败的对象
            log.info("删除失败:{}", deleteResult.getErrorResults());

            request.setKeyMarker(result.getNextKeyMarker());
            request.setVersionIdMarker(result.getNextVersionIdMarker());
        } while (result.isTruncated());
    }

    public void deleteObject(String objectkey) {
        if (objectkey.startsWith("/")) {
            objectkey = objectkey.substring(1);
        }
        String deleteObject = String.format(ObsUtilCmd.deleteObject, this.bucketName, objectkey);
        log.info("deleteObject:{}", deleteObject);
        String result = RuntimeUtil.execForStr(deleteObject);
        log.info("删除结果:{}", result);
    }

    /**
     * 清空 指定云端文件夹 相对同路径
     *
     * @param folder /
     */
    public void deleteFolder(String folder) {
        DropFolderRequest dropFolderRequest = new DropFolderRequest();
        dropFolderRequest.setBucketName(this.bucketName);
        dropFolderRequest.setFolderName(folder);
        obsClient.dropFolder(dropFolderRequest);
    }

    /**
     * 关闭obs客户端
     */
    public void closeObs() throws IOException {
        log.info("obsClient closed");
        obsClient.close();
    }

    @Override
    public void close() throws IOException {
        log.info("obsClient closed");
        obsClient.close();
    }

    private String handleDir(String dir) {
        if (dir.startsWith("/")) {
            dir = dir.substring(1);
        }
        if (!dir.endsWith("/")) {
            dir += "/";
        }
        return dir;
    }

    /**
     * 检查是否可用
     *
     * @return
     */
    public static boolean isAvailable(String endPoint, String ak, String sk) {
        try {
            ObsClient obsClient = new ObsClient(ak, sk, ObsConfig.getConfig(endPoint));
            obsClient.listBuckets(new ListBucketsRequest());
            obsClient.close();
            return true;
        } catch (Exception e) {
            log.error("obs建立失败:{}", e.getMessage());
        }
        return false;
    }

    private boolean checkCompleted(Runtime runtime) throws IOException {
        String queryCmd = String.format("cmd /c echo %s", "%errorlevel%");
        if (!FileUtil.isWindows()) {
            queryCmd = String.format("echo %s", "$?");
            String[] cmds = {"sh", "-c", queryCmd};
        }
        log.info("queryCmd:{}", queryCmd);
        String result = ReUtil.getGroup0("\\d", RuntimeUtil.getResult(runtime.exec(queryCmd)));
        log.info("查询结果:{}", result);
        if (Integer.valueOf(result).equals(ObsErrorLevelEnum.SUCCESS.code)) {
            return true;
        } else {
            log.error("文件操作失败:{}", ObsErrorLevelEnum.selectByCode(Integer.valueOf(result)).msg);
            return false;
        }
    }


    public static void main(String[] args) {
        String bucketName = "ebackup-oracle";
        String endPoint = "https://obs.cn-north-1.myhuaweicloud.com";

        String ak = "YNMFU9BIUDDXORKJ9API";

        String sk = "WftQLMWN8SQRpSQiEhs2BLGED6zjb5ywc1WILLXQ";

        String obsBinPath = "obsutil\\obsutil_windows_amd64\\";
        System.setProperty("https.protocols", "TLSv1,TLSv1.1,TLSv1.2,SSLv3");
//        String windowsStartCmd = obsBinPath + "obsutil config -i=%s -k=%s -e=%s";
//
//        String s = RuntimeUtil.execForStr(String.format(windowsStartCmd, ak, sk, endPoint));  //初始化
//

//        String s1 = RuntimeUtil.execForStr("obsutil help");
        ObsHandler obsHandler = new ObsHandler(endPoint, ak, sk, bucketName);

//        File file = new File("C:\\CHENLI\\work\\idea_workspace\\ebackup\\E-backup-Menegement\\target");
//        Map<String, File> map = new HashMap<>();
//        obsHandler.showDirectory(file, map, 0, "test/");
//        System.out.println(map);

//        boolean existBucket = obsHandler.isExistBucket(bucketName);
//        System.out.println(existBucket);
//
//        String bucket = obsHandler.createBucket(bucketName);
//        System.out.println(bucket);

//        boolean mkdir = obsHandler.mkdir("/22");
//        mkdir = obsHandler.mkdir("test1/1/2");
//        mkdir = obsHandler.mkdir("test2/1/2");
//        mkdir = obsHandler.mkdir("test3/1/2");
//        mkdir = obsHandler.mkdir("test3/1/3");
//        obsHandler.uploadObject(new File("target/ebackupmenegement2.jar.original"), "target");
//        obsHandler.uploadObject("test1/1/2", new File("target/ebackupmenegement.jar.original"));
//        obsHandler.deleteObject("target/ebackupmenegement2.jar.original");

//        String[] keys = {"test/1/ebackupmenegement.jar.original", "test1/1/2/ebackupmenegement.jar.original", "test2/1/2"};
//        obsHandler.deleteObjects(keys);

//        obsHandler.emptyFolder("test/1");
//        obsHandler.emptyFolder("test/2");
//        obsHandler.emptyFolder("/test/1");
//        obsHandler.emptyFolder("/test1/1/2");
//        obsHandler.emptyFolder("test3/1/2");
//        List<File> strings = FileUtil.loopFiles("C:\\CHENLI\\work\\idea_workspace\\ebackup\\E-backup-Menegement\\target");

//        boolean target = obsHandler.uploadFolderWithFile(new File("C:\\CHENLI\\work\\idea_workspace\\ebackup\\E-backup-Menegement\\target"), "target");
//        boolean target = obsHandler.uploadFolderWithFile(new File("target/maven-status"));
//        System.out.println(target);
//
//        obsHandler.downloadObject("/target/", "ebackupmenegement2.jar.original", "C:\\Users\\WHST-CHENLI\\Desktop\\wstone\\ebackupmenegement.jar.original");
//        obsHandler.downloadFolder("target/", "C:\\Users\\WHST-CHENLI\\Desktop\\wstone\\target");

//        String getenv = System.getenv("path");
//
//        System.out.println(getenv);
//
//        String s = CommandUtil.execWinCommand("echo %path%");
//        System.out.println(s);
////        set path=%path%;d:\tempfile
//        String s2 = CommandUtil.execWinCommand("set Path=%Path%;C:\\CHENLI\\work\\idea_workspace\\ebackup\\E-backup-Menegement\\obsutil\\obsutil_windows_amd64");
        boolean b = obsHandler.downloadFolder2("221", "C:\\Users\\WHST-CHENLI\\Desktop\\obstest\\");
        System.out.println();
    }

}