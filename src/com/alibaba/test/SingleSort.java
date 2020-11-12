package com.alibaba.test;

/**
 * @author lihai
 * @date 2020/9/2-14:57
 *
 *
     * 1:j    i=0    n=0
     * 2:构造块    i=1    n=1
     * 3:t1    i=2    n=2
     * 4:j    i=3    n=3
     * 5:构造块    i=4    n=4
     * 6:t2    i=5    n=5
     * 7:i    i=6    n=6
     * 8:静态块    i=7    n=99
     * 9:j    i=8    n=100
     * 10:构造块    i=9    n=101
     * 11:init    i=10    n=102
 */
public class SingleSort {
    public static int k = 0;
    public static SingleSort t1 = new SingleSort("t1");//类加载时先执行这一行
    public static SingleSort t2 = new SingleSort("t2");
    public static int i = print("i");
    public static int n = 99;   //这里执行完了应该去执行静态代码块！
    public int j = print("j");
    {
        //实例语句块里调用静态方法！
        print("构造块");
    }

    static{
        print("静态块");
    }

    public SingleSort(String str){
//        System.out.print("Test1: ");
        System.out.println((++k)+":"+str+"    i="+i+"    n="+n);
        ++i;
        ++n;
    }

    public static int print(String str){
//        System.out.print("print: ");
        System.out.println((++k)+":"+str+"    i="+i+"    n="+n);
        ++n;
        return ++i;
    }

    public static void main(String[] args) {
        SingleSort singleSort = new SingleSort("init");
    }
}

