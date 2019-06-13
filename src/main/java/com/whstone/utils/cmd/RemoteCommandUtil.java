package com.whstone.utils.cmd;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;
import ch.ethz.ssh2.StreamGobbler;
import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbFile;
import jcifs.smb.SmbFileInputStream;
import jcifs.smb.SmbFileOutputStream;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

public class RemoteCommandUtil {

    private static final Logger log = LoggerFactory.getLogger(RemoteCommandUtil.class);


    private static String domainip = "145.170.23.9";
    private static String username = "Administrator";
    private static String password = "P@ssw0rd";
    private static String remoteurl = "smb://145.170.23.9/SHARE/";


    private static String DEFAULTCHART = "UTF-8";

    /**
     * 登录主机
     *
     * @return 登录成功返回true，否则返回false
     */
    public static Connection login(String ip,
                                   String userName,
                                   String userPwd) {

        boolean flg = false;
        Connection conn = null;
        try {
            conn = new Connection(ip);
            conn.connect();//连接
            flg = conn.authenticateWithPassword(userName, userPwd);//认证
            if (flg) {
                log.info("=========登录成功=========" + conn);
                return conn;
            }
        } catch (IOException e) {
            log.error("=========登录失败=========" + e.getMessage());
            e.printStackTrace();
        }
        return conn;
    }

    /**
     * 远程执行shll脚本或者命令
     *
     * @param cmd 即将执行的命令
     * @return 命令执行完后返回的结果值
     */
    public static String execute(Connection conn, String cmd) {
        String result = "";
        try {
            if (conn != null) {
                Session session = conn.openSession();//打开一个会话
                session.execCommand(cmd);//执行命令
                result = processStdout(session.getStdout(), DEFAULTCHART);
                log.info(result);
                //如果为得到标准输出为空，说明脚本执行出错了
                if (StringUtils.isBlank(result)) {
                    log.info("得到标准输出为空,链接conn:" + conn + ",执行的命令：" + cmd);
                    result = processStdout(session.getStderr(), DEFAULTCHART);
                } else {
                    log.info("执行命令成功,链接conn:" + conn + ",执行的命令：" + cmd);
                }
                conn.close();
                session.close();
            }
        } catch (IOException e) {
            log.info("执行命令失败,链接conn:" + conn + ",执行的命令：" + cmd + "  " + e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 远程执行shll脚本或者命令
     *
     * @param cmd 即将执行的命令
     * @return 命令执行完后返回的结果值
     */
    public static String execute2(Connection conn, String cmd) {
        String result = "";
        try {
            if (conn != null) {
                Session session = conn.openSession();//打开一个会话
                session.execCommand(cmd);//执行命令
                result = processStdout(session.getStdout(), DEFAULTCHART);
                log.info(result);
                //如果为得到标准输出为空，说明脚本执行出错了
                if (StringUtils.isBlank(result)) {
                    log.info("得到标准输出为空,链接conn:" + conn + ",执行的命令：" + cmd);
                    result = processStdout(session.getStderr(), DEFAULTCHART);
                } else {
                    log.info("执行命令成功,链接conn:" + conn + ",执行的命令：" + cmd);
                }
            }
        } catch (IOException e) {
            log.info("执行命令失败,链接conn:" + conn + ",执行的命令：" + cmd + "  " + e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 解析脚本执行返回的结果集
     *
     * @param in      输入流对象
     * @param charset 编码
     * @return 以纯文本的格式返回
     */
    private static String processStdout(InputStream in, String charset) {
        InputStream stdout = new StreamGobbler(in);
        StringBuffer buffer = new StringBuffer();
        ;
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(stdout, charset));
            String line = null;
            while ((line = br.readLine()) != null) {
                buffer.append(line + "\n");
            }
        } catch (UnsupportedEncodingException e) {
            log.error("解析脚本出错：" + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            log.error("解析脚本出错：" + e.getMessage());
            e.printStackTrace();
        }
        return buffer.toString();
    }

    /**
     * 获取远程文件
     *
     * @param remoteUsername 远程目录访问用户名
     * @param remotePassword 远程目录访问密码
     * @param remoteFilepath 远程文件地址,该参数需以IP打头,如'192.168.8.2/aa/bb.java'或者'192.168.8.2/aa/',如'192.168.8.2/aa'是不对的
     * @param localDirectory 本地存储目录,该参数需以'/'结尾,如'D:/'或者'D:/mylocal/'
     * @return boolean 是否获取成功
     * @throws Exception
     */
    public static boolean getRemoteFile(String remoteUsername, String remotePassword, String remoteFilepath,
                                        String localDirectory) throws Exception {
        boolean isSuccess = Boolean.FALSE;
        if (remoteFilepath.startsWith("/") || remoteFilepath.startsWith("\\")) {
            return isSuccess;
        }
        if (!(localDirectory.endsWith("/") || localDirectory.endsWith("\\"))) {
            return isSuccess;
        }
        SmbFile smbFile = null;
        if (StringUtils.isNotEmpty(remoteUsername) && StringUtils.isNotEmpty(remotePassword)) {
            smbFile = new SmbFile("smb://" + remoteUsername + ":" + remotePassword + "@" + remoteFilepath);
            log.debug("smb://" + remoteUsername + ":" + remotePassword + "@" + remoteFilepath);
        } else {
            smbFile = new SmbFile("smb://" + remoteFilepath);
            log.debug("smb://" + remoteFilepath);
        }

        System.out.println(smbFile.getType() + "==" + smbFile.isFile());
        if (smbFile != null) {
            if (smbFile.isDirectory()) {
                log.info(String.valueOf(smbFile.getDiskFreeSpace()));
                for (SmbFile file : smbFile.listFiles()) {
                    log.info(String.valueOf(file.getContentLength()));
                    //     isSuccess = copyRemoteFile(file, localDirectory);
                }
            } else if (smbFile.isFile()) {
                //   isSuccess = copyRemoteFile(smbFile, localDirectory);
            }
        }

        return isSuccess;
    }

    /**
     * 拷贝远程文件到本地目录
     *
     * @param smbFile        远程SmbFile
     * @param localDirectory 本地存储目录,本地目录不存在时会自动创建,本地目录存在时可自行选择是否清空该目录下的文件,默认为不清空
     * @return boolean 是否拷贝成功
     */
    public static boolean copyRemoteFile(SmbFile smbFile, String localDirectory) {
        SmbFileInputStream in = null;
        FileOutputStream out = null;
        try {
            File[] localFiles = new File(localDirectory).listFiles();
            if (null == localFiles) {
                // 目录不存在的话,就创建目录
                new File(localDirectory).mkdirs();
            }
            in = new SmbFileInputStream(smbFile);
            out = new FileOutputStream(localDirectory + smbFile.getName());
            byte[] buffer = new byte[1024];
            int len = -1;
            while ((len = in.read(buffer)) != -1) {
                out.write(buffer, 0, len);
            }
            out.flush();
        } catch (Exception e) {
            log.error("拷贝远程文件到本地目录失败", e);
            return false;
        } finally {
            if (null != out) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null != in) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return true;
    }

    public static boolean copyLocalFileToRemote(SmbFile smbFile, String localFilePath) {
        SmbFileOutputStream out = null;
        FileInputStream in = null;
        try {
            File localFile = new File(localFilePath);
            smbFile.createNewFile();
            in = new FileInputStream(localFile);
            out = new SmbFileOutputStream(smbFile);
            byte[] buffer = new byte[1024];
            int len;
            while ((len = in.read(buffer)) != -1) {
                out.write(buffer, 0, len);
            }
            out.flush();
        } catch (Exception e) {
            log.error("拷贝远程文件到本地目录失败", e);
            return false;
        } finally {
            if (null != out) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null != in) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return true;
    }


    public static void main(String[] args) {
        // Linux
        Connection conn = RemoteCommandUtil.login("47.107.80.174", "root", "Cl314159");
        String execute = RemoteCommandUtil.execute(conn, "sh /chenli/java/restart.sh");
        System.out.println(execute);
//        Connection conn = RemoteCommandUtil.login("145.170.23.212", "root", "123456");
//        RemoteCommandUtil.execute(conn, "du -sm /backup/WCG/oracle");

        //  RemoteCommandUtil.getNetWorkFile();

// smb:域名;用户名:密码@目的IP/文件夹/文件名.xxx
        /**
         * windows
         */
        try {
            NtlmPasswordAuthentication auth = new NtlmPasswordAuthentication(domainip, username, password);  //先登录验证
            SmbFile smbFile = new SmbFile(remoteurl + "\\", auth);

            // 通过 smbFile.isDirectory();isFile()可以判断smbFile是文件还是文件夹
            int length = smbFile.getContentLength();// 得到文件的大小

            if (smbFile != null) {
                System.out.println(length);
                System.out.println("总容量：" + smbFile.length());
                log.info("剩余容量：" + String.valueOf(smbFile.getDiskFreeSpace()));
                //  log.info("已使用容量：" + String.valueOf(smbFile.length() - smbFile.getDiskFreeSpace()));
            }
        } catch (
                Exception e) {
            e.printStackTrace();
        }


    }
}
