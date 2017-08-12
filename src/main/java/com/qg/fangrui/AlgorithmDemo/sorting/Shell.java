package com.qg.fangrui.AlgorithmDemo.sorting;

/**
 * Created by funrily on 17-8-12
 * 希尔排序
 * @version 1.0.0
 */
public class Shell {

    //将a[]按升序排序
    public static void sort(Comparable[] a) {
        int N = a.length;
        int h = 1;
        while (h < N/3) h = h*3+1;
        while (h >= 1){
            for (int i=h; i<N; i++){
                for (int j=i; j>=h && less(a[j], a[j-h]); j-=h){
                    exch(a, j, j-h);
                }
            }
            h = h / 3;
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
