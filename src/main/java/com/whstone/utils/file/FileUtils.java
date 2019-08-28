package com.whstone.utils.file;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.channels.FileChannel;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.CRC32;

/**
 * Created by weijun on 2017/8/2.
 */
public class FileUtils {

    private static Logger logger = LoggerFactory.getLogger(FileUtils.class);

    /**
     * 将文件从源路径拷贝到目的路径下。
     *
     * @param src  源文件路径字符串（由文件路径和文件名组成）
     * @param dest 目标文件路径
     */
    public static void copyFile(String src, String dest) throws Exception {


        File srcFile = new File(src);
        if (!srcFile.exists()) {
            return;

        }
        File destFile = new File(dest);
        mkdirs(destFile);

        InputStream in = new FileInputStream(src);
        OutputStream out = new FileOutputStream(dest + File.separator + srcFile.getName());

        copyStream(in, out);
        in.close();
        out.close();

    }

    public static void copyWriteFile(String oldPath, String newPath) throws IOException {
        File oldFile = new File(oldPath);
        File file = new File(newPath);
        FileInputStream in = new FileInputStream(oldFile);
        FileOutputStream out = new FileOutputStream(file);

        byte[] buffer = new byte[2097152];
        int readByte = 0;
        while ((readByte = in.read(buffer)) != -1) {
            out.write(buffer, 0, readByte);
        }

        in.close();
        out.close();

    }

    /**
     * 拷贝文件夹
     *
     * @param sourcePath
     * @param newPath
     * @throws Exception
     * @auth zhongkf
     */
    public static void copyDir(String sourcePath, String newPath) throws Exception {
        File file = new File(sourcePath);
        String[] filePath = file.list();

        if (!(new File(newPath)).exists()) {
            logger.info(newPath);
            (new File(newPath)).mkdirs();
        }

        for (int i = 0; i < filePath.length; i++) {
            if ((new File(sourcePath + File.separator + filePath[i])).isDirectory()) {
                logger.info("Directory:{}", newPath + File.separator + filePath[i]);
                copyDir(sourcePath + File.separator + filePath[i], newPath + File.separator + filePath[i]);
            }

            if (new File(sourcePath + File.separator + filePath[i]).isFile()) {
                logger.info("file:{}", newPath + File.separator + filePath[i]);
                copyWriteFile(sourcePath + File.separator + filePath[i], newPath + File.separator + filePath[i]);
            }

        }
    }

    /**
     * 拷贝数据流
     *
     * @param in  输入流
     * @param out 输出流
     * @throws IOException
     * @throws
     */
    public static void copyStream(InputStream in, OutputStream out) throws IOException {
        byte[] buff = new byte[4096];
        int bytesRead;

        while (-1 != (bytesRead = in.read(buff, 0, buff.length))) {
            out.write(buff, 0, bytesRead);
            out.flush();
        }
    }

    public static void copyStream(byte[] buff, OutputStream out) throws IOException {
        out.write(buff);
        out.flush();
    }

    /**
     * 创建文件目录
     *
     * @param name 目录名
     */
    public static void mkdirs(String name) {
        if (StringUtils.isNotEmpty(name)) {
            File file = new File(name);
            mkdirs(file);
        }
    }

    /**
     * 创建文件目录
     *
     * @param dir File对象
     */
    public static void mkdirs(File dir) {
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }


    /**
     * 将指定的文件内容写入到指定的文件对象中。
     *
     * @param file     File对象
     * @param contents 文件内容
     * @return boolean布尔型
     */
    public static boolean writeString(File file, String contents) {
        return writeString(file, contents, false);
    }

    /**
     * 将指定的文件内容写入到指定的文件对象中。
     *
     * @param file     File对象
     * @param contents 文件内容
     * @param append   是否追加
     * @return boolean
     */
    public static boolean writeString(File file, String contents, boolean append) {
        try {
            //     DataOutputStream dos=new DataOutputStream(new BufferedOutputStream(new FileOutputStream(file,append)));
            PrintStream dos = new PrintStream(new BufferedOutputStream(new FileOutputStream(file, append)));
            dos.print(contents);
            dos.flush();
            dos.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 将指定的文件内容写入到指定的文件对象中。
     *
     * @param name     文件名
     * @param contents 文件内容
     * @param append   是否追加
     * @return boolean
     */
    public static boolean writeString(String name, String contents, boolean append) {
        return writeString(new File(name), contents, append);
    }

    /**
     * 将指定的文件内容写入到指定的文件对象中。
     *
     * @param name     文件名
     * @param contents 文件内容
     * @return true 成功 false 失败
     */
    public static boolean writeString(String name, String contents) {
        return writeString(new File(name), contents);
    }

    /**
     * 将byte数组中的内容写入到File对象中。
     *
     * @param file 文件
     * @param buff 文件内容
     * @return true写文件成功 false写文件失败
     */
    public static boolean writeBytes(File file, byte[] buff) {
        try {
            OutputStream out = new FileOutputStream(file);
            out.write(buff);
            out.flush();
            out.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 将byte数组中的内容输出到文件中。
     *
     * @param name 文件名
     * @param buff 文件内容
     * @return true写文件成功 false写文件失败
     */
    public static boolean writeBytes(String name, byte[] buff) {
        return writeBytes(new File(name), buff);
    }

    /**
     * 将输入流写入文件中。
     *
     * @param name 文件名
     * @param in   输入流
     * @return true写文件成功 false写文件失败
     */
    public static boolean write(String name, InputStream in) {
        return write(new File(name), in);
    }

    /**
     * 将输入流写入文件中。
     *
     * @param file 文件
     * @param in   输入流
     * @return true写文件成功 false写文件失败
     */
    public static boolean write(File file, InputStream in) {
        try {
            OutputStream out = new FileOutputStream(file);
            copyStream(in, out);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * 获取CRC32效验码。
     *
     * @param buf 缓存流
     * @return CRC32效验码
     */
    public static String getCRC32(byte[] buf) {
        CRC32 crc32 = new CRC32();
        crc32.update(buf);
        long value = crc32.getValue();
        String hexValue = Long.toHexString(value).toUpperCase();
        for (int i = hexValue.length(); i < 8; i++)
            hexValue = "0" + hexValue;
        return hexValue;
    }

    /**
     * 获取文件的扩展名。
     *
     * @param fileName 文件名
     * @return java.lang.String
     */
    public static String getExtendName(String fileName) {
        if (StringUtils.isNotEmpty(fileName)) {
            return null;
        }
        int index = fileName.lastIndexOf('.');
        if (index == -1) {
            return null;
        }
        return fileName.substring(index + 1).toLowerCase();
    }


    /**
     * 移动文件。
     *
     * @param dest 目录路径
     * @return
     */
    public static boolean move(String src, String dest) {
        File srcFile = new File(src);
        File destFile = new File(dest);
        if (destFile.exists()) {
            destFile.delete();
        }
        if (!destFile.getParentFile().exists()) {
            destFile.getParentFile().mkdirs();
        }
        return srcFile.renameTo(destFile);
    }


    /**
     * 生成导出附件中文名。应对导出文件中文乱码
     * <p>
     * response.addHeader("Content-Disposition", "attachment; filename=" + cnName);
     *
     * @param cnName
     * @param defaultName
     * @return
     */
    public static String genAttachmentFileName(String cnName, String defaultName) {
        try {
//            fileName = URLEncoder.encode(fileName, "UTF-8");
            cnName = new String(cnName.getBytes("gb2312"), "ISO8859-1");
            /*
            if (fileName.length() > 150) {
                fileName = new String( fileName.getBytes("gb2312"), "ISO8859-1" );
            }
            */
        } catch (Exception e) {
            cnName = defaultName;
        }
        return cnName;
    }

    /**
     * size单位转化
     *
     * @param file
     * @return
     * @auth zhongkf
     */
    public static String formetSize(long file) {
        DecimalFormat df = new DecimalFormat("#.00");
        String sizeString = "";
        if (file < 1024) {
            sizeString = df.format((double) file) + "B";
        } else if (file < 1024 * 1024) {
            sizeString = df.format((double) file / 1024) + "K";
        } else if (file < 1024 * 1024 * 1024) {
            sizeString = df.format((double) file / 1024 / 1024) + "M";
        } else {
            sizeString = df.format((double) file / (1024 * 1024 * 1024)) + "G";
        }
        return sizeString;
    }


    /**
     * 获取文件夹 size
     *
     * @param file
     * @return
     * @auth zhongkf
     */
    public static long getFolderSize(File file) {

        long size = 0;
        try {
            File[] fileList = file.listFiles();
            if (fileList != null) {
                for (int i = 0; i < fileList.length; i++) {
                    if (fileList[i].isDirectory()) {
                        size = size + getFolderSize(fileList[i]);

                    } else {
                        size = size + fileList[i].length();

                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return size;
    }

    /**
     * 读取文件对象内容
     *
     * @param file
     * @return
     * @auth zhongkf
     */
    public static String readFile(File file) throws IOException {

        if (!file.exists()) {

            return "";

        }

        BufferedReader r = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
        StringBuffer buffer = new StringBuffer();
        String line;
        while ((line = r.readLine()) != null) {

            buffer.append(line + "\n");

        }
        return buffer.toString();

    }

    /**
     * 读取文件的内容
     *
     * @param path 文件路径
     * @return
     * @auth zhongkf
     */
    public static String readFileString(String path) throws IOException {

        File file = new File(path);
        return readFile(file);

    }

    /**
     * 文件覆盖
     *
     * @param sPath 覆盖 dPath
     * @return
     * @auth zhongkf
     */
    public static boolean overwriteFile(String sPath, String dPath) {

        File sfile = new File(sPath);
        if (!sfile.exists()) {
            return false;
        }
        File dfile = new File(dPath);
        if (!dfile.exists()) {
            return false;
        }
        FileChannel inputChannel = null;
        FileChannel outputChannel = null;
        try {
            inputChannel = new FileInputStream(sfile).getChannel();
            outputChannel = new FileOutputStream(dfile).getChannel();
            outputChannel.transferFrom(inputChannel, 0, inputChannel.size());
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                inputChannel.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                outputChannel.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        return true;

    }

    /**
     * 获取文件夹下文件集合
     *
     * @param path
     * @return
     */
    public static List<String> getFileName(String path) {
        File file = new File(path);
        String[] names = file.list();

        List<String> nameList = new ArrayList<>();
        for (String name : names) {
            logger.info(name);
            nameList.add(name);

        }
        return nameList;

    }

    /**
     * 将文件夹路径以 "/" 或者 "\"结束
     *
     * @param path
     * @param separator
     * @return
     */
    public static String endWithSeparator(String path, String separator) {

        return path.substring(path.length() - 1).equals(separator) ? path : path + separator;

    }

    /**
     * 将文件夹路径不以 "/" 或者 "\"结束
     *
     * @param path
     * @param separator
     * @return
     */
    public static String endWithoutSeparator(String path, String separator) {

        return path.substring(path.length() - 1).equals(separator) ? path.substring(0, path.length() - 1) : path;

    }

    /**
     * 截取路径中的文件(夹)名
     *
     * @return
     */
    public static String splitFileName(String path, String separator) {

        String s = endWithoutSeparator(path, separator);
        return s.split(separator)[s.split(separator).length - 1];


    }

    /**
     * 根据路径和文件名获取文件大小
     *
     * @param path
     * @param name
     * @return
     */
    public static String getfileSize(String path, String name) {
        File f = new File(path + name);

        return FileUtils.formetSize(f.length());
    }


    /**
     * 判断字符串中是否包含中文
     *
     * @param str 待校验字符串
     * @return 是否为中文
     * @warn 不能校验是否为中文标点符号
     */
    public static boolean isContainChinese(String str) {
        Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
        Matcher m = p.matcher(str);
        if (m.find()) {
            return true;
        }
        return false;
    }

    /**
     * 判断文件中是否包含某个字符串
     *
     * @param path      文件路径
     * @param searchStr 待校验字符串
     */
    public static boolean findStringInFile(String path, String searchStr) throws IOException {

        InputStreamReader read = new InputStreamReader(new FileInputStream(new File(path)), "UTF-8");//考虑到编码格式
        BufferedReader bufferedReader = new BufferedReader(read);
        String line = null;
        while ((line = bufferedReader.readLine()) != null) {
            if (line.startsWith("#")) {
                continue;
            }
            //指定字符串判断处
            if (line.contains(searchStr)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 替换文件中指定文件内容
     *
     * @param target      待替换字符串
     * @param replacement 新的字符串
     * @return
     */
    public static Boolean replacFileContent(String filePath, String target, String replacement) {
        //原有的内容
        // 读
        File file = new File(filePath);
        if (!file.exists()) {
            return false;
        }
        try {
            FileReader in = new FileReader(file);
            BufferedReader bufIn = new BufferedReader(in);
            // 内存流, 作为临时流
            CharArrayWriter tempStream = new CharArrayWriter();
            // 替换
            String line = null;
            while ((line = bufIn.readLine()) != null) {
                // 替换每行中, 符合条件的字符串
                line = line.replaceAll(target, replacement);
                // 将该行写入内存
                tempStream.write(line);
                // 添加换行符
                tempStream.append(System.getProperty("line.separator"));
            }
            // 关闭 输入流
            bufIn.close();
            // 将内存中的流 写入 文件
            FileWriter out = new FileWriter(file);
            tempStream.writeTo(out);
            out.close();

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static void main(String[] args) {
        Long ll = getFolderSize(new File("D:\\"));
        System.out.println(ll.toString());
    }
}
