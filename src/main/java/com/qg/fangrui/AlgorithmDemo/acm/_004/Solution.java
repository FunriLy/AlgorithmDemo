package com.qg.fangrui.AlgorithmDemo.acm._004;

import java.util.Arrays;

/**
 * Median of Two Sorted Arrays
 * Created by funrily on 17-11-6
 * @author funrily
 * @version 1.0.0
 */
class Solution {
    public double findMedianSortedArrays(int[] nums1, int[] nums2) {
        int m = nums1.length;
        int n = nums2.length;
        // 奇数
        if ((m+n) % 2 != 0) {
            return findKth(nums1, 0, nums2, 0, (m+n)/2+1);
        }
        // 偶数
        return (findKth(nums1, 0, nums2, 0, (m+n)/2)
                + findKth(nums1, 0, nums2, 0, (m+n)/2+1))/2;
    }

    /**
     * 寻找两个数组中第 k 小的元素
     * @param nums1 数组1
     * @param l1 数组1的启始位置
     * @param nums2 数组2
     * @param l2 数组2的启始位置
     * @param k 所求元素索引
     * @return 返回第k小元素的值
     */
    private static double findKth(int[] nums1, int l1, int[] nums2, int l2, int k){
        if (l1 >= nums1.length) {
            return nums2[l2 + k -1];
        }
        if (l2 >= nums2.length) {
            return nums1[l1 + k -1];
        }

        if (k == 1) {
            return nums1[l1] > nums2[l2] ? nums2[l2] : nums1[l1];
        }

        int key1 = l1+k/2-1>=nums1.length ? Integer.MAX_VALUE : nums1[l1+k/2-1];
        int key2 = l2+k/2-1>=nums2.length ? Integer.MAX_VALUE : nums2[l2+k/2-1];
        if (key1 < key2) {
            return findKth(nums1, l1+k/2, nums2, l2, k-k/2);
        } else {
            return findKth(nums1, l1, nums2, l2+k/2, k-k/2);
        }
    }

    /**
     * 直接调用JDK提供的方法
     * @param nums1 数组1
     * @param nums2 数组2
     * @return 求两个数组合并排序后的中位数
     */
    public double findMedianSortedArrays2(int[] nums1, int[] nums2) {
        int m = nums1.length;
        int n = nums2.length;
        nums1 = Arrays.copyOf(nums1, m+n);
        System.arraycopy(nums2, 0, nums1, m , n);
        Arrays.sort(nums1);
        if ((m+n) % 2 != 0) {
            return (double) nums1[(n+m)/2];
        } else {
            return (nums1[(n+m)/2] + nums1[(n+m-1)/2]) / 2.0;
        }
    }

    public static void main(String[] args) {
        Solution solution = new Solution();
        int[] a1 = {1, 3};
        int[] a2 = {2};
        System.out.println(solution.findMedianSortedArrays(a1, a2));
        int[] b1 = {1};
        int[] b2 = {2, 3, 4, 5, 6};
        System.out.println(solution.findMedianSortedArrays(b1, b2));
        int[] c1 = {1, 2};
        int[] c2 = {3, 4};
        System.out.println(solution.findMedianSortedArrays(c1, c2));
    }
}