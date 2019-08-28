package com.whstone.service;

import com.whstone.model.DTO.HostInfoDTO;

/**
 * @Author: powerchen
 * @Date: 2019/8/27 11:16
 */
public interface FetchHostService {
    /**
     * 获取当前主机的相关信息
     * @return
     */
    HostInfoDTO getHostInfo();

    /**
     * 修改主机信息
     * @param hostInfo
     * @return
     */
    int updateHostInfo(HostInfoDTO hostInfo);

    /**
     * 重启当前主机
     * windows默认5秒后关机
     * @return
     */
    int restartHost();
}
