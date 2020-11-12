package com.lihai.connection;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.dbcp.BasicDataSourceFactory;
import org.junit.Test;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

/**
 * @author lihai
 * @date 2020/8/23-13:55
 */
public class DBCPTest {
    //方式一:不推荐


    @Test
    public void testGetConnection() throws SQLException {
        //创建了一个DBCP数据库连接池
        BasicDataSource source=new BasicDataSource();
        //设置基本信息
        source.setDriverClassName("com.mysql.jdbc.Driver");
        source.setUrl("jdbc:mysql://localhost:3306/test");
        source.setUsername("root");
        source.setPassword("lihai520");

        //还可以设置其他涉及数据库连接池管理的相关属性
        source.setInitialSize(10);
        source.setMaxActive(10);
        Connection conn = source.getConnection();
        System.out.println(conn);

    }
    //方式二:使用配置文件(推荐使用)
    private static DataSource source;
    static{
        try {
            Properties pro = new Properties();
            InputStream is =ClassLoader.getSystemClassLoader().getResourceAsStream("DBCP.properties");
            pro.load(is);
            source = BasicDataSourceFactory.createDataSource(pro);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Test
    public void testGetConnection1() throws Exception {
        Connection conn = source.getConnection();
        System.out.println(conn);
    }
}
