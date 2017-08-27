package com.qg.fangrui.AlgorithmDemo.bsaedata;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by funrily on 17-8-25
 * 实验 fail-fast 机制
 * @version 1.0.0
 */
public class FailFastTest {
//    private static final List<Integer> list = new CopyOnWriteArrayList<>();
    private static final List<Integer> list = new ArrayList<>();
    /**
     * 线程One负责迭代链表
     */
    private static class threadOne extends Thread {
        public void run() {
            Iterator<Integer> it = list.iterator();
            while (it.hasNext()) {
                int i = it.next();
                System.out.println("ThreadOne 遍历：" + i);
                try {
                    sleep(10);
                } catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 线程Two负责修改链表
     */
    private static class threadTwo extends Thread {
        public void run() {
            int i=0;
            while (i<6){
                System.out.println("ThreadTwo run：" + i);
                if (i == 3){
                    list.remove(i);
                }
                i++;
            }
        }
    }

    public static void main(String[] args) {
        for (int i=0; i<20; i++) {
            list.add(i);
        }
        new threadOne().start();
        new threadTwo().start();
    }
}