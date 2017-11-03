package com.qg.fangrui.AlgorithmDemo.acm._001;

import java.util.Arrays;
import java.util.HashMap;

/**
 * Two Sum
 * Created by funrily on 17-11-3
 * @author funrily
 * @version 1.0.0
 */
public class Solution {

    /**
     * 方法1：采用两层循环
     * @param nums
     * @param target
     * @return
     */
//    public int[] twoSum(int[] nums, int target) {
//        for (int i = 0; i < nums.length; i++) {
//            for (int j = i + 1; j < nums.length; ++j) {
//                if (nums[i] + nums[j] == target) {
//                    return new int[]{i, j};
//                }
//            }
//        }
//        return null;
//    }

    /**
     * 方法2：利用HashMap作为存储
     * @param nums
     * @param target
     * @return
     */
    public int[] twoSum(int[] nums, int target) {
        HashMap<Integer, Integer> map = new HashMap<>();
        for (int i=0; i<nums.length; i++) {
            // 如果新的索引值已经存在Map中证明已经找到指定的两个元素
            if (map.containsKey(nums[i])) {
                return new int[]{map.get(nums[i]), i};
            }
            // 将指定值与数组元素做差并成为索引存入Map
            map.put(target-nums[i], i);
        }
        return null;
    }

    public static void main(String[] args) {
        Solution solution = new Solution();
        int[] nums = {3, 2, 4};
        int target = 6;
        // 答案为[1, 2]
        System.out.println(Arrays.toString(solution.twoSum(nums, target)));
    }
}
