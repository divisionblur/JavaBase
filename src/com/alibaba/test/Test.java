package com.alibaba.test;

import com.atguigu3.bean.Customer;
import sun.misc.Unsafe;

import java.lang.reflect.Field;

/**
 * @author lihai
 * @date 2020/9/2-21:04
 */
public class Test implements  Cloneable{
    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException, InstantiationException, CloneNotSupportedException {

            Customer customer = new Customer();
            System.out.println(customer);

            Field field = Unsafe.class.getDeclaredField("theUnsafe");
            field.setAccessible(true);
            Unsafe unsafe = (Unsafe) field.get(null);
            Customer customer1 = (Customer) unsafe.allocateInstance(Customer.class);
            System.out.println(customer1);


            Test test = new Test();
            Object clone = test.clone();
            System.out.println(clone);


    }



}
/**
 * Customer [id=0, name=null, email=null, birth=null]
 * Customer [id=0, name=null, email=null, birth=null]
 */