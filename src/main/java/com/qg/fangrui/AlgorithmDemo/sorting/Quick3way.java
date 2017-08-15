package com.qg.fangrui.AlgorithmDemo.sorting;

/**
 * Created by funrily on 17-8-15
 * 三向切分的快速排序
 * 针对了含有大量重复元素的数组
 * @version 1.0.0
 */
public class Quick3way {

    public static void sort(Comparable[] a) {
        sort(a, 0, a.length-1);
    }

    private static void sort(Comparable[] a, int lo, int hi) {
        if (hi <= lo)
            return;
        int lt=lo, i=lo+1, gt=hi;
        Comparable v = a[lo];
        while (i<=gt) {
            int cmp = a[i].compareTo(v);
            if (cmp < 0)
                exch(a, lt++, i++);
            else if (cmp > 0)
                exch(a, i, gt--);
            else
                i++;    //将i的区域留出来

        }   //a[lo……lt-1] < v = a[lt……gt] < a[gt+1……hi] 成立
        sort(a, lo, lt-1);
        sort(a, gt+1, hi);
    }

    private static boolean less(Comparable v, Comparable w){
        return v.compareTo(w) < 0;
    }

    //交换两个元素
    private static void exch(Comparable[] a, int i, int j) {
        Comparable t = a[i];
        a[i] = a[j];
        a[j] = t;
    }

    private static void show(Comparable[] a){
        for (int i=0; i<a.length; i++){
            System.out.print(a[i] + " ");
        }
        System.out.println();
    }

    //测试数组是否有序
    public static boolean isSorted(Comparable[] a){
        for (int i=0; i<a.length; i++){
            if (less(a[i], a[i-1]))
                return false;
        }
        return true;
    }

    public static void main(String[] args) {
        String[] a = {"R", "B", "W", "W", "R", "W", "B", "R", "R", "W", "B", "R"};
        sort(a);
        assert isSorted(a);
        show(a);
    }
}
