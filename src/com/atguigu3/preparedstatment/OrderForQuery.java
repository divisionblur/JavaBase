package com.atguigu3.preparedstatment;

import com.atguigu2.jdbcutil.JDBCUtils;
import com.atguigu3.bean.Order;
import org.junit.Test;

import java.lang.reflect.Field;
import java.sql.*;

/**
 * @author lihai
 * @date 2020/8/20-21:46
 */

/*
 * 针对于表的字段名与类的属性名不相同的情况：
 * 1. 必须声明sql时，使用类的属性名来命名字段的别名
 * 2. 使用ResultSetMetaData时，需要使用getColumnLabel()来替换getColumnName(),
 *    获取列的别名。
 *  说明：如果sql中没有给字段其别名，getColumnLabel()获取的就是列名
 *
 *
 */
public class OrderForQuery {
    @Test
    public void testOrderForQuery() throws Exception {
        String sql="select order_id orderId,order_name orderName,order_date orderDate from fuck_order where order_id=?";
        Order order = orderForQuery(sql, 4);
        System.out.println(order);
    }


    public Order orderForQuery(String sql,Object ...args) throws Exception {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = JDBCUtils.getConnection3();
            ps = conn.prepareStatement(sql);
            for(int i=0;i<args.length;i++){
                ps.setObject(i+1,args[i]);
            }
            rs = ps.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnCount = rsmd.getColumnCount();
            if(rs.next()){
                Order order = new Order();
                for(int i=0;i<columnCount;i++){
                    Object columnValue = rs.getObject(i + 1);
                    String columnLabel = rsmd.getColumnLabel(i + 1);
                    Field field = Order.class.getDeclaredField(columnLabel);
                    field.setAccessible(true);
                    field.set(order,columnValue);
                }
                return order;
            }
        } finally {
            JDBCUtils.closeResource(conn,ps,rs);
        }
        return null;
    }

    @Test
    public void testQuery1() throws Exception {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = JDBCUtils.getConnection();
            String sql = "select order_id,order_name,order_date from fuck_order where order_id=?";
            ps = conn.prepareStatement(sql);
            ps.setObject(1, 1);
            rs = ps.executeQuery();
            Date orderDate = null;
            String orderName = null;
            int orderId = 0;
            if (rs.next()) {
                orderId = rs.getInt(1);
                orderName = rs.getString(2);
                orderDate = rs.getDate(3);
            }
            Order order = new Order(orderId, orderName, orderDate);
            System.out.println(order);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn,ps,rs);
        }
    }
}
