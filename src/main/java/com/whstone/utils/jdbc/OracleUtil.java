package com.whstone.utils.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

public class OracleUtil {

    public static String getOracleVersionInfo(DBUtil manager) {

        try {
            //ResultSet res = manager.query("select * from v$version");
            ResultSet res = manager.query("select COMPATIBLE from IMP9COMPAT");
            while (res.next()) {
                /*String serverVerison = res.getString("BANNER");
                String[] resultArr = serverVerison.split(" ");
                return resultArr[6];*/

                return res.getString("COMPATIBLE").trim();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "";
        }
        return "";

    }

}
