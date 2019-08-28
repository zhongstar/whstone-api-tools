package com.whstone.model.domain;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.whstone.utils.http.response.ResponseStatusEnum;


/**
 * Created by chenzheqi on 2017/5/24.
 */
public class ResponseResult<T> {
    private String message;
    private T data;
    @JSONField(serialzeFeatures = SerializerFeature.NotWriteDefaultValue)
    private Integer code;

    public ResponseResult setInfo(ResponseStatusEnum successEnum) {
        this.code = successEnum.code;
        this.message = successEnum.name;
        return this;
    }


    public Integer getCode() {
        return code;
    }

    public ResponseResult<T> setCode(Integer code) {
        this.code = code;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public ResponseResult setMessage(String message) {
        this.message = message;
        return this;
    }

    public T getData() {
        return data;
    }

    public ResponseResult setData(T data) {
        this.data = data;
        return this;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
