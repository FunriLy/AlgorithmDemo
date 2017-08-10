package com.qg.fangrui.AlgorithmDemo.datastructure;


import java.util.Iterator;

/**
 * Created by funrily on 17-8-10
 * 下压栈(LIFO):动态调整数组大小
 * @version 1.0.0
 */
public class ResizingArrayStack<Item> implements Iterable<Item> {

    private Item[] a = (Item[]) new Object[1];
    private int N = 0;

    public boolean isEmpty() {
        return N == 0;
    }
    public int size() {
        return N;
    }

    private void resize(int max) {
        //将栈移动到一个大小为max的数组
        Item[] temp = (Item[]) new Object[max];
        for (int i=0; i<N; i++){
            temp[i] = a[i];
        }
        a = temp;
    }

    public void push(Item item){
        //将元素加到栈顶
        if(N == a.length) resize(2*a.length);
        a[N++] = item;
    }

    public Item pop(){
        //从栈顶删除元素
        Item item = a[--N];
        a[N] = null;    //避免对象游离
        if (N>0 && N==a.length/4){
            resize(a.length/2);
        }
        return item;
    }

    @Override
    public Iterator<Item> iterator() {
        return new ReverseArrayIterator();
    }

    private class ReverseArrayIterator implements Iterator<Item> {
        private int i = N;

        @Override
        public boolean hasNext() {
            return i>0;
        }

        @Override
        public Item next() {
            return a[--i];
        }

        @Override
        public void remove(){

        }
    }


    public static void main(String[] args) {
        ResizingArrayStack<String> stack = new ResizingArrayStack<>();
        stack.push("A");
        stack.push("B");
        System.out.println("pop的对象是 " + stack.pop());
        stack.push("C");
        stack.push("D");
        Iterator<String> iterator = stack.iterator();
        System.out.println("遍历输出：");
        while (iterator.hasNext()){
            System.out.println(iterator.next());
        }
    }
}
