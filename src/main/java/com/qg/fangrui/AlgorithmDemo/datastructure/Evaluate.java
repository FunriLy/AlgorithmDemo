package com.qg.fangrui.AlgorithmDemo.datastructure;


import java.util.Stack;

/**
 * Created by funrily on 17-8-9
 * Dijkstra的双栈算术表达式求值算法
 * 备注：数字不能大于等于10，不支持负数
 * @version 1.0.0
 */
public class Evaluate {

    public static void main(String[] args) {

        Stack<String> ops = new Stack<>();
        Stack<Double> vals = new Stack<>();
        String s = "(1+((2+3)*2))";
        if (s != null && !s.equals("")){
            for (int i = 0; i<s.length(); i++){
                //读取字符，如果是运算符则入栈
                char ch = s.charAt(i);
                if (ch=='(')
                    ;
                else if (ch == '+')
                    ops.push(String.valueOf(ch));
                else if (ch == '-')
                    ops.push(String.valueOf(ch));
                else if (ch == '*')
                    ops.push(String.valueOf(ch));
                else if (ch == '/')
                    ops.push(String.valueOf(ch));
                else if (ch == ')') {
                    //如果字符为")",弹出运算符和操作数，计算结果并入栈
                    String op = ops.pop();
                    double v = vals.pop();
                    if (op.equals("+"))
                        v = vals.pop() + v;
                    else if (op.equals("-"))
                        v = vals.pop() - v;
                    else if (op.equals("*"))
                        v = vals.pop() * v;
                    else if (op.equals("/"))
                        v = vals.pop() / v;

                    vals.push(v);
                }
                else vals.push(Double.parseDouble(String.valueOf(ch)));
            }
            System.out.println(vals.pop());
        }
    }
}
