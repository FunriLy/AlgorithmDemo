package com.qg.fangrui.AlgorithmDemo.datastructure;

import java.util.Iterator;

/**
 * Created by funrily on 17-8-11
 * 下压栈(LIFO):链表实现
 * @version 1.0.0
 */
public class Stack<Item> implements Iterable<Item> {

    private class Node{
        //定义结点嵌套类
        Item item;
        Node next;
    }

    private Node first;     //栈顶(最近添加的元素)
    private int N;          //元素个数

    public boolean isEmpty(){
        return first == null;
    }

    public int size(){
        return N;
    }

    public void push(Item item){
        //向栈顶添加元素
        Node oldfirst = first;
        first = new Node();
        first.item = item;
        first.next = oldfirst;
        N++;
    }

    public Item pop(){
        //从栈顶删除元素
        Item item = first.item;
        first = first.next;
        N--;
        return item;
    }

    @Override
    public Iterator<Item> iterator() {
        return null;
    }

    public class ListIterator implements Iterator<Item> {

        private Node current = first;

        @Override
        public boolean hasNext() {
            return current != null;
        }

        @Override
        public Item next() {
            Item item = current.item;
            current = current.next;
            return item;
        }
    }
}
