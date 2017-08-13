package com.qg.fangrui.AlgorithmDemo.sorting;

/**
 * Created by funrily on 17-8-13
 * 归并排序
 * @version 1.0.0
 */
public class Merge {

    private static Comparable[] aux;        //归并所需的辅助数组

    //自顶向下归并排序
    public static void sort(Comparable[] a){
        aux = new Comparable[a.length];
        sort(a, 0, a.length-1);
    }

    //自底向上
//    public static void sort(Comparable[] a) {
//        int N = a.length;
//        aux = new Comparable[N];
//        for (int sz=1; sz<N; sz=sz+sz) {
//            for (int lo= 0; lo<N-sz; lo += sz+sz) {
//                merge(a, lo, lo+sz-1, Math.min(lo+sz+sz-1, N-1));
//            }
//        }
//    }

    //将数组a[lo……hi]排序
    private static void sort(Comparable[] a, int lo, int hi) {
        if (hi<=lo) return;
        int mid = lo+(hi-lo)/2;
        sort(a, lo, mid);       //将左半边排序
        sort(a, mid+1, hi);  //将右半边排序
        merge(a, lo, mid, hi);  //归并结果
    }


    //将a[lo……mid]和a[mid+1……hi]归并
    public static void merge(Comparable[] a, int lo, int mid, int hi) {
        int i = lo, j = mid+1;
        for (int k=lo; k<=hi; k++) {
            aux[k] = a[k];      //复制操作
        }

        for (int k=lo; k<=hi; k++) {
            if (i>mid)                      a[k] = aux[j++];
            else if (j>hi)                  a[k] = aux[i++];
            else if (less(aux[j], aux[i]))  a[k] = aux[j++];
            else                            a[k] = aux[i++];
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
