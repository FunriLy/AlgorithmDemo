## [Median of Two Sorted Arrays](https://leetcode.com/problems/median-of-two-sorted-arrays/description/)

### Description

---------------------------

There are two sorted arrays **nums1** and **nums2** of size m and n respectively.

Find the median of the two sorted arrays. The overall run time complexity should be O(log (m+n)).

### Example

------------------

Example 1 :

```
nums1 = [1, 3]
nums2 = [2]

The median is 2.0
```

Example 2 :

```
nums1 = [1, 2]
nums2 = [3, 4]

The median is (2 + 3)/2 = 2.5
```

### 题意解疑

-------------------------------

给予两个排序(有小到大)的数组，寻找两个数组合并后的中位数，并且要求时间复杂度应该为  O(log (m+n))。

### 题目解答

---------------

思路一：

最开始的时候，我选择了最直接、最原始的方法，直接使用JDK自带的方法将两个数组进行合并为新数组并重新排序，取其中位数：

```java
    public double findMedianSortedArrays(int[] nums1, int[] nums2) {
        int m = nums1.length;
        int n = nums2.length;
        nums1 = Arrays.copyOf(nums1, m+n);			// 数组扩容
        System.arraycopy(nums2, 0, nums1, m , n);	// 数组合并
        Arrays.sort(nums1);							// 重新排序
        if ((m+n) % 2 != 0) {
            return (double) nums1[(n+m)/2];
        } else {
            return (nums1[(n+m)/2] + nums1[(n+m-1)/2]) / 2.0;
        }
    }
```

在LeetCode上跑耗时是65ms，状态是 Accepted。

可是我们知道这里调用了`Arrays.sort()`方法，其复杂度最坏情况为`O(nlogn)`。（PS：这就说明了leetcode对于算法运行时间上正确性的要求并不高……）



思路二：

思路二其实就是在两个已经排序好的数组中寻找最k小的元素的思路。

假设，数组A元素个数为m，数组B元素个数为n，求第k小元素。

1. 取 A[k/2] 与 B[k/2]
2. 将两者进行比较。若 A[k/2] > B[k/2]，说明所求元素必然不存在 B 前 k/2 个元素中；反之，则不存在 A 前 k/2 个元素中。
3. 将 A 或者 B 前 k/2 个元素删除，求第 k-k/2 小的元素。
4. 如果 k/2 大于某个数组元素个数，证明所求元素必定在于另外一个数组中。
5. 递归操作，得到最后解。

```java
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
        if (l1 >= nums1.length) {	// 第4点
            return nums2[l2 + k -1];
        }
        if (l2 >= nums2.length) {	// 第4点
            return nums1[l1 + k -1];
        }

        if (k == 1) {
            return nums1[l1] > nums2[l2] ? nums2[l2] : nums1[l1];
        }
		// 第4点
        int key1 = l1+k/2-1>=nums1.length ? Integer.MAX_VALUE : nums1[l1+k/2-1];
        int key2 = l2+k/2-1>=nums2.length ? Integer.MAX_VALUE : nums2[l2+k/2-1];
        if (key1 < key2) {	// 递归调用
            return findKth(nums1, l1+k/2, nums2, l2, k-k/2);
        } else {
            return findKth(nums1, l1, nums2, l2+k/2, k-k/2);
        }
    }
```

这种算法在最好的情况下，每次都有k/2元素被删除掉，所以可以说算法的复杂度为  O(log (m+n))。



PS：第一份代码的消耗时间为65ms，第二份代码的消耗时间为76ms，可见样本案例的数据是多么重要。