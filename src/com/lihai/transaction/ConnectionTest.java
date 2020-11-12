package com.lihai.transaction;

import com.atguigu2.jdbcutil.JDBCUtils;
import org.junit.Test;

import java.sql.Connection;

/**
 * @author lihai
 * @date 2020/8/21-21:41
 */
public class ConnectionTest {
    @Test
    public void testGetConnection() throws Exception{
        Connection conn = JDBCUtils.getConnection();
        System.out.println(conn);
    }
}
