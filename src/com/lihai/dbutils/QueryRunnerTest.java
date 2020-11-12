package com.lihai.dbutils;
import com.atguigu2.jdbcutil.JDBCUtils;
import com.atguigu3.bean.Customer;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.*;
import org.junit.Test;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author lihai
 * @date 2020/8/23-15:21
 */

/**
 *commons-dbutils 是 Apache 组织提供的一个开源 JDBC工具类库,封装了针对于数据库的增删改查操作
 */
public class QueryRunnerTest {
    //测试插入
    @Test
    public void testInsert() {
        Connection conn = null;
        try {
            QueryRunner runner=new QueryRunner();
            conn = JDBCUtils.getConnection3();
//            String sql = "insert into customers(name,email,birth)values(?,?,?)";
            String sql="update customers set name=' 厉害呀', email='1318359006025@qq.com' where id=?;";
            runner.update(conn,sql,22);

//            System.out.println("添加了" + insertCount + "条记录");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn,null);
        }
    }
    //测试查询
    /*
     * BeanHandler:是ResultSetHandler(结果集处理器)接口的实现类，用于封装表中的一条记录。
     */
    @Test
    public void testQuery1(){
        Connection conn = null;
        try {
            QueryRunner runner=new QueryRunner();
            conn = JDBCUtils.getConnection3();
            String sql="select id,name,birth,email from customers where id=?";
            BeanHandler<Customer> handler = new BeanHandler<>(Customer.class);
            Customer customer = runner.query(conn, sql, handler, 2);
            System.out.println(customer);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn,null);
        }
    }


    /*
     * BeanListHandler:是ResultSetHandler接口的实现类，用于封装表中的多条记录构成的集合。
     */
    @Test
    public void testQuery2() {
        Connection conn = null;
        try {
            conn = JDBCUtils.getConnection3();
            String sql="select id,name,birth,email from customers where id<?";
            QueryRunner runner = new QueryRunner();
            BeanListHandler<Customer> handler = new BeanListHandler<>(Customer.class);
            List<Customer> list = runner.query(conn, sql, handler, 23);
            for(Customer customer:list){
                System.out.println(customer);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn,null);
        }
    }
    
    
    
    /*
     * MapHandler:是ResultSetHandler接口的实现类，对应表中的一条记录。
     * 将字段及相应字段的值作为map中的key和value
     */
    @Test
    public void testQuery3()  {
        Connection conn = null;
        try {
            conn = JDBCUtils.getConnection3();
            QueryRunner runner = new QueryRunner();
            String sql="select id,name,email,birth from customers where id=?";
            MapHandler handler = new MapHandler();
            Map<String, Object> map = runner.query(conn, sql, handler, 22);
            System.out.println(map);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn,null);
        }
    }

    /*
     * MapListHandler:是ResultSetHandler接口的实现类，对应表中的多条记录。
     * 将字段及相应字段的值作为map中的key和value。将这些map添加到List中
     */
    @Test
    public void testQuery4()  {
        Connection conn= null;
        try {
            conn = JDBCUtils.getConnection3();
            String sql="select * from customers";
            QueryRunner runner = new QueryRunner();
            MapListHandler handler = new MapListHandler();
            List<Map<String, Object>> list = runner.query(conn, sql, handler);
            list.forEach(System.out::println);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn,null);
        }
    }




    /*
     * ScalarHandler:用于查询特殊值
     */
    @Test
    public void testQuery5()  {
        Connection conn = null;
        try {
            conn = JDBCUtils.getConnection3();
            String sql="select count(*) from customers";
            QueryRunner runner = new QueryRunner();
            ScalarHandler handler = new ScalarHandler();
            Object query = runner.query(conn, sql, handler);
            System.out.println(query);
        } catch (SQLException throwables){
            throwables.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn,null);
        }
    }
    @Test
    public void testQuery6() {
        Connection conn = null;
        try {
            conn = JDBCUtils.getConnection3();
            String sql="select max(birth) from customers";
            QueryRunner runner = new QueryRunner();
            ScalarHandler handler = new ScalarHandler();
            Date maxBirth = (Date) runner.query(conn, sql, handler);
            System.out.println(maxBirth);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn,null);
        }
    }

    /*
     * 自定义ResultSetHandler的实现类
     */

    /**
     * ResultSetHandler是个接口直接new  以匿名内部类的方式  然后重写handle()方法！！！
     * 不同的实现类重写不同的handle()方法，返回构造不一样的结果集。
     * @throws SQLException
     */

    @Test
    public void testQuery() throws SQLException {
        Connection conn = null;
        try {
            conn = JDBCUtils.getConnection3();
            String sql = "select id,name,birth,email from customers where id=?";
            QueryRunner runner = new QueryRunner();
            ResultSetHandler<Customer> handler = new ResultSetHandler<Customer>() {
                @Override
                public Customer handle(ResultSet rs) throws SQLException {
                    int id = 0;
                    String name = null;
                    String email = null;
                    java.sql.Date birth = null;
                    if (rs.next()) {
                        id = rs.getInt(1);
                        name = rs.getString(2);
                        birth = rs.getDate(3);
                        email = rs.getString(4);
                        Customer customer = new Customer(id, name, email, birth);
                        return customer;
                    }
                    return null;
                }
            };
            Customer customer = runner.query(conn, sql, handler, 22);
            System.out.println(customer);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn, null);
        }
    }
}




















