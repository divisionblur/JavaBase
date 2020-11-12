package com.atguigu1.model;

import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.Scanner;
/**
 * @author lihai
 * @date 2020/8/19-17:36
 */
public class user {
    //方式一 这种方法显示出现了第三方数据库的API即 com.mysql.jdbc.Driver
    @Test
    public void connectionTest1() throws SQLException {
        Driver driver=new com.mysql.jdbc.Driver();  //JDBC驱动类名
        String url="jdbc:mysql://localhost:3306/test";
        Properties info=new Properties();
        info.setProperty("user","root");
        info.setProperty("password","lihai520");
        Connection conn = driver.connect(url, info);//这个connect()方法应该是com.mysql.jdbc.Driver重写的java.sql.Driver得方法
        System.out.println(conn);
    }

    //方式二，利用反射实例化Driver,不在代码中体现第三方数据库的API，面向接口编程！
    @Test
    public void connectionTest2() throws Exception {
        String str="com.mysql.jdbc.Driver";
        Class<?> clazz= Class.forName(str);
        Driver driver = (Driver) clazz.newInstance();//调用com.mysql.jdbc.Driver类的无参构造
        String url="jdbc:mysql://localhost:3306/test";
        Properties info=new Properties();
        info.setProperty("user","root");
        info.setProperty("password","lihai520");
        Connection conn = driver.connect(url, info);
        System.out.println(conn);
    }
    //方式三，使用驱动程序管理器类来注册驱动DriverManager.registerDriver(driver)
    @Test
    public void connectionTest3() throws Exception {
        //1.数据库连接的4个基本要素
        String url="jdbc:mysql://localhost:3306/test";
        String user="root";
        String password="lihai520";
        String driverName="com.mysql.jdbc.Driver";
        //2.实例化Driver
        //先获取运行时类Class对象。
        Class<?> clazz = Class.forName(driverName);
        Driver driver=(Driver)clazz.newInstance();
        //3.注册驱动
        DriverManager.registerDriver(driver);
        //4.获取连接
        Connection conn = DriverManager.getConnection(url, user, password);
        System.out.println(conn);
    }
    //方式四，不必显示注册驱动了，在com.mysql.jdbc.Driver中已经存在静态代码块，实现了驱动注册！
    /*
                public Driver() throws SQLException {}
            static {
                try {
                    DriverManager.registerDriver(new Driver());
                } catch (SQLException var1) {
                    throw new RuntimeException("Can't register driver!");
          }
    }
     */
    @Test
    public void connectionTest04() throws Exception {
        String url="jdbc:mysql://localhost:3306/test";
        String user="root";
        String password="lihai520";
        /**
         * 获得驱动类的运行时类对象！当我们new一个新对象时JVM中的类加载器子系统会将对应Class对象加载到JVM中
         * JVM再根据这个类型信息相关的Class对象创建我们需要的实例对象！
         *
         *  DriverManager.registerDriver(new Driver());
         */
        Class.forName("com.mysql.jdbc.Driver");//获得驱动类的运行时类对象！JVM
        Connection conn = DriverManager.getConnection(url, user, password);
        System.out.println(conn);
    }
    //方式五,使用配置文件方式保存配置信息，在代码中加载配置文件
    @Test
    public void connectionTest05() throws Exception {
        //1.加载配置文件
        InputStream is =ClassLoader.getSystemClassLoader().getResourceAsStream("jdbc.properties");
//        InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("jdbc.properties");
        Properties pro=new Properties();
        pro.load(is);
        //2.读取配置信息
        String user = pro.getProperty("user");
        String password = pro.getProperty("password");
        String url = pro.getProperty("url");
        String driverClass = pro.getProperty("driverClass");
        //3.加载驱动
        Class.forName("com.mysql.jdbc.Driver");
        //4.获取连接
        Connection conn = DriverManager.getConnection(url, user, password);
        System.out.println(conn);
    }
}
