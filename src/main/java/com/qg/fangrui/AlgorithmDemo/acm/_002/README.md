## [Add Two Numbers](https://leetcode.com/problems/add-two-numbers/description/)

### Description

--------------------------

You are given two **non-empty** linked lists representing two non-negative integers. The digits are stored in reverse order and each of their nodes contain a single digit. Add the two numbers and return it as a linked list.

You may assume the two numbers do not contain any leading zero, except the number 0 itself.

**Input:** (2 -> 4 -> 3) + (5 -> 6 -> 4)
**Output:** 7 -> 0 -> 8



### 题意解疑

------

这道题的意思是给你两个链表，用链表来代替数实现两个链表相加。用example的例子就是实现 342+465=807。

注意，**链表代表的整数，低位在前，高位在后**！

### 题目解答

--------------------

这道题的思路就是新建一个链表，将两个给定的链表从头往后撸，每两个相加，添加一个新节点到新链表后面。注意处理好进位的问题即可。

```java
    public ListNode addTwoNumbers(ListNode l1, ListNode l2) {
        ListNode node = new ListNode(-1);
        ListNode cur = node;
        // 存储进位值
        int sum = 0;
        while (l1 != null || l2 != null) {
            // 取出两个链表的值
            int d1 = 0, d2 = 0;
            if (l1 != null) {
                d1 = l1.val;
                l1 = l1.next;
            }
            if (l2 != null) {
                d2 = l2.val;
                l2 = l2.next;
            }
            int temp = d1+d2+sum;	// 加上来自低位的进位值
            sum = temp>=10? 1 : 0;
            cur.next = new ListNode(temp % 10);
            cur = cur.next;
        }
        if (sum == 1) {	// 解决最高位进位问题
            cur.next = new ListNode(1);
        }
        return node.next;
    }
```

