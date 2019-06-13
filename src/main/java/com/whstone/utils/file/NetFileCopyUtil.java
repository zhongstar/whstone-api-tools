package com.whstone.utils.file;

import com.whstone.utils.cmd.CommandUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by zhongkf on 2018/08/06
 */
public class NetFileCopyUtil {

    private static final Logger logger = LoggerFactory.getLogger(NetFileCopyUtil.class);

    /**
     * 将 本地指定路径下的所有文件(夹) 拷贝到共享路径下
     *
     * @param localBackupPath 本地待拷贝的路径 C:\\app\\user-app\\7-Zip
     * @param shareDisk       共享盘符 D:
     * @param shareUser       共享所需系统用户名  administrator
     * @param sharePass       共享所需系统密码 P@ssw0rd
     * @param sharePath       挂载共享路径 \\145.170.23.228\test 不以 \ 结尾
     * @return
     */
    public static Boolean copyFileByNetUse(String localBackupPath, String shareDisk, String shareUser, String sharePass, String sharePath) {

        //挂载之前先清除原先挂载
        CommandUtil.getStateExec("net use " + shareDisk + " /del");

        String cmd = "net use " + shareDisk + " " + sharePath + " " + sharePass + " /user:" + shareUser;
        Boolean success = CommandUtil.getStateExec(cmd);

        if (!success) {

            logger.info("挂载共享盘 {} 失败", shareDisk);
            return false;
        }
        logger.info("挂载共享盘 {} 成功", shareDisk);
        try {
            FileUtils.copyDir(localBackupPath, shareDisk);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("复制文件(夹)失败");
            logger.info("复制文件(夹) {} 失败", localBackupPath);
            return false;
        }
        logger.info("复制文件(夹) {} 成功", localBackupPath);
        CommandUtil.getStateExec("net use " + shareDisk + " /del");
        return success;
    }

//    public static void main(String[] args) {
//        System.out.println(NetFileCopyUtil.copyFileByNetUse(
//            "C:\\\\app\\\\user-app\\\\7-Zip",
//               "D:",
//                "administrator",
//                "P@ssw0rd",
//                "\\\\145.170.23.228\\test"
//        ));
//    }

}