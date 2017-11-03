## [Two Sum](https://leetcode.com/problems/two-sum/description/)

### Description

------

Given an array of integers, return **indices** of the two numbers such that they add up to a specific target.

You may assume that each input would have **exactly** one solution, and you may not use the *same* element twice.

### Example

-------------------------------

```json
Given nums = [2, 7, 11, 15], target = 9,
Because nums[0] + nums[1] = 2 + 7 = 9,
return [0, 1].
```

### 题意解疑

--------

题目意思很明确，就是给定一个整数数组和一个整数，从中找到两个元素的和为指定值并返回索引值。

### 题目解答

#### 1、使用两层循环

最简单的方法就是使用两层循环，遍历所有的可能性结果，若符合条件则返回索引，若找不到结果返回null。

```java
    public int[] twoSum(int[] nums, int target) {
        for (int i = 0; i < nums.length; i++) {
            for (int j = i + 1; j < nums.length; ++j) {
                if (nums[i] + nums[j] == target) {
                    return new int[]{i, j};
                }
            }
        }
        return null;
    }
```

这种方法也是可以获得`Accepted`，但其复杂度为O(n^2)是其最大的缺点。



#### 2、利用HashMap作为存储

还有一种方法，是利用`目标值-当前元素值`作为键，当前元素值的索引作为值建立键值对Map。

首先对于整个数组进行一次扫描，对于每一个元素进行判断，若该元素不存在于Map中，则取目标值与元素值做差存入Map中；若该元素存在于Map中，证明Map中存在一个值与该元素做和等于目标值，则将获得的两个值组成数组返回；若给定数组遍历完毕，仍然没有找到符合条件的值，则返回null。

```java
    public int[] twoSum(int[] nums, int target) {
        HashMap<Integer, Integer> map = new HashMap<>();
        for (int i=0; i<nums.length; i++) {
            if (map.containsKey(nums[i])) {
                return new int[]{map.get(nums[i]), i};
            }
            // 将指定值与数组元素做差并成为索引存入Map
            map.put(target-nums[i], i);
        }
        return null;
    }
```

