package com.lihai.dao;

import com.atguigu3.bean.Customer;

import java.sql.Connection;
import java.sql.Date;
import java.util.List;


/**
 * @author lihai
 * @date 2020/8/23-9:14
 */
public class CustomerDAOImpl extends BaseDAO implements CustomerDAO{
    @Override
    public void insert(Connection conn, Customer cust) {
        String sql = "insert into customers(name,email,birth)values(?,?,?)";
        update((com.mysql.jdbc.Connection) conn, sql,cust.getName(),cust.getEmail(),cust.getBirth());
    }

    @Override
    public void deleteById(Connection conn, int id) {
        String sql = "delete from customers where id = ?";
        update((com.mysql.jdbc.Connection)conn, sql, id);

    }

    @Override
    public void update(Connection conn, Customer cust) {
        String sql = "update customers set name = ?,email = ?,birth = ? where id = ?";
        update((com.mysql.jdbc.Connection)conn, sql,cust.getName(),cust.getEmail(),cust.getBirth(),cust.getId());
    }

    @Override
    public Customer getCustomerById(Connection conn, int id) {
        String sql = "select id,name,email,birth from customers where id = ?";
        Customer customer = getInstance((com.mysql.jdbc.Connection)conn,Customer.class, sql,id);
        return customer;
    }

    @Override
    public List<Customer> getAll(Connection conn) {
        String sql = "select id,name,email,birth from customers";//如果没有占位符就不写 占位符所需要的参数
        List<Customer> list = getForList((com.mysql.jdbc.Connection)conn, Customer.class, sql);
        return list;
    }

    @Override
    public Long getCount(Connection conn) {
        String sql = "select count(*) from customers";
        return getValue((com.mysql.jdbc.Connection)conn, sql);
    }

    @Override
    public Date getMaxBirth(Connection conn) {
        String sql = "select max(birth) from customers";
        return getValue((com.mysql.jdbc.Connection)conn, sql);
    }

}

