package com.whstone.utils.jdbc;

import java.sql.*;

/**
 * Created by zhongkf on 2018/5/24
 */
public class DBUtil {

    private static String driver = "oracle.jdbc.OracleDriver";

    private String url = "jdbc:oracle:thin:@145.170.23.127:1521:orcl";
    private String user = "sys as sysdba";
    private String pwd = "oracle";
    private PreparedStatement sta = null;
    private ResultSet rs = null;
    private Connection conn = null;

    /**
     * 加载驱动程序
     */
    static {
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public DBUtil() throws SQLException {
        conn = getConn();
    }

    public DBUtil(String url, String dbUser, String dbPass) throws SQLException {
        this.url = url;
        this.user = dbUser;
        this.pwd = dbPass;

        conn = getConn();
    }

    /**
     * @return 连接对象
     */
    public Connection getConn() throws SQLException {
        conn = DriverManager.getConnection(url, user, pwd);
        return conn;
    }

    /**
     * @param sql sql语句
     * @return 数据集合
     */
    public ResultSet query(String sql) throws SQLException {
        sta = conn.prepareStatement(sql);
        rs = sta.executeQuery();
        return rs;
    }

    /**
     * @param sql sql语句
     * @return sql true如果第一个结果是一个ResultSet对象; false如果第一个结果是更新计数或没有结果
     */
    public boolean exec(String sql) throws SQLException {
        sta = conn.prepareStatement(sql);
        return sta.execute();
    }

    /**
     * 关闭资源
     */
    public void close() {
        try {
            if (rs != null) {
                rs.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (sta != null) {
                    sta.close();
                }
            } catch (SQLException e2) {
                e2.printStackTrace();
            } finally {
                if (conn != null) {
                    try {
                        conn.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }


}
