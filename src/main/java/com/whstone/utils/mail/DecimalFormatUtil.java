package com.whstone.utils.mail;

import java.text.DecimalFormat;

public class DecimalFormatUtil {

    /**
     * 备份大小 转换
     *
     * @param backup_size
     * @return
     */
    public static String getBackupSize(String backup_size) {
        Long backupSize = Long.valueOf(backup_size);
        String backupSizeStr = "";
        DecimalFormat df = new DecimalFormat("0.00");
        if (backupSize >= 1024 * 1024 * 1024) {
            backupSizeStr = String.valueOf(df.format((float) backupSize / 1024 / 1024 / 1024)) + "GB";
        } else if (backupSize >= 1024 * 1024) {
            backupSizeStr = String.valueOf(df.format((float) backupSize / 1024 / 1024)) + "MB";
        } else if (backupSize >= 1024) {
            backupSizeStr = String.valueOf(df.format((float) backupSize / 1024)) + "KB";
        } else {
            backupSizeStr = backup_size + "B";
        }
        return backupSizeStr;
    }

}
