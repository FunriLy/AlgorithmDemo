package com.qg.fangrui.AlgorithmDemo.NIO;

/**
 * Created by funrily on 17-9-27
 * NIO 代码示例：时间客户端
 * @version 1.0.0
 */
public class NIOTimeClient {

    public static void main(String[] args) {
        System.out.println("启动时间客户端！");
        new Thread(new TimeClientHandle("127.0.0.1", 10086), "NIO-Time-Client").start();

    }
}
