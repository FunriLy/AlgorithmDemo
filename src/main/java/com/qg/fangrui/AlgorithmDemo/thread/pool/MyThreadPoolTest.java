package com.qg.fangrui.AlgorithmDemo.thread.pool;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by funrily on 17-9-5
 * 自定义线程池的实现
 * @version 1.0.0
 */
public class MyThreadPoolTest {
    public static void main(String[] args) {
        ThreadPoolExecutor pool = new ThreadPoolExecutor(// 自定义一个线程池
                1, // coreSize
                2, // maxSize
                60, // 60s
                TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(3) // 有界队列，容量是3个
                , Executors.defaultThreadFactory()
                , new MyRejectedHandler()
        );
        for (int i=0; i<6; i++){
            pool.execute(new MyTask(i, "任务"+i));
            System.out.println("活跃的线程数："+pool.getActiveCount() + ",核心线程数：" + pool.getCorePoolSize() + ",线程池大小：" + pool.getPoolSize() + ",队列的大小" + pool.getQueue().size());
        }
        pool.shutdown();
    }
}
