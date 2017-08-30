package com.qg.fangrui.AlgorithmDemo.bsaedata.proxy.staticdemo;

/**
 * Created by funrily on 17-8-30
 *
 * @version 1.0.0
 */
public class ProxyTest {
    public static void main(String[] args) {
        Target target = new TargetImpl();
        proxy proxy = new proxy(target);
        System.out.println(proxy.execute());
    }
}
