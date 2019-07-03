package com.whstone.utils.obs;

import com.obs.services.ObsConfiguration;

/**
 * Created by zhongkf on 2019/05/28
 */
public class ObsConfig {
    public static final long PART_SIZE = 10 * 1024 * 1024L; //每段上传的大小
    public static final int TASK_NUM = 5; //分段下载时的最大并发数

    public static ObsConfiguration getConfig(String endPoint) {

        ObsConfiguration config = new ObsConfiguration();
        config.setSocketTimeout(30000);
        config.setConnectionTimeout(30000);
        config.setEndPoint(endPoint);

        return config;

    }
}