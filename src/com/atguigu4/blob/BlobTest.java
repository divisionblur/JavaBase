package com.atguigu4.blob;

import com.atguigu2.jdbcutil.JDBCUtils;
import com.atguigu3.bean.Customer;
import com.atguigu3.bean.Student;
import org.junit.Test;

import java.io.*;
import java.sql.*;

/**
 * @author lihai
 * @date 2020/8/21-15:42
 */
public class BlobTest {
    //向表中插入Blob类型的数据
    @Test
    public void testInsert()  {
        Connection conn = null;
        try {
            conn = JDBCUtils.getConnection3();
            String sql = "insert into customers(name,email,birth,photo)values(?,?,?,?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setObject(1, "李海");
            ps.setObject(2, "1842757284@qq.com");
            ps.setObject(3, "1998-07-05");
            FileInputStream fis = new FileInputStream(new File("屏幕截图(2).png"));
            ps.setBlob(4, fis);//索引加上流!!!
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn, null);
        }
    }
    //查询数据表customers中Blob类型的字段
    @Test
    public void testQuery()  {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        InputStream is = null;
        FileOutputStream fos= null;
        try {
            conn = JDBCUtils.getConnection();
            String sql = "select id,name,email,birth,photo from customers where id = ?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, 16);
            rs = ps.executeQuery();
            int id = 0;
            String name = null;
            String email = null;
            Date birth = null;
            if (rs.next()) {
                id = rs.getInt(1);
                name = rs.getString(2);
                email = rs.getString(3);
                birth = rs.getDate(4);
            }
            Customer customer = new Customer(id, name, email, birth);
            System.out.println(customer);
            Blob photo = rs.getBlob(5);
            is = photo.getBinaryStream();
            fos = new FileOutputStream(new File("紫霞仙子.png"));
            byte[] buffer=new byte[1024];
            int len=0;
            while((len=is.read(buffer))!=-1){
                fos.write(buffer,0,len);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            JDBCUtils.closeResource(conn,ps,rs);
        }
    }
}
