package com.qg.fangrui.AlgorithmDemo.bsaedata.myreflect;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by funrily on 17-8-30
 * 利用反射实现泛型擦除
 * @version 1.0.0
 */
public class ReflectGeneric {
    public static void main(String[] args) {
        List list1 = new ArrayList();               // 没有泛型
        List<String> list2 = new ArrayList<>();     // 有泛型

        // 先进行正常的元素添加方式，在编译期对泛型进行检查
        list2.add("hello");
        System.out.println("List2 的长度为 " + list2.size());

        /*
        通过反射来实现泛型擦除：
        1、先获得反射的类；
        2、通过方法反射绕过编译器来调用add方法
         */
        Class clazz1 = list1.getClass();
        Class clazz2 = list2.getClass();
        System.out.println("检查两个类的类型是否相同：" + (clazz1 == clazz2));
        try {
            Method method = clazz2.getMethod("add", Object.class);  // 反射得到add方法
            method.invoke(list2, 20);
            System.out.println("List2 的长度为 " + list2.size());
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
