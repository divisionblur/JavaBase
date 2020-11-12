package com.atguigu3.preparedstatment;

import com.atguigu2.jdbcutil.JDBCUtils;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

/**
 * @author lihai
 * @date 2020/8/19-21:11
 */
public class PreparedStatementUpdateTest {
    //添加一条记录到customers表(未使用工具类建立连接)
    @Test
    public void testInsert() {
        PreparedStatement ps = null;
        Connection conn = null;
        try {
            InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream("jdbc.properties");
            Properties pro = new Properties();
            pro.load(is);
            String user = pro.getProperty("user");
            String password = pro.getProperty("password");
            String url = pro.getProperty("url");
            String driverClass = pro.getProperty("driverClass");
            Class.forName(driverClass);
            conn = DriverManager.getConnection(url, user, password);
            String sql = "insert into customers(name,email,birth)values(?,?,?)";
            ps = conn.prepareStatement(sql);
            ps.setString(1, "哪吒");
            ps.setString(2, "nazha@gmail.com");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date date = sdf.parse("1000-01-01");
            ps.setDate(3, new java.sql.Date(date.getTime()));
            ps.execute();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (ps != null)
                    ps.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            try {
                if(conn!=null)
                    conn.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }
    //修改customers表的一条记录(使用工具类)
    @Test
    public void testUpdate() throws Exception {
        Connection conn= null;
        PreparedStatement ps = null;
        try {
            conn = JDBCUtils.getConnection();
            String sql="update customers set name=? where id=?";
            ps = conn.prepareStatement(sql);//预编译
            ps.setObject(1,"肖邦");
            ps.setObject(2,18);
            ps.execute();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn,ps);
        }
    }
    //通用的增删改操作(不需要结果集相对于查的操作简单了很多！)也不需要表对象
    @Test
    public void  testCommonDelete(){
        Connection conn = null;
        try {
//        String sql="delete from customers where id = ?";
//        update(sql,3);
            conn = JDBCUtils.getConnection3();
            String sql1 = "update  fuck_order set order_name=? where order_id = ?";
            update(conn, sql1, "李海", 4);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn, null);
        }
    }

    //考虑了事务的通用的增删改
    public void update(Connection conn,String sql,Object...args) {
        PreparedStatement ps = null;
        try {
            conn = JDBCUtils.getConnection();
            ps = conn.prepareStatement(sql);
            for(int i=0;i<args.length;i++){
                ps.setObject(i+1,args[i]);
            }
            ps.execute();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(null,ps);
        }
    }
}