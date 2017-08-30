package com.qg.fangrui.AlgorithmDemo.bsaedata.proxy.JDKdynamicdemo;

import com.qg.fangrui.AlgorithmDemo.bsaedata.proxy.staticdemo.Target;
import com.qg.fangrui.AlgorithmDemo.bsaedata.proxy.staticdemo.TargetImpl;

import java.lang.reflect.Proxy;

/**
 * Created by funrily on 17-8-30
 * JDK 实现动态代理
 * @version 1.0.0
 */
public class JDKDynamicProxyTest {
    public static void main(String[] args) {
        // 这里调用了静态代理的被代理对象
        Target target = new TargetImpl();
        JDKDynamicProxyHandler handler = new JDKDynamicProxyHandler(target);
        Target proxySubject = (Target) Proxy.newProxyInstance(TargetImpl.class.getClassLoader(),
                TargetImpl.class.getInterfaces(),handler);
        String result = proxySubject.execute();
        System.out.println(result);
    }
}
