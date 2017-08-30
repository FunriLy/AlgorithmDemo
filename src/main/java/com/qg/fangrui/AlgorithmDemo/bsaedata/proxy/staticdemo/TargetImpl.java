package com.qg.fangrui.AlgorithmDemo.bsaedata.proxy.staticdemo;

/**
 * Created by funrily on 17-8-30
 *
 * @version 1.0.0
 */
public class TargetImpl implements Target {
    @Override
    public String execute() {
        System.out.println("TargetImpl execute！");
        return "execute";
    }
}
