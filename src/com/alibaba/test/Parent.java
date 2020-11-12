package com.alibaba.test;

/**
 * @author lihai
 * @date 2020/9/2-15:14
 */
 class SubClass extends Parent {
    /**
     * 静态代码块是在类加载时自动执行的，非静态代码块是在创建对象时自动执行的代码，
     * 不创建对象不执行该类的非静态代码块。且执行顺序为:
     * 静态代码块---非静态代码块---构造函数。
     *
     * 静态代码块是自动执行的;  静态方法是被调用的时候才执行的.静态代码块
     * 可用来初始化一些项目最常用的变量或对象;
     * 静态方法可用作不创建对象也可能需要执行的代码
     */

    // 静态变量
    public static String s_StaticField = "子类--静态变量";
    // 变量
    public String s_Field = "子类--变量";
    // 静态初始化块
    static {
        System.out.println(s_StaticField);
        System.out.println("子类--静态初始化块");
    }
    // 初始化块
    {
        System.out.println(s_Field);
        System.out.println("子类--初始化块");
    }
    // 构造器
    public SubClass() {
        super();//调用父类的无参构造
        System.out.println("子类--构造器");
    }
    // 程序入口
    public static void main(String[] args) {
        new SubClass();
    }
}
class Parent {
    // 静态变量
    public static String p_StaticField = "父类--静态变量";
    // 变量
    public String p_Field = "父类--变量";
    // 静态初始化块
    static {
        System.out.println(p_StaticField);
        System.out.println("父类--静态初始化块");
    }
    // 初始化块
    {
        System.out.println(p_Field);
        System.out.println("父类--初始化块");
    }
    // 构造器
    public Parent() {
        System.out.println("父类--构造器");
    }
}

