package com.qg.fangrui.AlgorithmDemo.bsaedata.proxy.CGLIBdynamicdemo;

import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * Created by funrily on 17-8-30
 *
 * @version 1.0.0
 */
public class CglibProxy implements MethodInterceptor {
    // 直接调用Spring框架中的CGLIB动态代理
    private Object target;

    public Object getProxyInstance(Object target) {
        this.target = target;
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(this.target.getClass());
        enhancer.setCallback(this);  // call back method
        return enhancer.create();  // create proxy instance
    }

    @Override
    public Object intercept(Object target, Method method, Object[] args, MethodProxy proxy) throws Throwable {
        before();
        Object result = proxy.invokeSuper(target, args);
        after();
        return result;
    }
    private void before() {
        System.out.println("=== before ===");
    }
    private void after() {
        System.out.println("=== after ===");
    }
}
