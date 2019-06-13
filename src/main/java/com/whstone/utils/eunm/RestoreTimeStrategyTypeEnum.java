package com.whstone.utils.eunm;

/**
 * @Author: powerchen
 * @Date: 2019/4/15 9:54
 */
public enum RestoreTimeStrategyTypeEnum {
    ONCE_BACKUP(1, "单次"),
    WEEK_BACKUP(2, "周"),
    MONTH_BACKUP(3, "月");

    public Integer code;
    public String name;

    RestoreTimeStrategyTypeEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    public static RestoreTimeStrategyTypeEnum getByCode(int id) {
        for (RestoreTimeStrategyTypeEnum type : values()) {
            if (type.code == id)
                return type;
        }
        return null;
    }
}
