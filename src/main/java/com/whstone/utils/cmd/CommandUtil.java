package com.whstone.utils.cmd;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Properties;

/**
 * Created by zhongkf on 2018/5/2
 */
public class CommandUtil {

    private static String osName;

    static {
        Properties pros = System.getProperties();
        osName = (String) pros.get("os.name");
    }

    /**
     * windows 根据执行命令获取返回结果信息
     *
     * @param command
     * @return result 结果集
     */
    public static String execWinCommand(String command) {
        String result = null;
        Process p = null;
        BufferedReader r = null;
        try {
            if (command.contains("powershell")) {
                p = Runtime.getRuntime().exec(command);
            } else {
                p = Runtime.getRuntime().exec("cmd /c " + command);
            }

            r = new BufferedReader(new InputStreamReader(p.getInputStream(), Charset.forName("GBK")));
            StringBuffer buffer = new StringBuffer();
            String line;
            while ((line = r.readLine()) != null) {

                buffer.append(line + "\n");

            }
            result = buffer.toString();
            p.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (p != null) {
                    p.destroy();
                }
                if (r != null) {
                    r.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * windows 调用dg broker 命令，获取解析结果
     *
     * @param command
     * @return result 结果集
     */
    public static String execWinDGCommand(String command) {
        String result = null;
        Process p = null;
        BufferedReader r = null;
        try {
            p = Runtime.getRuntime().exec("cmd /c " + command);
            p.waitFor();
            r = new BufferedReader(new InputStreamReader(p.getInputStream(), Charset.forName("GBK")));
            StringBuffer buffer = new StringBuffer();
            String line;
            while ((line = r.readLine()) != null) {

                if (line.contains("已连接") || line.contains("身份连接")) {
                    while ((line = r.readLine()) != null) {
                        buffer.append(line + "\n");
                    }
                }

            }
            result = buffer.toString();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (p != null) {
                    p.destroy();
                }
                if (r != null) {
                    r.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * windows 执行命令，并获取命令执行状态
     *
     * @param command
     * @return
     */
    public static Boolean getStateExec(String command) {
        boolean result = false;
        Process p = null;
        try {
            p = Runtime.getRuntime().exec("cmd /c " + command);
            p.waitFor();
            result = p.exitValue() == 0;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            if (p != null) {
                p.destroy();
            }
        }
        return result;
    }

    /**
     * Linux 根据执行命令获取返回结果信息
     *
     * @param command
     * @return result 结果集
     */
    public static String execLinuxCommand(String command) {
        String result = null;
        Process p = null;
        BufferedReader r = null;
        try {
            if (osName != null && osName.startsWith("Linux")) {

                String[] cmds = {"sh", "-c", command};
                p = Runtime.getRuntime().exec(cmds);

            } else if (osName != null && osName.startsWith("AIX")) {

                String[] cmds = {"sh", "-c", command};
                p = Runtime.getRuntime().exec(cmds);

            } else {
                //非unix或linux系统
            }
            p.waitFor();
            r = new BufferedReader(new InputStreamReader(p.getInputStream(), Charset.forName("GBK")));
            StringBuffer buffer = new StringBuffer();
            String line;
            while ((line = r.readLine()) != null) {

                buffer.append(line + "\n");

            }
            result = buffer.toString();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (p != null) {
                    p.destroy();
                }
                if (r != null) {
                    r.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * Linux 调用dg broker 命令，获取解析结果
     *
     * @param command
     * @return result 结果集
     */
    public static String execLinuxDGCmd(String command) {
        String result = null;
        Process p = null;
        BufferedReader r = null;
        try {
            if (osName != null && osName.startsWith("Linux")) {

                String[] cmds = {"sh", "-c", command};
                p = Runtime.getRuntime().exec(cmds);

            } else if (osName != null && osName.startsWith("AIX")) {

                String[] cmds = {"sh", "-c", command};
                p = Runtime.getRuntime().exec(cmds);

            } else {
                //非unix或linux系统
            }
            p.waitFor();
            r = new BufferedReader(new InputStreamReader(p.getInputStream(), Charset.forName("GBK")));
            StringBuffer buffer = new StringBuffer();
            String line;
            while ((line = r.readLine()) != null) {

                if (r.readLine().contains("Connected")) {
                    while ((line = r.readLine()) != null) {
                        buffer.append(line + "\n");
                    }
                }
            }
            result = buffer.toString();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (p != null) {
                    p.destroy();
                }
                if (r != null) {
                    r.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * linux 执行命令，并获取命令执行状态
     *
     * @param command
     * @return
     */
    public static Boolean getLinuxStateExec(String command) {
        boolean result = false;
        Process p = null;
        try {
            if (osName != null && osName.startsWith("Linux")) {

                String[] cmds = {"sh", "-c", command};
                p = Runtime.getRuntime().exec(cmds);

            } else if (osName != null && osName.startsWith("AIX")) {

                String[] cmds = {"sh", "-c", command};
                p = Runtime.getRuntime().exec(cmds);

            } else {
                //非unix或linux系统
            }
            p.waitFor();
            result = p.exitValue() == 0;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            if (p != null) {
                p.destroy();
            }
        }
        return result;
    }

    /**
     * Linux-oracle-12c 调用dg broker 命令，获取解析结果
     *
     * @param command
     * @return result 结果集
     */
    public static String execLinux12CDGCmd(String command) {

        String result = null;
        Process p = null;
        BufferedReader r = null;
        try {
            String[] cmds = {"sh", "-c", command};
            p = Runtime.getRuntime().exec(cmds);
            p.waitFor();
            r = new BufferedReader(new InputStreamReader(p.getInputStream(), Charset.forName("GBK")));
            StringBuffer buffer = new StringBuffer();
            String line;
            while ((line = r.readLine()) != null) {

                if (r.readLine().contains("Connect")) {
                    while ((line = r.readLine()) != null) {
                        buffer.append(line + "\n");
                    }
                }
            }
            result = buffer.toString();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (p != null) {
                    p.destroy();
                }
                if (r != null) {
                    r.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }


    public static String getOsName() {
        return osName;
    }

    public static void setOsName(String osName) {
        CommandUtil.osName = osName;
    }
}
