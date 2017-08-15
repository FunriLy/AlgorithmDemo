package com.qg.fangrui.AlgorithmDemo.sorting;

/**
 * Created by funrily on 17-8-15
 * 快速排序
 * @version 1.0.0
 */
public class Quick {

    /**
     * 快排
     * 最好的情况：每次都正好将数组对半分，这种情况下次数满足分治递归公式 Cn=2Cn+N，解为Cn～Nlgn
     * 最差情况：切分不平衡时，极为低效。有大数组出现。
     */

    public static void sort(Comparable[] a) {
        sort(a, 0, a.length-1);
    }

    private static void sort(Comparable[] a, int lo, int hi) {
        if (hi <= lo)
            return;
        int j = partition(a, lo, hi);   //切分
        sort(a, lo, j-1);   //对左边区域排序
        sort(a, j+1, hi);   //对右边区域排序
    }

    //将数组切分为a[lo……i-1]、a[i]、a[i+1……hi]
    private static int partition(Comparable[] a, int lo, int hi) {
        int i = lo, j = hi+1;
        Comparable v = a[lo];
        //根据a[lo]原来的值划分两个区域，但不考虑区域内的排序
        while (true) {
            while (less(a[++i], v))
                if (i == hi) break;
            while (less(v, a[--j]))
                if (j == lo ) break;

            if (i >= j)
                break;

            exch(a, i, j);
        }

        exch(a, lo, j);
        return j;
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
        String a[] = {"S", "O", "R", "T", "E", "X", "A", "M", "P", "L", "E"};
        sort(a);
        assert isSorted(a);
        show(a);
    }
}
