package com.whstone.utils.obs;

import cn.hutool.core.io.FileUtil;

/**
 * @Author: powerchen
 * @Date: 2019/6/3 9:35
 * 参数默认值参照 https://support.huaweicloud.com/utiltg-obs/obs_11_0035.html
 * 配置参数说明 https://support.huaweicloud.com/utiltg-obs/obs_11_0013.html
 * windows 参数1-obsutil.exe的所在路径 obsutil\obsutil_windows_amd64\
 * linux 参数1-./obs/obsutil_linux_amd64/  必须先赋权
 */
public class ObsUtilCmd {

    private static String obsUtilPath ;


    static {

        if(FileUtil.isWindows()) {

            obsUtilPath = ObsConstant.OBS_WIN64_BIN;

        }else {
            obsUtilPath = ObsConstant.OBS_LINUX_BIN;
        }

    }
    /**
     * 初始化操作
     * 后面的参数依次为ak, sk, endPoint
     */
    //public static final String initObsutil = obsUtilPath + "obsutil config -i=%s -k=%s -e=%s -config=" + ObsConstant.OBS_CONF_FILE;
    public static final String initObsutil = obsUtilPath + "obsutil config -i=%s -k=%s -e=%s ";
    /**
     * 检查连接状态
     */
    public static final String checkConnected = obsUtilPath +  "obsutil ls -s";

    /**
     * 上传文件夹
     * 递归上传本地src1文件夹中的所有文件和文件夹（包括src1文件夹本身）至bucket-test桶的src文件夹下，且上传过程中不进行询问操作
     * p-分段上传最大并发数默认5   threshold-开启分段上传的阈值默认10MB   ps-每个分段的大小默认5MB
     * obsutil cp /src1  obs://bucket-test/src -r -f 递归上传本地src1文件夹中的所有文件和文件夹（包括src1文件夹本身）至bucket-test桶的src文件夹下，且上传过程中不进行询问操作
     * 参数1-指定文件夹  参数3-目标文件夹
     */
    public static final String uploadFolder = obsUtilPath +  "obsutil cp %s obs://%s%s -r -f -fr -u -vlength -p=10 -threshold=100MB -ps=10MB";
    /**
     * 递归上传本地src1文件夹中的所有文件和文件夹（不包括src1文件夹本身）至bucket-test桶的src文件夹下，且上传过程中不进行询问操作
     */
    public static final String uploadFlatFolder = obsUtilPath +  "obsutil cp %s obs://%s%s -r -f -fr -u -vlength -flat -p=10 -threshold=100MB -ps=10MB";

    /**
     * 上传对象
     * obsutil cp /src1/test3.txt  obs://bucket-test    上传本地src1文件夹下的test3.txt文件至bucket-test桶的根目录
     * obsutil cp /src1/test3.txt  obs://bucket-test/aaa.txt    上传本地src1文件夹下的test3.txt文件至bucket-test桶的根目录，并且重命令为aaa.txt
     * obsutil cp /src1/test3.txt  obs://bucket-test/src/    上传本地src1文件夹下的test3.txt文件至bucket-test桶的src文件夹中
     * 参数1-指定文件夹 参数3-桶名 参数4-[""-桶的根目录]["/src/xxx/"-指定文件夹下]["/src/aaa.txt"-重命名]
     */
    public static final String uploadObject = obsUtilPath +  "obsutil cp %s obs://%s%s -f -p=10 -threshold=100MB -ps=10MB";

    /**
     * 下载对象
     * obsutil cp obs://bucket-test/test1.txt  /src1 下载bucket-test桶中的test1.txt文件至本地的src1文件夹中
     * obsutil cp obs://bucket-test/test1.txt  /test.txt 下载bucket-test桶中的test1.txt文件至本地，如果本地不存在test.txt文件，则直接下载且下载后命名为text.txt，如果本地已存在test.txt文件，则以test1.txt内容覆盖test.txt进行下载
     * 参数1-桶  参数3-指定对象  参数4-本地目录或者文件路径
     */
    public static final String downloadObject = obsUtilPath +  "obsutil cp obs://%s/%s  %s";

    /**
     * 下载文件夹
     * obsutil cp obs://bucket-test/src2  /src1 -r -f    递归下载bucket-test桶中的src2文件夹中的所有文件和文件夹（包括src2文件夹本身）至本地已存在的src1文件夹，且下载过程中不进行询问操作
     * obsutil cp obs://bucket-test/src2  /src1 -r -f -flat  递归下载bucket-test桶中的src2文件夹中的所有文件和文件夹（不包括src2文件夹本身）至本地已存在的src1文件夹，且下载过程中不进行询问操作
     * obsutil cp obs://bucket-test  /src0 -r -f 递归下载bucket-test桶中的所有文件至本地已存在的src0文件夹中，且下载过程中不进行询问操作
     * 参数1-桶  参数3-[指定目录][""-桶下所有的文件夹]  参数4-本地目录
     */
    public static final String downloadFolder = obsUtilPath +  "obsutil cp obs://%s%s  %s -r -f";
    public static final String downloadFlatFolder = obsUtilPath +  "obsutil cp obs://%s%s  %s -r -f -flat -u -vlength ";

    /**
     * 删除对象
     * 参数1-桶  参数3-指定对象的路径
     */
    public static final String deleteObject = obsUtilPath +  "obsutil rm obs://%s/%s -f ";
}
