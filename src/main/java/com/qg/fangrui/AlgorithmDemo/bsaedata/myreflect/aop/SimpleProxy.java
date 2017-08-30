package com.qg.fangrui.AlgorithmDemo.bsaedata.myreflect.aop;


import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Created by funrily on 17-8-30
 * 代理绑定类
 * @version 1.0.0
 */
public class SimpleProxy implements InvocationHandler{

    private Object obj;
    private Advice advice;
    // 指定代理对象
    public Object bind(Object obj, Advice advice){
        this.obj = obj;
        this.advice = advice;
        return Proxy.newProxyInstance(obj.getClass().getClassLoader(), obj.getClass().getInterfaces(), this);
    }
    // 实现代理
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object result = null;
        try {
            advice.before();
            result = method.invoke(obj, args);
            advice.after();
        } catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }
}
