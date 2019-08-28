package com.whstone.utils.http.response;

public enum ResponseStatusEnum {

    LOGIN_SUCCESS("登陆成功", 200),
    LOGINOUT_SUCCESS("登出成功", 200),
    QUERY_SUCCESS("查询成功", 200),
    OPERATION_SUCCESS("操作成功", 200),
    QUERY_SUCCESS_NODATA("查无数据", 200),
    CREATE_SUCCESS("创建成功", 200),
    UPDATE_SUCCESS("修改成功", 200),
    BINDING_SUCCESS("绑定成功", 201),
    BINDING_FAIL("绑定失败", 201),
    DELETE_SUCCESS("删除成功", 200),


    OPERATION_FAIL("操作失败", 400);


    public String name;
    public Integer code;

    ResponseStatusEnum(String name, Integer code) {
        this.name = name;
        this.code = code;
    }
}
