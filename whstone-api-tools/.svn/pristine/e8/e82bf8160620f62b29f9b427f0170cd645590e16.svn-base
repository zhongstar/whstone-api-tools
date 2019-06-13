package com.whstone.utils.checkItem;

import com.whstone.utils.cmd.CommandUtil;
import com.whstone.utils.file.FileUtils;
import com.whstone.utils.ip.IpService;

import java.io.*;
import java.util.Iterator;
import java.util.Properties;

/**
 * Created by xzli on 2019/02/18
 */
public class CheckItemUtil {

    private static String osName;

    static {
        Properties pros = System.getProperties();
        osName = (String) pros.get("os.name");
    }

    /**
     * 检查数据库监听、环境变量、状态、是否归档等
     *
     * @return result 结果集
     */
    public static String checkOracleItem() {

        String result = "";

        //1.检查监听
        String lsnStatus = "";
        if (osName != null && (osName.startsWith("Linux") || osName.startsWith("AIX"))) {
            lsnStatus = CommandUtil.execLinuxCommand("lsnrctl stat");
            if (lsnStatus.contains("No listener")) {
                result = "数据库监听未启动！\n";
            }
        } else if (osName != null && osName.startsWith("Windows")) {
            lsnStatus = CommandUtil.execWinCommand("lsnrctl stat");
            if (lsnStatus.contains("无监听程序")) {
                result = "数据库监听未启动！\n";
            }
        }

        //2.检查oracle的环境变量是否配置
        String oracleHome = null;
        String oracleBase = null;
        if (osName != null && (osName.startsWith("Linux") || osName.startsWith("AIX"))) {

            oracleHome = CommandUtil.execLinuxCommand("echo $ORACLE_HOME").trim();
            oracleBase = CommandUtil.execLinuxCommand("echo $ORACLE_BASE").trim();

        } else if (osName != null && osName.startsWith("Windows")) {

            oracleHome = System.getenv("ORACLE_HOME");
            oracleBase = System.getenv("ORACLE_BASE");
        }
        if (null == oracleHome || null == oracleBase) {
            result = result + "ORACLE环境变量未配置！\n";
        }

        //3.检查数据库状态
        String path = System.getProperty("user.dir");
        String sql = "WHENEVER OSERROR EXIT FAILURE\n" +
                "conn / as sysdba\n" +
                "WHENEVER SQLERROR EXIT FAILURE\n" +
                "select status from v$instance;\n" +
                "quit;\n";
        File oracleStatusSql = new File(path + File.separator + "oracle_status_sql.sql");
        if (!oracleStatusSql.exists()) {
            try {
                oracleStatusSql.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        FileUtils.writeString(oracleStatusSql, sql);
        String getStatusCmd = "sqlplus -s /nolog @" + path + File.separator + "oracle_status_sql.sql";
        String statusResult = "";
        if (osName != null && (osName.startsWith("Linux") || osName.startsWith("AIX"))) {
            statusResult = CommandUtil.execLinuxCommand(getStatusCmd);
        } else if (osName != null && osName.startsWith("Windows")) {
            statusResult = CommandUtil.execWinCommand(getStatusCmd);
        }
        if (statusResult.contains("ERROR") || !statusResult.contains("OPEN")) {
            result = result + "数据库未启动！\n";
        }

        //4.检查是否开启了归档
        sql = "WHENEVER OSERROR EXIT FAILURE\n" +
                "conn / as sysdba\n" +
                "WHENEVER SQLERROR EXIT FAILURE\n" +
                "select log_mode from v$database;\n" +
                "quit;\n";
        File archivelogSql = new File(path + File.separator + "archivelog_status_sql.sql");
        if (!archivelogSql.exists()) {
            try {
                archivelogSql.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        FileUtils.writeString(archivelogSql, sql);
        String archivelogStatusCmd = "sqlplus -s /nolog @" + path + File.separator + "archivelog_status_sql.sql";
        String archivelogResult = "";
        if (osName != null && (osName.startsWith("Linux") || osName.startsWith("AIX"))) {
            archivelogResult = CommandUtil.execLinuxCommand(archivelogStatusCmd);
        } else if (osName != null && osName.startsWith("Windows")) {
            archivelogResult = CommandUtil.execWinCommand(archivelogStatusCmd);
        }
        if (archivelogResult.contains("ERROR") || !archivelogResult.contains("ARCHIVELOG")) {
            result = result + "数据库归档模式未开启！\n";
        }
        return result;
    }


    /**
     * 检查启动端口
     *
     * @return result 结果集
     */
    public static String checkDbPort() {
        String result = "";
        int dbport = 0;
        File directory = new File("");//设定为当前文件夹
        String infoPath = directory.getAbsolutePath() + File.separator + "dbport.properties";
        Properties prop = new Properties();
        try {
            //读取属性文件a.properties
            InputStream in = new BufferedInputStream(new FileInputStream(infoPath));
            prop.load(in);     ///加载属性列表
            Iterator<String> it = prop.stringPropertyNames().iterator();
            while (it.hasNext()) {
                String key = it.next();
                if (key.equals("agent.dbport")) {
                    dbport = Integer.valueOf(prop.getProperty(key));
                }
            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        boolean portStatus = IpService.isHostConnectable(IpService.getLocalAddress(), dbport);
        if (!portStatus) {
            result = "端口不正确！\n";
        }
        return result;
    }

    /**
     * 检查NFS挂载
     *
     * @return result 结果集
     */
    public static String checkNFS() {
        String result = "";
        String nfsStatus = "";
        if (osName != null && (osName.startsWith("Linux") || osName.startsWith("AIX"))) {
            nfsStatus = CommandUtil.execLinuxCommand("mount");
            if (!nfsStatus.contains("/backup/WCG")) {
                result = "NFS未挂载！\n";
            }
        } else if (osName != null && osName.startsWith("Windows")) {
            nfsStatus = CommandUtil.execWinCommand("mount");
            if (!nfsStatus.contains("\\backup\\WCG")) {
                result = "NFS未挂载！\n";
            }
        }
        return result;
    }

    /**
     * 检查文件系统wbadmin和rsync是否启用
     *
     * @return
     */
    public static String checkFileItem() {
        String result = "";
        if (osName != null && (osName.startsWith("Linux") || osName.startsWith("AIX"))) {

            Boolean rsyncStatus = CommandUtil.getLinuxStateExec("rsync -h");
            if (!rsyncStatus) {
                result = "RSYNC不可运行！\n";
            }

        } else if (osName != null && osName.startsWith("Windows")) {
            Boolean status = CommandUtil.getStateExec("wbadmin get disks");
            if (!status) {
                result = "wbadmin未开启！\n";
            }
//            File rsyncFile = new File(System.getProperty("user.dir") + "\\rsync\\bin\\rsync.exe");
//            if (!rsyncFile.exists()) {
//                result = "RSYNC执行文件不存在！\n";
//            }
        }
        return result;
    }

    public static String getOsName() {
        return osName;
    }

    public static void setOsName(String osName) {
        CheckItemUtil.osName = osName;
    }
}
