package com.atguigu4.blob;

import com.atguigu2.jdbcutil.JDBCUtils;
import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;

/**
 * @author lihai
 * @date 2020/8/21-17:15
 */
/*
 * 使用PreparedStatement实现批量数据的操作
 *
 * update、delete本身就具有批量操作的效果。
 * 此时的批量操作，主要指的是批量插入。使用PreparedStatement如何实现更高效的批量插入？
 *
 * 题目：向goods表中插入20000条数据
 * CREATE TABLE goods(
	id INT PRIMARY KEY AUTO_INCREMENT,
	NAME VARCHAR(25)
   );
 * 方式一：使用Statement
 * Connection conn = JDBCUtils.getConnection();
 * Statement st = conn.createStatement();
 * for(int i = 1;i <= 20000;i++){
 * 		String sql = "insert into goods(name)values('name_" + i + "')";
 * 		st.execute(sql);
 * }
 *
 */
public class InsertTest {
    @Test
    public void testInsert1() throws Exception {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            long start=System.currentTimeMillis();
            conn = JDBCUtils.getConnection();
            String sql="insert into goods(name)values (?)";
            ps = conn.prepareStatement(sql);
            for(int i=1;i<20000;i++){
                ps.setObject(1,"name_"+i);
                ps.execute();
            }
            long end=System.currentTimeMillis();
            System.out.println("花费的时间为："+(end-start));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn,ps);
        }
    }
    @Test
    public void testInsert2() {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            //设置不允许自动提交数据
            long start = System.currentTimeMillis();
            conn = JDBCUtils.getConnection();
            conn.setAutoCommit(false);
            String sql = "insert into goods(name)values(?)";
            ps = conn.prepareStatement(sql);
            for(int i = 1;i <= 1000000;i++){
                ps.setObject(1, "name_" + i);
                //1."攒"sql
                ps.addBatch();
                if(i % 5000 == 0){
                    //2.执行batch
                    ps.executeBatch();
                    //3.清空batch
                    ps.clearBatch();
                }
            }
            conn.commit();
            long end = System.currentTimeMillis();
            System.out.println("花费的时间为：" + (end - start));//20000:83065 -- 565
        } catch (Exception e) {								//1000000:16086
            e.printStackTrace();
        }finally{
            JDBCUtils.closeResource(conn, ps);
        }
    }
}
