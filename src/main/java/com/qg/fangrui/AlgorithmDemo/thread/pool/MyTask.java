package com.qg.fangrui.AlgorithmDemo.thread.pool;

/**
 * Created by funrily on 17-9-5
 * 定义一个线程类
 * @version 1.0.0
 */
public class MyTask implements Runnable {
    private int id;
    private String name;

    public MyTask(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void run() {
        System.out.println("ID:" + id + ", Name:" + name);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
