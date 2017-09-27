package com.qg.fangrui.AlgorithmDemo.NIO;

/**
 * Created by funrily on 17-9-26
 * NIO 代码示例：时间服务器
 * @version 1.0.0
 */
public class NIOTimeServer {
    public static void main(String[] args) {
        System.out.println("启动时间服务器!");
        MultiplexerTimeServer timeServer = new MultiplexerTimeServer(10086);
        new Thread(timeServer, "NIO-Time-Server").start();
    }
}
