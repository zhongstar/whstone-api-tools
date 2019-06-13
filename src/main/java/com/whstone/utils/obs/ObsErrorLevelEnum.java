package com.whstone.utils.obs;

/**
 * @Author: powerchen
 * @Date: 2019/6/10 16:08
 */
public enum ObsErrorLevelEnum {

    UNKNOWN_ERROR(-1, "其他未知错误"),
    SUCCESS(0, "执行成功"),
    FILE_DOES_NOT_EXIST(1, "文件不存在"),
    TASK_DOES_NOT_EXIST(2, "任务不存在"),
    PARAMETER_ERROR(3, "参数错误"),
    CHECK_BUCKET_STATUS_ERRORS(4, "检查桶状态错误"),
    COMMAND_INIT_ERROR(5, "命令初始化错误"),
    EXECUTION_ERROR(6, "执行错误"),
    OPERATION_NOT_SUPPORTED(7, "操作不支持"),
    BATCH_TASK_EXECUTION_NOT_COMPLETELY_SUCCESSFUL(8, "批量任务执行不完全成功"),
    INTERRUPT_ERROR(9, "中断错误");

    public int code;
    public String msg;

    ObsErrorLevelEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public static ObsErrorLevelEnum selectByCode(int code) {
        for (ObsErrorLevelEnum value : values()) {
            if (value.code == code) {
                return value;
            }
        }
        return null;
    }
}
