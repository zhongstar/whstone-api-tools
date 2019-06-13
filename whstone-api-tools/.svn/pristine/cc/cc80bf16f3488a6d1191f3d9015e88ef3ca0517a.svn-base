package com.whstone.utils.ip;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.HashMap;
import java.util.Map;

public class GatewayService {

    private static Logger logger = Logger.getLogger(GatewayService.class);

    public static Map getNetMaskAndDefaultGateway2003(String ip) throws IOException {
        Map map = new HashMap();
        String cmd = "ipconfig";
        Process process = Runtime.getRuntime().exec(cmd);
        InputStreamReader isr = new InputStreamReader(process.getInputStream(), "GBK");
        LineNumberReader input = new LineNumberReader(isr);
        String line;
        while ((line = input.readLine()) != null) {
            logger.info(line);
            if (line.contains("Ethernet adapter 本地连接")) {
                String degaultnNetLink = line;
                String netLink = degaultnNetLink.substring(17, degaultnNetLink.length() - 1);
                logger.info("netLink=" + netLink);
                map.put("netLink", netLink);
                continue;
            }
            if (line.contains("Subnet Mask")) {

                String netMask = line.split(":")[1].trim();
                map.put("netMask", netMask);

            }

            if (line.contains("Default Gateway")) {

                String defaultGateway = line.split(":")[1].trim();
                map.put("defaultGateway", defaultGateway);

            }

        }
        return map;
    }

    public static Map getNetMaskAndDefaultGateway(String ip) throws IOException {
        Map map = new HashMap();
        String cmd = "ipconfig";
        Process process = Runtime.getRuntime().exec(cmd);
        InputStreamReader isr = new InputStreamReader(process.getInputStream(), "GBK");
        LineNumberReader input = new LineNumberReader(isr);
        String line;
        while ((line = input.readLine()) != null) {
            if (line.contains("以太网适配器 本地连接")) {
                String degaultnNetLink = line;
                String netLink = degaultnNetLink.substring(7, degaultnNetLink.length() - 1);
                logger.info("netLink=" + netLink);
                map.put("netLink", netLink);
                continue;
            }
            if (line.contains(ip)) {
                String netmaskStr = input.readLine();
                String defaultGatewayStr = input.readLine();
                String netMask = netmaskStr.substring(35, netmaskStr.length());
                String defaultGateway = defaultGatewayStr.substring(35, defaultGatewayStr.length());
                logger.info("netMask=" + netMask);
                logger.info("defaultGateway=" + defaultGateway);
                map.put("netMask", netMask);
                map.put("defaultGateway", defaultGateway);
                break;
            }

        }
        return map;
    }

    public static Map getNetMaskAndDefaultGateway2012(String ip) throws IOException {
        Map map = new HashMap();
        String cmd = "ipconfig";
        Process process = Runtime.getRuntime().exec(cmd);
        InputStreamReader isr = new InputStreamReader(process.getInputStream(), "GBK");
        LineNumberReader input = new LineNumberReader(isr);
        String line;
        while ((line = input.readLine()) != null) {
            logger.info(line);
            if (line.contains("以太网适配器 Ethernet0")) {
                String degaultnNetLink = line;
                String netLink = degaultnNetLink.substring(7, degaultnNetLink.length() - 1);
                logger.info("netLink=" + netLink);
                map.put("netLink", netLink);
                continue;
            }
            if (line.contains(ip)) {
                String netmaskStr = input.readLine();
                String defaultGatewayStr = input.readLine();
                String netMask = netmaskStr.substring(35, netmaskStr.length());
                String defaultGateway = defaultGatewayStr.substring(35, defaultGatewayStr.length());
                logger.info("netMask=" + netMask);
                logger.info("defaultGateway=" + defaultGateway);
                map.put("netMask", netMask);
                map.put("defaultGateway", defaultGateway);
                break;
            }

        }
        return map;
    }

    public static boolean ipIsReachable(String ip) throws IOException {
        String cmd = String.format("ping %s", ip);
        Process process = Runtime.getRuntime().exec(cmd);
        InputStreamReader isr = new InputStreamReader(process.getInputStream(), "GBK");
        LineNumberReader input = new LineNumberReader(isr);
        String line;
        while ((line = input.readLine()) != null) {
            if (line.contains("TTL=")) {
                return true;
            }
        }
        return false;
    }

    public static String getOsVERSION() {
        String osName = System.getProperty("os.name");
        if (osName.contains("2003")) {
            return "2003";
        }
        if (osName.contains("2008")) {
            return "2008";
        }
        if (osName.contains("2012")) {
            return "2012";
        }
        return "";
    }

    public static void main(String[] args) {
        System.out.println(GatewayService.getOsVERSION());
    }
}
