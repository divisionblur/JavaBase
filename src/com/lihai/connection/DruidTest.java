package com.lihai.connection;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidDataSourceFactory;
import org.junit.Test;

import javax.sql.DataSource;
import java.io.InputStream;
import java.sql.Connection;
import java.util.Properties;

/**
 * @author lihai
 * @date 2020/8/23-14:32
 */
public class DruidTest {
    @Test
    public void getConnection() throws Exception {
        Properties pro = new Properties();
        InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream("Druid.properties");
        pro.load(is);
        DataSource source = DruidDataSourceFactory.createDataSource(pro);
        Connection conn = source.getConnection();
        System.out.println(conn);
    }
}
