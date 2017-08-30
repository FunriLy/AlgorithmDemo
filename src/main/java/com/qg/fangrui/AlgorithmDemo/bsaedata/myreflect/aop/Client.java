package com.qg.fangrui.AlgorithmDemo.bsaedata.myreflect.aop;

/**
 * Created by funrily on 17-8-30
 * 具体逻辑实现类
 * @version 1.0.0
 */
public class Client implements ClientInterface {
    public void client(){
        System.out.println("=== 开始逻辑处理 ===");
        try {
            Thread.sleep(2000); // 模拟逻辑处理
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("=== 结束逻辑处理 ===");
    }
}
