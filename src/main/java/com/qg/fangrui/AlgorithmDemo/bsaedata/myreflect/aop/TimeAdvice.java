package com.qg.fangrui.AlgorithmDemo.bsaedata.myreflect.aop;

/**
 * Created by funrily on 17-8-30
 * 具体代理实现接口
 * @version 1.0.0
 */
public class TimeAdvice implements Advice {
    long startTime, endTime;
    @Override
    public void before() {
        startTime = System.currentTimeMillis(); // 记录开始时间
    }
    @Override
    public void after() {
        endTime = System.currentTimeMillis();   // 记录结束时间
        System.out.println("=== 执行时间：" + (endTime - startTime) + " ===");
    }
}
