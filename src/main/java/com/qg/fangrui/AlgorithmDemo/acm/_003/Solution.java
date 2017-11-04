package com.qg.fangrui.AlgorithmDemo.acm._003;

/**
 * Longest Substring Without Repeating Characters
 * Created by funrily on 17-11-4
 * @author funrily
 * @version 1.0.0
 */
class Solution {
    public int lengthOfLongestSubstring(String s) {
        // 存储所有字符个数
        int[] arr = new int[256];
        // 最大长度
        int maxL = 0;
        // 存储当前指针走向，j存储上一次指针走向
        for(int i = 0, j = 0; i < s.length(); ++i){
            char ch = s.charAt(i);
            ++arr[ch];
            // 数组目标值>1,即已经找到重复的字符
            while(arr[ch] > 1){
                --arr[s.charAt(j++)];
            }
            // 所以最大长度=i-j+1，每次只取最大值
            maxL = Math.max(maxL, i - j + 1);
        }
        return maxL;
    }

    public static void main(String[] args) {
        Solution solution = new Solution();
        System.out.println(solution.lengthOfLongestSubstring("abcb"));
    }
}
