package com.lihai.transaction;

import com.atguigu2.jdbcutil.JDBCUtils;
import com.atguigu3.bean.User;
import org.apache.commons.dbutils.QueryRunner;
import org.junit.Test;

import java.lang.reflect.Field;
import java.sql.*;

/**
 * @author lihai
 * @date 2020/8/22-9:26
 */
public class TransactionTest {
    @Test
    public void testUpdateWithTransaction() {
        Connection conn = null;
        try {
            QueryRunner runner = new QueryRunner();
            conn = JDBCUtils.getConnection3();
            conn.setAutoCommit(false);
            String sql="update user_table set balance=balance-200 where user=?";
            runner.update(conn,sql,"AA");
            String sql1="update user_table set balance=balance+200 where user=?";
            runner.update(conn,sql1,"BB");
            conn.commit();
            System.out.println("转账成功");
        } catch (SQLException throwables) {
            try {
                conn.rollback();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            throwables.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn,null);
        }
    }
    public int update(Connection conn,String sql,Object...args) {
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement(sql);
            for(int i=0;i<args.length;i++){
                ps.setObject(i+1,args[i]);
            }
            return  ps.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            JDBCUtils.closeResource(null,ps);
        }
        return 0;// 如果出现异常的话  就会执行到这里 方法需要返回值那么可以返回0
    }


    //*****************************************************
    @Test
    public void testTransactionSelect() throws Exception{
        Connection conn = JDBCUtils.getConnection3();
        //获取当前连接的隔离级别:
        System.out.println(conn.getTransactionIsolation());
        //设置数据库的隔离级别：
        conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);//读已经提交的数据能够解决脏读问题，脏读是不允许的！
        //取消自动提交数据:
        conn.setAutoCommit(false);
        String sql = "select user,password,balance from user_table where user = ?";
        User user = getInstance(conn, User.class, sql, "CC");
        System.out.println(user);
    }

    @Test
    public void testTransactionUpdate() {
        Connection conn = null;
        try {
            conn = JDBCUtils.getConnection();
            conn.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
            System.out.println(conn.getTransactionIsolation());
            //取消自动提交数据
            conn.setAutoCommit(false);
            String sql = "update user_table set balance = ? where user = ?";
            update(conn, sql, 5000, "CC");
            Thread.sleep(8000);
            System.out.println("修改结束");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn, null);
        }
    }

    //通用的查询操作，用于返回数据表中的一条记录（version 2.0：考虑上事务）
    public <T> T getInstance(Connection conn,Class<T> clazz,String sql,Object ...args) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conn.prepareStatement(sql);
            //填充占位符
            for(int i=0;i<args.length;i++){
                ps.setObject(i+1,args[i]);
            }
            rs = ps.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnCount = rsmd.getColumnCount();
            if(rs.next()){
                T t = clazz.newInstance();
                for(int i=0;i<columnCount;i++){
                    Object columnValue = rs.getObject(i + 1);
                    String columnLabel = rsmd.getColumnLabel(i + 1);
                    Field field = clazz.getDeclaredField(columnLabel);
                    field.setAccessible(true);
                    field.set(t,columnValue);
                }
                return t;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(null,ps,rs);
        }
        return null;
    }
}
