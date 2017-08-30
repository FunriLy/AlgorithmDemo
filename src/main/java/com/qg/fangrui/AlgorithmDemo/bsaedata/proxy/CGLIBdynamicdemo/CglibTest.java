package com.qg.fangrui.AlgorithmDemo.bsaedata.proxy.CGLIBdynamicdemo;

import com.qg.fangrui.AlgorithmDemo.bsaedata.proxy.staticdemo.Target;
import com.qg.fangrui.AlgorithmDemo.bsaedata.proxy.staticdemo.TargetImpl;

/**
 * Created by funrily on 17-8-30
 *
 * @version 1.0.0
 */
public class CglibTest {
    public static void main(String[] args) {
        CglibProxy proxy = new CglibProxy();
        Target hello = (Target) proxy.getProxyInstance(new TargetImpl());
        String result = hello.execute();
        System.out.println(result);
    }

}
