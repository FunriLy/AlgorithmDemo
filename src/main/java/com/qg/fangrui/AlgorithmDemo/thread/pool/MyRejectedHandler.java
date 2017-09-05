package com.qg.fangrui.AlgorithmDemo.thread.pool;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created by funrily on 17-9-5
 * 自定义拒绝策略
 * @version 1.0.0
 */
public class MyRejectedHandler implements RejectedExecutionHandler {
    @Override
    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
        MyTask task = (MyTask) r;
        System.out.println("报警信息：" + task.getName() + " 被线程池拒绝，没有被执行");
    }
}
