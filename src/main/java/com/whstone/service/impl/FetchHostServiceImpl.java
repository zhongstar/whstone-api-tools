package com.whstone.service.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.whstone.model.DTO.HostInfoDTO;
import com.whstone.service.FetchHostService;
import com.whstone.utils.cmd.CommandUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @Author: powerchen
 * @Date: 2019/8/27 11:17
 */
@Service
public class FetchHostServiceImpl implements FetchHostService {
    private Logger logger = LoggerFactory.getLogger(FetchHostServiceImpl.class);

    @Override
    public HostInfoDTO getHostInfo() {
        String hostName = null;
        try {
            InetAddress localHost = InetAddress.getLocalHost();
            hostName = localHost.getHostName();
        } catch (UnknownHostException e) {
            logger.error("get localhost failed:{}", e.getMessage());
        }
        HostInfoDTO hostInfoDTO = new HostInfoDTO();
        hostInfoDTO.setHostname(hostName);
        return hostInfoDTO;
    }

    @Override
    public int updateHostInfo(HostInfoDTO hostInfo) {
        int result = 0;
        String newHostname = hostInfo.getHostname();
        if (StrUtil.isNotEmpty(newHostname)) {
            result = updateHostname(newHostname);
        }
        return result;
    }

    @Override
    @Async
    public int restartHost() {
        int result = 1;
        if (FileUtil.isWindows()) {
            CommandUtil.execWinCommand("shutdown.exe -r -t 5 ");
        } else {

        }
        return result;
    }

    private int updateHostname(String newHostname) {
        if (FileUtil.isWindows()) {
            String updateHostname = String.format("wmic computersystem where \"name='%%ComputerName%%'\" call rename \"%s\"", newHostname);
            String result = CommandUtil.execWinCommand(updateHostname);
            if (result.contains("ReturnValue = 0;")) {
                return 1;
            }
        } else {

        }
        return 0;
    }
}
