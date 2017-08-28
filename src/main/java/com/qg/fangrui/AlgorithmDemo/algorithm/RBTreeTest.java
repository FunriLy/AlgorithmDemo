package com.qg.fangrui.AlgorithmDemo.algorithm;

/**
 * Created by funrily on 17-8-28
 * 红黑树测试
 * @version 1.0.0
 *
 */
public class RBTreeTest {
    private static final int a[] = {10, 40, 30, 60, 90, 70, 20, 50, 80};
    private static final boolean mDebugInsert = false;    // 改为 true 可以看到每一步插入操作后的结果
    private static final boolean mDebugDelete = false;    // 改为 true 可以看到每一步删除操作后的结果

    public static void main(String[] args) {
        int len = a.length;
        RBTree<Integer> tree = new RBTree<>();
        System.out.print("== 原始数据：");
        for (int i=0; i<len; i++) {
            System.out.print(a[i] + " ");
        }
        System.out.println();
        for (int i=0; i<len; i++){
            tree.insert(a[i]);
            if (mDebugInsert){  // 调试插入
                System.out.println("== 插入节点为：" + a[i]);
                System.out.println("== 树的详细情况：");
                tree.print();
                System.out.println();
            }
        }

        System.out.println("== 最小值：" + tree.minimum());
        System.out.println("== 最大值：" + tree.maximum());
        System.out.println("== 树的详细情况：");
        tree.print();
        System.out.println();

        for (int i=0; i<len; i++) {
            tree.remove(a[i]);
            if (mDebugDelete) {
                System.out.println("== 删除节点为：" + a[i]);
                System.out.println("== 树的详细情况：");
                tree.print();
                System.out.println();
            }
        }

        tree.clear();
    }
}
