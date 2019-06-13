package com.whstone.utils.eunm;

/**
 * @Author: powerchen
 * @Date: 2019/4/15 9:54
 */
public enum BackupTimeStrategyTypeEnum {
    ONCE_BACKUP(0, "单次"),
    MINUTE_INTERVAL_BACKUP(1, "分钟"),
    HOURS_INTERVAL_BACKUP(2, "小时"),
    DAY_BACKUP(3, "天"),
    WEEK_BACKUP(4, "周"),
    MONTH_BACKUP(5, "月"),
    IMMEDIATELY_BACKUP(6, "立即备份");

    public Integer code;
    public String name;

    BackupTimeStrategyTypeEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    public static BackupTimeStrategyTypeEnum getByCode(int id) {
        for (BackupTimeStrategyTypeEnum type : values()) {
            if (type.code == id)
                return type;
        }
        return null;
    }
}
