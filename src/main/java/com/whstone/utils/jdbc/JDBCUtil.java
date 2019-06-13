package com.whstone.utils.jdbc;

import java.sql.*;

/**
 * Created by zhongkf on 2018/5/24
 */
public class JDBCUtil {

    private String url;
    private String user ;
    private String pwd ;
    private PreparedStatement sta ;
    private ResultSet rs ;
    private Connection conn ;

    public JDBCUtil(String driver, String url, String dbUser, String dbPass) throws SQLException {

        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        this.url = url;
        this.user = dbUser;
        this.pwd = dbPass;

        conn=getConn();
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
        sta=conn.prepareStatement(sql);
        rs=sta.executeQuery();
        return rs;
    }

    /**
     * @param sql sql语句
     * @return sql true如果第一个结果是一个ResultSet对象; false如果第一个结果是更新计数或没有结果
     */
    public boolean exec(String sql) throws SQLException {
        sta=conn.prepareStatement(sql);
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

    public static void main(String[] args) {
        try {
            JDBCUtil jdbcUtil = new JDBCUtil(
                    "net.sourceforge.jtds.jdbc.Driver",
                    "jdbc:jtds:sybase://145.170.23.123:5000/test",
                    "sa",
                    "123456"
            );

            ResultSet set = jdbcUtil.query("select name from persion where id = 10");
            while (set.next()) {
                System.out.println(set.getString("name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
