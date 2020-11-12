package com.lihai.dao;

import com.atguigu2.jdbcutil.JDBCUtils;
import com.mysql.jdbc.Connection;

import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author lihai
 * @date 2020/8/23-8:32
 */

/*
 * DAO: data(base) access object
 * 封装了针对于数据表的通用的操作
 */
public class BaseDAO{
    //通用的增删改操作---version 2.0 （考虑上事务）
    public int update(Connection conn,String sql,Object ...args){
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement(sql);
            for(int i=0;i<args.length;i++){
                ps.setObject(i+1,args[i]);
            }
            return ps.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            JDBCUtils.closeResource(null,ps);
        }
        return 0;
    }
    // 通用的查询操作，用于返回数据表中的一条记录（version 2.0：考虑上事务）
    public <T>  T getInstance(Connection conn,Class<T> clazz,String sql,Object ...args)  {
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
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(null,ps,rs);
        }
        return null;
    }

    // 通用的查询操作，用于返回数据表中的多条记录构成的集合（version 2.0：考虑上事务）
    public <T> List<T> getForList1(Connection conn,Class<T> clazz,String sql,Object...args) {
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
            ArrayList<T> list = new ArrayList<>();
            while(rs.next()){
                T t = clazz.newInstance();
                for(int i=0;i<columnCount;i++){
                    Object columnValue = rs.getObject(i + 1);
                    String columnLabel = rsmd.getColumnLabel(i + 1);
                    Field field = clazz.getDeclaredField(columnLabel);//表中数据的名字对应  类中同名的属性名！
                    field.setAccessible(true);
                    field.set(t,columnValue);
                }
                list.add(t);
            }
            return list;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(null,ps,rs);
        }
        return null;
    }

    //用于查询特殊值的通用的方法
    public <E> E getValue(Connection conn,String sql,Object...args){

        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps=conn.prepareStatement(sql);
            for(int i=0;i<args.length;i++){
                ps.setObject(i+1,args[i]);
            }
            rs = ps.executeQuery();
            if(rs.next()){
                return (E) rs.getObject(1);//获取一个结果！
            }
        } catch (SQLException throwables){
            throwables.printStackTrace();
        } finally {
            JDBCUtils.closeResource(null,ps,rs);
        }
        return null;
    }
    public <T>  List<T> getForList(Connection conn,Class<T> clazz,String sql,Object...args) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conn.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1, args[i]);
            }
            rs = ps.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnCount = rsmd.getColumnCount();
            List list = new ArrayList<>();

            while (rs.next()) {
                T t = clazz.newInstance();
                for (int i = 0; i < columnCount; i++) {
                    String columnLabel = rsmd.getColumnLabel(i + 1);
                    Object columnValue = rs.getObject(i + 1);
                    Field field = clazz.getDeclaredField(columnLabel);
                    field.setAccessible(true);
                    field.set(t, columnValue);
                }
                list.add(t);
            }

            return list;

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(null, ps, rs);
        }
        return null;
    }
}























