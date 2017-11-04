## [Longest Substring Without Repeating Characters](https://leetcode.com/problems/longest-substring-without-repeating-characters/description/)

### Description

-----------------------

Given a string, find the length of the **longest substring** without repeating characters.

### Example

-------------

Given `"abcabcbb"`, the answer is `"abc"`, which the length is 3.

Given `"bbbbb"`, the answer is `"b"`, with the length of 1.

Given `"pwwkew"`, the answer is `"wke"`, with the length of 3. Note that the answer must be a **substring**, `"pwke"` is a *subsequence* and not a substring.

### 题意解疑

------------------------

计算不带重复的最长字符串的长度。

### 题目解答

---------------

本道题的解题思路来源于分享：https://discuss.leetcode.com/topic/22048/my-easy-solution-in-java-o-n/2

首先开辟一个数组来存储字符在当前字符时的计数。(PS:这句话比较别扭，意思就是说，在该数组标记字符是否重复出现)

用两个变量来执行循环操作，i代表当前遍历到的字符索引，j代表上一次该字符出现的索引位置。所以使用`i-j+1`即可得到两个重复字符串之间的长度。

```java
    public int lengthOfLongestSubstring(String s) {
        int[] arr = new int[256];
        int maxL = 0;
        for(int i = 0, j = 0; i < s.length(); ++i){
            char ch = s.charAt(i);
            ++arr[ch];
            while(arr[ch] > 1){
                --arr[s.charAt(j++)];
            }
            maxL = Math.max(maxL, i - j + 1);
        }
        return maxL;
    }
```

里面需要注意的一点是：有可能j的值代表的不是当前字符串上一次出现的索引，所以使用以下代码进行排除

```java
            while(arr[ch] > 1){
                --arr[s.charAt(j++)];
            }
```

#### 举例

用一个简单的字符串“abcb”来证明一下吧。

```json
i=0,j=0;
	mxaL=1;
i=1,j=0;
	maxL=2;
i=2,j=0;
	maxL=3;
i=3,j=0;
	// 注意这里对j的位置进行了调整，同时这种调整也不会影响到j原来的值
	// 假如字符串为“abcba”，两个a之间已经存在重复字符所以不用重复记录前一个a的位置
	内层循环：i=3，j=0;	
	内层循环：i=3;j=1;
	max=3;
return maxL;
```

