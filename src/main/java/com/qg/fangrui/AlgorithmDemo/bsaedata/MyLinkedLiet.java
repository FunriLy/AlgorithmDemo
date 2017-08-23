package com.qg.fangrui.AlgorithmDemo.bsaedata;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by funrily on 17-8-23
 * ArrayList 和 LinkedList 遍历效率比较
 * @version 1.0.0
 */
public class MyLinkedLiet {
    private static int SIZE = 100000;
    public static void main(String[] args) {
        List<Integer> arrayList = new ArrayList<>();
        List<Integer> linkedList = new LinkedList<>();
        for(int i=0; i<SIZE; i++){
            arrayList.add(i);
            linkedList.add(i);
        }
        loopList(arrayList);
        loopList(linkedList);
    }

    private static void loopList(List<Integer> list) {
        int value = 0;
        long start = System.currentTimeMillis();
        for (int i=0; i<SIZE; i++) {
            value = list.get(i);
        }
        System.out.println(list.getClass().getSimpleName() + "使用普通for循环遍历时间为" +
                (System.currentTimeMillis() - start) + "ms");
        start = System.currentTimeMillis();
        for (Integer i : list) {
            value = i;
        }
        System.out.println(list.getClass().getSimpleName() + "使用foreach循环遍历时间为" +
                (System.currentTimeMillis() - start) + "ms");
    }

}
