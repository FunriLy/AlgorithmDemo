package com.qg.fangrui.AlgorithmDemo.bsaedata.proxy.JDKdynamicdemo;

import com.qg.fangrui.AlgorithmDemo.bsaedata.proxy.staticdemo.Target;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Created by funrily on 17-8-30
 * JDK 实现动态代理
 * @version 1.0.0
 */
public class JDKDynamicProxyHandler implements InvocationHandler {

    private Target target;

    public JDKDynamicProxyHandler(Target target) {
        this.target = target;
    }
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        before();
        Object result = method.invoke(target, args);
        after();
        return result;
    }
    private void before(){
        System.out.println("=== before ===");
    }
    private void after(){
        System.out.println("=== after ===");
    }
}
