package com.atguigu3.preparedstatment;

import com.atguigu2.jdbcutil.JDBCUtils;
import com.atguigu3.bean.Customer;
import org.junit.Test;

import java.lang.reflect.Field;
import java.sql.*;

/**
 * @author lihai
 * @date 2020/8/20-16:54
 */

public class customerForQuery {
    /*
        针对Customer通用的查询操作
     */
    @Test
    public void testQueryForCustomers(){
        String sql = "select name,email from customers where name = ?";
        Customer customer = queryForCustomers(sql, "周杰伦");
        System.out.println(customer);
    }
    public Customer queryForCustomers(String sql,Object...args)  {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = JDBCUtils.getConnection3();
            ps = conn.prepareStatement(sql);
            for(int i=0;i<args.length;i++){
                ps.setObject(i+1,args[i]);//给占位符赋值
            }
            rs = ps.executeQuery();//获得一个结果集
            ResultSetMetaData rsmd = rs.getMetaData();//元数据

            int columnCount = rsmd.getColumnCount();
            if(rs.next()){
                Customer customer=new Customer();
                for(int i=0;i<columnCount;i++){
                    String columnName = rsmd.getColumnName(i + 1);
                    Object columnValue = rs.getObject(i + 1);
                    Field field = Customer.class.getDeclaredField(columnName);
                    field.setAccessible(true);
                    field.set(customer,columnValue);
                }
                    return customer;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn,ps,rs);
        }
        return null;
    }
    /*
        这只是针对Customer查询举例

     */
    @Test
    public void testQuery() {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet resultSet = null;
        try {
            conn = JDBCUtils.getConnection();
            String sql = "select id,name,email,birth from customers where id=?";
            ps = conn.prepareStatement(sql);
            ps.setObject(1, 2);
            resultSet = ps.executeQuery();//获得一个结果集
            int id = 0;
            String name = null;
            String email = null;
            Date birth = null;
            while (resultSet.next()) {
                id = resultSet.getInt(1);
                name = resultSet.getString(2);
                email = resultSet.getString(3);
                birth = resultSet.getDate(4);
            }
            Customer ct = new Customer(id,name,email,birth);
            System.out.println(ct);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn, ps, resultSet);
        }
    }
}
