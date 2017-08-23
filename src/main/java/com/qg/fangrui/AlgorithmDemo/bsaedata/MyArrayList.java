package com.qg.fangrui.AlgorithmDemo.bsaedata;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by funrily on 17-8-21
 * ArrayList 遍历效率实验
 * @version 1.0.0
 */
public class MyArrayList {
    public static void main(String[] args) {
        List<Integer> list = new ArrayList<>();
        for (int i=0; i<100000; i++){
            list.add(i);
        }
        System.out.println("当前list数组长度为 " + list.size());
        iteratorThroughIterator(list);
        iteratorThroughRandomAccess(list);
        iteratorThroughForForeach(list);
    }
    // 通过迭代器(Iterator)来遍历
    public static void iteratorThroughIterator(List list) {
        Integer value = 0;
        long startTime=0, endTime=0;
        startTime = System.currentTimeMillis();
        for(Iterator iter = list.iterator(); iter.hasNext(); ) {
            value = (Integer) iter.next();
        }
        endTime = System.currentTimeMillis();
        long interval = endTime - startTime;
        System.out.println("iteratorThroughIterator：" + interval+" ms");
    }
    // 通过索引值遍历
    public static void iteratorThroughRandomAccess(List list) {
        int value = 0;
        long startTime=0, endTime=0;
        startTime = System.currentTimeMillis();
        for (int i=0; i<list.size(); i++) {
            value = (Integer) list.get(i);
        }
        endTime = System.currentTimeMillis();
        long interval = endTime - startTime;
        System.out.println("iteratorThroughRandomAccess：" + interval+" ms");
    }
    // foreach循环遍历
    public static void iteratorThroughForForeach(List list) {
        Integer value = 0;
        long startTime=0, endTime=0;
        startTime = System.currentTimeMillis();
        for(Object obj:list) {
            value = (Integer)obj;
        }
        endTime = System.currentTimeMillis();
        long interval = endTime - startTime;
        System.out.println("iteratorThroughForForeach：" + interval+" ms");
    }
}
