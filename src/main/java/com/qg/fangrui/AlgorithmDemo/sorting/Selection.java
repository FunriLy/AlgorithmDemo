package com.qg.fangrui.AlgorithmDemo.sorting;

/**
 * Created by funrily on 17-8-11
 * 选择排序
 * @version 1.0.0
 */
public class Selection {

    //将a[]按升序排序
    public static void sort(Comparable[] a) {
        int N = a.length;
        for (int i=0; i<N; i++){
            //将a[i]和a[i+1……N]中最小的元素交换
            int min = i;    //最小元素的索引
            for (int j=i+1; j<N; j++) {
                if (less(a[j], a[min]))
                    min = j;
            }
            exch(a, i, min);
        }
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
