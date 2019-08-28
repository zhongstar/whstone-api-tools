package com.whstone.controller;

import com.whstone.model.DTO.HostInfoDTO;
import com.whstone.model.domain.ResponseResult;
import com.whstone.service.FetchHostService;
import com.whstone.utils.http.RestResultGenerator;
import com.whstone.utils.http.response.ResponseStatusEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Describe: 提供关于主机信息操作的接口
 * @Author: powerchen
 * @Date: 2019/8/27 11:03
 */
@RestController
@RequestMapping("/api/provider/host")
public class FetchHostController {
    private Logger logger = LoggerFactory.getLogger(FetchHostController.class);

    @Autowired
    private FetchHostService fetchHostService;

    /**
     * 获取主机的相关信息
     *
     * @return
     */
    @GetMapping()
    public ResponseResult getHostInfo() {
        logger.info("开始获取主机信息");
        HostInfoDTO hostInfo = fetchHostService.getHostInfo();
        return RestResultGenerator.genResult(hostInfo, ResponseStatusEnum.QUERY_SUCCESS);
    }

    /**
     * 修改主机信息
     *
     * @param hostInfo
     * @return
     */
    @PutMapping()
    public ResponseResult updateHostInfo(@RequestBody HostInfoDTO hostInfo) {
        logger.info("开始修改主机信息");
        ResponseResult responseResult;
        int result = fetchHostService.updateHostInfo(hostInfo);
        if (result == 1) {
            responseResult = RestResultGenerator.genResult(result, ResponseStatusEnum.OPERATION_SUCCESS);
        } else {
            responseResult = RestResultGenerator.genResult(result, ResponseStatusEnum.OPERATION_FAIL);
        }
        return responseResult;
    }

    /**
     * 重启设备
     *
     * @return
     */
    @GetMapping("/restart")
    public ResponseResult restartHost() {
        logger.info("开始重启主机");
        int result = fetchHostService.restartHost();
        return RestResultGenerator.genResult(result, ResponseStatusEnum.OPERATION_SUCCESS);
    }
}
