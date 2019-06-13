package com.whstone.utils.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class JdbcUtils {


    @SuppressWarnings("unused")
    private void excute(String sql, String Dbtype) {


        try {
            //载入驱动程序
            if (Dbtype.equals("oracle")) {
                Class.forName("oracle.jdbc.OracleDriver").newInstance();
            }
            if (Dbtype.equals("mysql")) {
                Class.forName("com.mysql.jdbc.Driver").newInstance();
            }
            if (Dbtype.equals("sqlserver")) {
                Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver").newInstance();
            }

        } catch (InstantiationException ex) {
            ex.printStackTrace();
            System.out.println("载入" + Dbtype + "数据库驱动时出错");

        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
            System.out.println("载入" + Dbtype + "数据库驱动时出错");
        } catch (IllegalAccessException ex) {
            ex.printStackTrace();
            System.out.println("载入" + Dbtype + "数据库驱动时出错");
        }
        Connection conn = null;
        try {

            //连接数据库
            if (Dbtype.equals("oracle")) {
                //连接Oracle数据库
                conn = DriverManager.getConnection("jdbc:oracle:thin:@127.0.0.1:1521:orcl", "username", "password");

            }
            if (Dbtype.equals("mysql")) {
                //连接Mysql库
                conn = DriverManager.getConnection("jdbc:mysql://localhost/cookbook", "root", "");

            }
            if (Dbtype.equals("sqlserver")) {
                //连接sqlserver库
                conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=militaryProduct", "sa", "sa2008");
            }


        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("连接到" + Dbtype + "数据库时出错！");
            System.exit(0);
        }
        ////////////////////////////////////////////////////////////////////////


        try {
            System.out.println("-----------------  " + sql);
            PreparedStatement stat = conn.prepareStatement(sql);

            boolean rs = stat.execute();
            System.out.println(rs);
        } catch (Exception ex) {
            ex.printStackTrace();
            System.exit(0);
        }

        //关半程序所占用的资源
        try {
            conn.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("关闭程序所占用的资源时出错");
            System.exit(0);
        }
    }

    public static void main(String[] args) {
        JdbcUtils jdbcService = new JdbcUtils();
        String sql = "select * from userInfo";
        String Dbtype = "sqlserver";
        jdbcService.excute(sql, Dbtype);
    }
}
