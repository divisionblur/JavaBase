package com.alibaba.test;

/**
 * @author lihai
 * @date 2020/9/2-19:50
 */
public class StaticTest {
    public static void main(String[] args) {
        staticFunction();

    }
    static StaticTest st=new StaticTest();
    static{
        System.out.println("1");
    }
    {
        System.out.println("2");
    }
    StaticTest(){
        System.out.println("3");
        System.out.println("a="+a+",b="+b);
    }
    public static void staticFunction(){
        System.out.println("4");
    }
    int a=110;
    static int b=112;
}
/**
 * JVM想要执行程序，发现StaticTest类没有加载进来，于是开始加载
 * 类的加载过程 分为准备阶段 为类变量和静态代码块分配空间，另一个阶段是类的初始化阶段
 * 执行类变量的赋值和静态代码块 先执行到
 * static StaticTest st=new StaticTest();   发现要实例化一个对象，当对象建好了赋给引用这行代码才算结束
 *
 * Java对象的初始化过程主要涉及三种执行对象初始化的结构，分别是实例变量初始化，实例代码块初始化以及构造函数初始化。
 * 我们在定义实例变量的同时，还可以直接对实例变量进行赋值或者使用实例代码块对其进行赋值。如果我们以这两种方式为实例变量进行
 * 初始化，那么他们将在执行构造函数之前完成这些初始化操作。
 * 实际上如果我们对实例变量进行直接赋值或者使用实例代码块赋值，那么编译器会将其中的代码放到类的构造函数中去，并且这些代码会
 * 被放在对超类构造函数的调用语句之后。
 *     StaticTest(){
 *
 *         System.out.println("2");
 *
 *         int a=110;
 *         System.out.println("3");
 *         System.out.println("a="+a+",b="+b);
 *     }
 *
 *     会输出2,3，a=110因为此时b还没有被赋值故b=0；
 *
 *     以上 static StaticTest st=new StaticTest(); 这行代码才执行完毕。
 *
 *     接下来执行
 *     static{
 *         System.out.println("1");
 *     }
 *     故输出1；
 *     最后执行：static int b=112;  到这里类加载过程的初始化阶段才算完成
 *
 *
 *     接下来在main方法中调用staticFunction()输出4
 *
 *
 *     故最终的结果是：
 *     2
 *     3
 *     a=110,b=0
 *     1
 *     4
 */
