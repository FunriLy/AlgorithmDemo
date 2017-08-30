package com.qg.fangrui.AlgorithmDemo.bsaedata.myreflect.aop;

/**
 * Created by funrily on 17-8-30
 * 调用测试
 * @version 1.0.0
 */
public class Test {
    public static void main(String[] args) {
        SimpleProxy proxy = new SimpleProxy();
        ClientInterface client = (ClientInterface) proxy.bind(new Client(), new TimeAdvice());
        client.client();
    }
}
