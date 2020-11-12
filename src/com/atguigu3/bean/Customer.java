package com.atguigu3.bean;

import java.sql.Date;

/**
 * @author lihai
 * @date 2020/8/20-17:01
 */
public class Customer {

    private int id;
    private String name;
    private String email;
    private Date birth;
    public Customer() {
        super();
    }
    public Customer(int id, String name, String email, Date birth) {
        super();
        this.id = id;
        this.name = name;
        this.email = email;
        this.birth = birth;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public Date getBirth() {
        return birth;
    }
    public void setBirth(Date birth) {
        this.birth = birth;
    }
    @Override
    public String toString() {
        return "Customer [id=" + id + ", name=" + name + ", email=" + email + ", birth=" + birth + "]";
    }



}
