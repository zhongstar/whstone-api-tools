package com.whstone.utils.http;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.whstone.model.domain.ResponseResult;
import com.whstone.utils.http.response.ResponseStatusEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 生成Rest风格的结果
 */
public class RestResultGenerator {
    private static final Logger logger = LoggerFactory.getLogger(RestResultGenerator.class);

    public static <T> ResponseResult<T> genResult(T data, ResponseStatusEnum statusEnum) {
        ResponseResult<T> result = new ResponseResult<>();
        result.setInfo(statusEnum).setData(data);
        logger.debug("responseResult : {}", JSON.toJSONString(result, SerializerFeature.DisableCircularReferenceDetect));
        return result;
    }
}
