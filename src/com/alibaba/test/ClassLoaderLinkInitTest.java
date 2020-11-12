package com.alibaba.test;

/**
 * @author lihai
 * @date 2020/9/2-18:13
 */

/**输出结果如下：
 * 4Father init block
 * 6Father constructor
 * 1son init block
 * 3son constructor
 * 5Father static block
 * 2son static block
 * 4Father init block
 * 6Father constructor
 * 1son init block
 * 3son constructor
 * ---end---
 */
public class ClassLoaderLinkInitTest {
    public static void main(String[] args) {
        Son son = new Son();//我觉得是程序的运行需要类加载整个过程完成！
        System.out.println("---end---");
    }
}

class Son extends Father{
    private int i=1;
    private long l=2L;
    static int ssi=3;

    {
        System.out.println("1son init block");
    }
    static{
        System.out.println("2son static block");
    }
    Son(){
        l=3L;
        System.out.println("3son constructor");
    }
}

class Father{
    int ii;
    static int fsi=4;
    static Son son=new Son();
    {
        System.out.println("4Father init block");
    }
    static{
        System.out.println("5Father static block");
    }
    Father(){
        ii=1;
        System.out.println("6Father constructor");
    }
}
