package com.atguigu2.jdbcutil;
import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.apache.commons.dbcp.BasicDataSourceFactory;
import javax.sql.DataSource;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;
/**
 * @author lihai
 * @date 2020/8/20-8:59
 */
public class JDBCUtils {
    public static Connection getConnection() throws Exception{
        InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream("jdbc.properties");
        Properties pro=new Properties();
        pro.load(is);
        String user = pro.getProperty("user");
        String password = pro.getProperty("password");
        String url = pro.getProperty("url");
        String driverClass = pro.getProperty("driverClass");
        Class.forName(driverClass);
        Connection conn = DriverManager.getConnection(url, user, password);
        return conn;
    }
    //使用C3P0数据库连接池！！！！(使用XML配置文件的方式)
    //数据库连接池只需要提供一个就可以了！
    private static ComboPooledDataSource cpds = new ComboPooledDataSource("helloc3p0");
    public static Connection getConnection1() throws Exception{
        Connection conn = cpds.getConnection();
        return conn;
    }
    //使用DBCP数据库连接池！！！
    private static DataSource  source;
    static{
        try {
            Properties pro = new Properties();
            InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream("DBCP.properties");
            pro.load(is);
            source = BasicDataSourceFactory.createDataSource(pro);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public  static Connection getConnection2() throws Exception {
        Connection conn = source.getConnection();
        return conn;
    }
    //使用德鲁伊数据库连接池！
    private static DataSource source1;
    static{
        try {
            Properties pro = new Properties();
            InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream("Druid.properties");
            pro.load(is);
            source1 = DruidDataSourceFactory.createDataSource(pro);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static Connection getConnection3() throws SQLException {
        Connection conn = source1.getConnection();
        return conn;
    }
    public static void closeResource(Connection conn, Statement ps){
        try {
            if (ps != null)
                ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if(conn!=null)
            conn.close();
        } catch (SQLException e) {
           e.printStackTrace();
        }
    }
    public static void closeResource(Connection conn, Statement ps,ResultSet resultSet){
        try {
            if (ps != null)
                ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if(conn!=null)
                conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }
}
