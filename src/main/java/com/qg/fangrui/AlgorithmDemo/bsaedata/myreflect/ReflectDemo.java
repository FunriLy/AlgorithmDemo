package com.qg.fangrui.AlgorithmDemo.bsaedata.myreflect;


/**
 * Created by funrily on 17-8-29
 * Java 反射机制：获取类的三种方法
 * @version 1.0.0
 */
public class ReflectDemo {

    public static void main(String[] args) throws ClassNotFoundException {
        // 通过 .class
        Class class1 = String.class;
        System.out.println(class1.getName());
        // 通过 getClass 方法
        Class class2 = "String".getClass();
        System.out.println(class2.getName());
        // 通过 forName 方法
        Class class3 = Class.forName("java.lang.String");
        System.out.println(class3.getName());
    }
}
