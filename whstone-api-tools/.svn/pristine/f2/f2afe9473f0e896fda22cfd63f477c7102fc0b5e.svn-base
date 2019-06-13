package com.whstone.utils.txt;

import java.io.*;

/**
 * Created by weijun on 2017/8/2.
 */
public class WriteStringToTxt {

    public static void WriteStringToFile(String filePath, String content) {
        try {
            File file = new File(filePath);
            PrintStream ps = new PrintStream(new FileOutputStream(file));
            ps.println(content + "\r\n");// 往文件里写入字符串
            // ps.append("http://www.baidu.com");// 在已有的基础上添加字符串
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * 往文本中增加新内容，
     *
     * @param filePath
     * @param content
     */
    public static void WriteStringToFile2(String filePath, String content) {
        try {

            File filename = new File(filePath);

            if (!filename.exists()) {

                filename.createNewFile();

            }
            FileWriter fw = new FileWriter(filePath, true);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.append("新增加内容\r\n");
            bw.write(content + "\r\n");// 往已有的文件上添加字符串
            bw.close();
            fw.close();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void WriteStringToFile3(String filePath, String content) {
        try {
            PrintWriter pw = new PrintWriter(new FileWriter(filePath));
            pw.println(content + "\r\n");
            pw.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * 往文本中增加内容
     *
     * @param filePath
     * @param content
     */
    public static void WriteStringToFile4(String filePath, String content) {
        /**
         * RandomAccessFile的特点在于任意访问文件的任意位置，可以说是基于字节访问的，可通过getFilePointer()获取当前指针所在位置，
         * 可通过seek()移动指针，这体现了它的任意性，也是其与其他I/O流相比，自成一派的原因
         *
         * 一句话总结：seek用于设置文件指针位置，设置后ras会从当前指针的下一位读取到或写入到
         * 操作文件有4种模式："r"、"rw"、"rws" 或 "rwd"

         如果模式为只读r。则不会创建文件，而是会去读取一个已经存在的文件，如果读取的文件不存在则会出现异常。
         如果模式为rw读写。如果文件不存在则会去创建文件，如果存在则不会创建。
         */
        try {
// 打开一个随机访问文件流，按读写方式
            RandomAccessFile randomFile = new RandomAccessFile(filePath, "rw");
// 文件长度，字节数
            long fileLength = randomFile.length() - 2;
// 将写文件指针移到文件尾。
            randomFile.seek(fileLength);
            randomFile.write(("\n" + content + "\n\n}").getBytes("gb2312"));
            randomFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void WriteStringToFile5(String filePath, String content) {
        try {
            FileOutputStream fos = new FileOutputStream(filePath);
            String s = content;
            fos.write(s.getBytes());
            fos.close();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String filePath = "C:\\Users\\weijun\\Downloads\\link.txt";
        String content = " 这么多年的兄弟\n" +
                "有谁比我更了解你\n" +
                "太多太多不容易\n" +
                "磨平了岁月和脾气\n" +
                "时间转眼就过去\n" +
                "这身后不散的筵席\n" +
                "只因为我们还在\n" +
                "心留在原地\n" +
                "张开手需要多大的勇气\n" +
                "这片天你我一起撑起";
        // new WriteStringToTxt().WriteStringToFile(filePath,content);
//         new WriteStringToTxt().WriteStringToFile2(filePath,"更努力只为了我们想要的明天\n" +
//                 "好好的这份情好好珍惜\n" +
//                 "我们不一样\n" +
//                 "每个人都有不同的境遇\n" +
//                 "我们在这里\n" +
//                 "在这里等你\n" +
//                 "我们不一样\n" +
//                 "虽然会经历不同的事情\n" +
//                 "我们都希望\n" +
//                 "来生还能相遇\n" +
//                 "这么多年的兄弟\n" +
//                 "有谁比我更了解你\n" +
//                 "太多太多不容易\n" +
//                 "磨平了岁月和脾气\n" +
//                 "时间转眼就过去\n" +
//                 "这身后不散的筵席");
        // new WriteStringToTxt().WriteStringToFile3(filePath,content);
        //   new WriteStringToTxt().WriteStringToFile4(filePath,content);
        new WriteStringToTxt().WriteStringToFile5(filePath, "更努力只为了我们想要的明天\n" +
                "好好的这份情好好珍惜\n" +
                "我们不一样\n" +
                "每个人都有不同的境遇\n" +
                "我们在这里\n" +
                "在这里等你\n" +
                "我们不一样\n" +
                "虽然会经历不同的事情\n" +
                "我们都希望\n" +
                "来生还能相遇\n" +
                "这么多年的兄弟\n" +
                "有谁比我更了解你\n" +
                "太多太多不容易\n" +
                "磨平了岁月和脾气\n" +
                "时间转眼就过去\n" +
                "这身后不散的筵席");
    }
}
