package com.qg.fangrui.AlgorithmDemo.acm._002;

import com.qg.fangrui.AlgorithmDemo.acm.structure.ListNode;

/**
 * Add Two Numbers
 * Created by funrily on 17-11-3
 * @author funrily
 * @version 1.0.0
 */
public class Solution {
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
            int temp = d1+d2+sum;
            sum = temp>=10? 1 : 0;
            cur.next = new ListNode(temp % 10);
            cur = cur.next;
        }
        if (sum == 1) {
            cur.next = new ListNode(1);
        }
        return node.next;
    }

    public static void main(String[] args) {
        Solution solution = new Solution();
        ListNode.print(solution.addTwoNumbers(
                ListNode.createTestData("[2,4,3]"),
                ListNode.createTestData("[5,6,4]")
        ));
    }
}
