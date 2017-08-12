package com.qg.fangrui.AlgorithmDemo.sorting;

/**
 * Created by funrily on 17-8-12
 * 插入排序
 * @version 1.0.0
 */
public class Insertion {

    //将a[]按升序排序
    public static void sort(Comparable[] a) {
        int N = a.length;
        //i不需要从0开始，没有意义
        for (int i=1; i<N; i++){
            //将a[i]插入到i之前的已经排序好的序列
            // TODO: 17-8-12 可以改进，将第i元素抽离，并所有大元素向右移动
            for (int j=i; j>0 && less(a[j], a[j-1]); j--){
                exch(a, j, j-1);
            }
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
