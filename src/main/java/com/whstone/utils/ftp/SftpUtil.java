package com.whstone.utils.ftp;

import com.jcraft.jsch.Session;
import cn.hutool.extra.ssh.JschUtil;
import cn.hutool.extra.ssh.Sftp;

/**
 * Created by zhongkf on 2019/05/17
 */
public class SftpUtil {


    public static Sftp createSftp(String hostIp,
                                  String user,
                                  String pass) {

        return JschUtil.createSftp(hostIp, 22, user, pass);

    }

    public static Session createSession(String hostIp,
                                        String user,
                                        String pass) {

        return JschUtil.getSession(hostIp, 22, user, pass);

    }

}