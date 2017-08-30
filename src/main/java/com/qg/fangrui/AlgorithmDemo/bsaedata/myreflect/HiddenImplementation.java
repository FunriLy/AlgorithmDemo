package com.qg.fangrui.AlgorithmDemo.bsaedata.myreflect;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by funrily on 17-8-29
 * 反射代理实现
 * @version 1.0.0
 */
public class HiddenImplementation {

    public static void main(String[] args) {
        try {
            Class clazz = Class.forName("com.qg.fangrui.AlgorithmDemo.bsaedata.myreflect.C");
            Object obj = clazz.newInstance();
            System.out.println("=== 获得成员方法 Method ===");
            Method[] methods = clazz.getDeclaredMethods();
            for (Method method : methods){
                method.setAccessible(true);
                System.out.println(method.getName());
                method.invoke(obj);
            }
            System.out.println("=== 获得成员变量 Field ===");
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);  // 设置 private 可访问，若是访问 public 则不需要
                System.out.println(field.get(obj));
            }
            System.out.println("=== 获得构造函数 Constructor ===");
            Constructor constructor = clazz.getDeclaredConstructor(String.class);
            constructor.setAccessible(true);
            constructor.newInstance("Constructor");
        }  catch (InvocationTargetException| IllegalAccessException |
                ClassNotFoundException | InstantiationException | NoSuchMethodException e) {
            e.printStackTrace();
        }
    }
}
