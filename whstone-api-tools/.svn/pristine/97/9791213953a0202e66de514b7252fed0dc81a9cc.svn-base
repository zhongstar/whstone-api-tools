package com.whstone.utils.obs;

import com.obs.services.ObsConfiguration;

/**
 * Created by zhongkf on 2019/05/28
 */
public class ObsConfig {
    public static final long partSize = 10 * 1024 * 1024L; //每段上传的大小



    public static ObsConfiguration getConfig(String endPoint) {

        ObsConfiguration config = new ObsConfiguration();
        config.setSocketTimeout(30000);
        config.setConnectionTimeout(10000);
        config.setEndPoint(endPoint);

        return config;

    }
}