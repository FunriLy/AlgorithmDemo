package com.qg.fangrui.AlgorithmDemo.bsaedata;

import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

/**
 * Created by funrily on 17-8-31
 * 多线程下HashMap、Hashtable、ConcurrentHashMap的性能差异比较
 * @version 1.0.0
 */
public class MyConcurrentHashMap {
    static final int THREADNUMBER = 50; // 线程数
    static final int NUMBER = 5000;     // 元素个数

    public static void main(String[] args) throws Exception {
        // 定义三种Map，其中 HashMap 是线程安全的 Map 防止多线程报错
        Map<String, Integer> hashmapSync = Collections.synchronizedMap(new HashMap<>());
        Map<String, Integer> concurrentHashMap = new ConcurrentHashMap<>();
        Map<String, Integer> hashtable = new Hashtable<>();
        long totalA = 0L, totalB = 0L, totalC = 0L;
        for (int i = 0; i <= 100; i++) {
            totalA += put(hashmapSync);
            totalB += put(concurrentHashMap);
            totalC += put(hashtable);
        }
        System.out.println("put time HashMapSync = " + totalA + "ms.");
        System.out.println("put time ConcurrentHashMap = " + totalB + "ms.");
        System.out.println("put time Hashtable = " + totalC + "ms.");
        totalA = 0;
        totalB = 0;
        totalC = 0;
        for (int i = 0; i <= 10; i++) {
            totalA += get(hashmapSync);
            totalB += get(concurrentHashMap);
            totalC += get(hashtable);
        }
        System.out.println("get time HashMapSync=" + totalA + "ms.");
        System.out.println("get time ConcurrentHashMap=" + totalB + "ms.");
        System.out.println("get time Hashtable=" + totalC + "ms.");
    }
    // 多线程对同一个Map执行put操作
    public static long put(Map<String, Integer> map) throws Exception {
        long start = System.currentTimeMillis();
        CountDownLatch countDownLatch = new CountDownLatch(THREADNUMBER);   // 多线程同步工具
        for (int i = 0; i < THREADNUMBER; i++) {
            new PutThread(map, countDownLatch).start();
        }
        countDownLatch.await();
        return System.currentTimeMillis() - start;
    }
    // 多线程对同一个Map执行get操作
    public static long get(Map<String, Integer> map) throws Exception {
        long start = System.currentTimeMillis();
        CountDownLatch countDownLatch = new CountDownLatch(THREADNUMBER);
        for (int i = 0; i < THREADNUMBER; i++) {
            new GetThread(map, countDownLatch).start();
        }
        countDownLatch.await();
        return System.currentTimeMillis() - start;
    }
}

class PutThread extends Thread {
    private Map<String, Integer> map;
    private CountDownLatch countDownLatch;
    private String key = "Put - " + this.getId();

    PutThread(Map<String, Integer> map, CountDownLatch countDownLatch) {
        this.map = map;
        this.countDownLatch = countDownLatch;
    }

    public void run() {
        for (int i = 1; i <= MyConcurrentHashMap.NUMBER; i++) {
            map.put(key, i);
        }
        countDownLatch.countDown();
    }
}

class GetThread extends Thread {
    private Map<String, Integer> map;
    private CountDownLatch countDownLatch;
    private String key = "Get - " + this.getId();
    GetThread(Map<String, Integer> map, CountDownLatch countDownLatch) {
        this.map = map;
        this.countDownLatch = countDownLatch;
    }
    public void run(){
        for (int i = 1; i <= MyConcurrentHashMap.NUMBER; i++) {
            map.get(key);
        }
        countDownLatch.countDown();
    }
}