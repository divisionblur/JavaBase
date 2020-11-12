package com.atguigu3.preparedstatment;
import com.atguigu2.jdbcutil.JDBCUtils;
import com.atguigu3.bean.Customer;
import org.junit.Test;
import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author lihai
 * @date 2020/8/21-9:56
 */

/**
 * 使用PreparedStatement实现针对于不同表的通用的查询操作
 */
public class PreparedStatementQueryTest {
    @Test
    public void testGetForList(){
        Connection conn = null;
        try {
            String sql = "select id,name,email from customers where id < ?";
            conn = JDBCUtils.getConnection3();
            List<Customer> list = getForList(conn, Customer.class, sql, 12);
            for (Customer customer : list) {
                System.out.println(customer);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn, null);
        }
    }
    //泛型方法，声明在方法上的泛型！(考虑上事务)
    public <T> List<T> getForList(Connection conn,Class<T> clazz, String sql, Object... args){

        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            //预编译SQL语句
            ps = conn.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1, args[i]);
            }
            rs = ps.executeQuery();
            // 获取结果集的元数据 :ResultSetMetaData
            ResultSetMetaData rsmd = rs.getMetaData();
            // 通过ResultSetMetaData获取结果集中的列数
            int columnCount = rsmd.getColumnCount();
            //创建集合对象
            ArrayList<T> list = new ArrayList<T>();
            while (rs.next()) {
                T t = clazz.newInstance();
                // 处理结果集一行数据中的每一个列:给t对象指定的属性赋值
                for (int i = 0; i < columnCount; i++) {
                    // 获取列值
                    Object columnValue = rs.getObject(i + 1);

                    // 获取每个列的列名
                    // String columnName = rsmd.getColumnName(i + 1);
                    String columnLabel = rsmd.getColumnLabel(i + 1);

                    // 给t对象指定的columnName属性，赋值为columValue：通过反射
                    Field field = clazz.getDeclaredField(columnLabel);
                    field.setAccessible(true);
                    field.set(t, columnValue);
                }
                list.add(t);
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn, ps, rs);
        }
        return null;
    }





    /*
    针对于不同的表的通用的查询操作，返回表中的一条记录
     */
    @Test
    public void testGetInstance() throws SQLException {
        String sql= "select id,name,email from customers where id = ?";
        Connection conn = JDBCUtils.getConnection3();
        Customer customer = getInstance(conn,Customer.class, sql, 12);
        System.out.println(customer);
    }


    public <T> T getInstance(Connection conn,Class<T> clazz,String sql,Object ...args) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conn.prepareStatement(sql);
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
            JDBCUtils.closeResource(conn,ps,rs);
        }
        return null;
    }
}
