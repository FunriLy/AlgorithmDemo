## fail-fast 机制 与 HashMap 迭代器(补充)

[TOC]

### 一、总概

主要是介绍 fail-fast 机制，以及 HashMap 的迭代器实现。

### 二、fail-fast(快速失败)机制

> “快速失败”也就是fail-fast，它是[Java](http://lib.csdn.net/base/java)集合的一种错误检测机制。当多个线程对集合进行结构上的改变的操作时，有可能会产生fail-fast机制。记住是有可能，而不是一定。例如：假设存在两个线程（线程1、线程2），线程1通过Iterator在遍历集合A中的元素，在某个时候线程2修改了集合A的结构（是结构上面的修改，而不是简单的修改集合元素的内容），那么这个时候程序就会抛出 ConcurrentModificationException 异常，从而产生fail-fast机制。

#### 1、举个例子

**fail-fast(快速失败)机制存在于多种集合中，如 ArrayList、HashMap 等。**（该表来源于网络）

| 集合大类 | 具体的集合类                                  |
| ---- | --------------------------------------- |
| List | ArrayList、LinkedList、Vector             |
| Map  | HashMap、LinkedHashMap、TreeMap、HashTable |
| Set  | HashSet、LinkedHashSet、TreeSet           |

```java
public class FailFastTest {
    private static final List<Integer> list = new ArrayList<>();
  	// private static final List<Integer> list = new CopyOnWriteArrayList<>();
    // 线程One负责迭代链表
    private static class threadOne extends Thread {
        public void run() {
            Iterator<Integer> it = list.iterator();
            while (it.hasNext()) {
                int i = it.next();
                System.out.println("ThreadOne 遍历：" + i);
                try {
                    sleep(10);
                } catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
        }
    }
	//线程Two负责修改链表
    private static class threadTwo extends Thread {
        public void run() {
            int i=0;
            while (i<6){
                System.out.println("ThreadTwo run：" + i);
                if (i == 3){
                    list.remove(i);
                }
                i++;
            }
        }
    }
    public static void main(String[] args) {
        for (int i=0; i<20; i++) {
            list.add(i);
        }
        new threadOne().start();
        new threadTwo().start();
    }
}
```

结果：

```json
Exception in thread "Thread-0" java.util.ConcurrentModificationException
	at java.util.ArrayList$Itr.checkForComodification(ArrayList.java:901)
	at java.util.ArrayList$Itr.next(ArrayList.java:851)
	at ……
```



#### 2、fial-fast 产生原因

当线程在对集合进行迭代时，某个线程对该集合在结构上做了修改，这时就会抛出`ConcurrentModificationException`，从而产生 fail-fast。(PS:如果单线程违反了规则，同样也有可能会抛出改异常)

定位到异常处：

```java
            if (modCount != expectedModCount)
                throw new ConcurrentModificationException();
```

就是说，检测modCount != expectedModCount 时刨除异常。

通过源码可知，在构造迭代器时将 modCount 赋值给了 expectedModCount(**全局变量**)，同时在调用迭代操作时都会执行 `modCount++`操作并检查两者是否相等。



#### 3、fail-fast解决办法

为了解决 fail-fast 带来的影响(或者说是确保线程安全的问题)，基本说有两种方法：

- 遍历时的迭代操作加上 synchronized 关键字或者直接使用 Collections.synchronizedXXX() 创建的线程安全的集合。(但是这种方案在进行增删造成的同步锁可能会造成堵塞)

- 采用安全的数据结果，如，使用CopyOnWriteArrayList来替换ArrayList。

  CopyOnWriteArrayList 并没有 fail-fast 机制，它的最大特点在于以下三句代码：

  ```java
                  Object[] newElements = Arrays.copyOf(elements, len);
                  newElements[index] = element;
                  setArray(newElements);
  ```

  我们很容易可以知道，CopyOnWriteArrayList 的核心概念是：任何对于数据结构上有所改变的操作(add、remove、clear 等)，会先 copy 现有的数据，在 copy 的数据上进行修改。这样子就不会影响到原来的数据，只需修改完改变原有的数据的引用即可。**但是这种方法会产生大量的对象，对于数组的 copy 也是相当有损耗的。**

>第二种实现方法又称 **Fail-Safe 机制**。



### 三、HashMap 迭代器

#### 1、重要属性

HashMap 中有一个抽象类 HashIterator，其封装了迭代器内部的一些操作。

```java
    abstract class HashIterator {
        Node<K,V> next;        // next entry to return 下一个节点
        Node<K,V> current;     // current entry	当前节点
        int expectedModCount;  // for fast-fail	期望的修改次数
        int index;             // current slot 当前桶的索引
      	// 省略
    }
```



#### 2、构造方法

```java
        HashIterator() {
            expectedModCount = modCount;
            Node<K,V>[] t = table;
            current = next = null;
            index = 0;
            if (t != null && size > 0) { // table不为空并且大小大于0
              	// 找到table数组中第一个存在的结点，即找到第一个具有元素的桶
                do {} while (index < t.length && (next = t[index++]) == null);
            }	// 注意，这里是 index++，会将索引往下移动
        } 
		/* 所以，next是第一个非空桶的第一个节点 */
```



#### 3、重要方法

* hasNext 方法

  ```java
  	// 是否存在了下一个节点       
  	public final boolean hasNext() {
  		return next != null;
  	}
  ```

  ​

* nextNode 方法

  ```java
          // 获得当前节点，并将指针下移
  		final Node<K,V> nextNode() {
              Node<K,V>[] t;
              Node<K,V> e = next;
              if (modCount != expectedModCount) // 若遍历时对HashMap进行结构性的修改会抛出异常
                  throw new ConcurrentModificationException();
              if (e == null)	// 若下一节点为空则抛出异常
                  throw new NoSuchElementException();
            	// 如果下下一个结点为空，并且table表不为空
            	// 表示桶中所有结点已经遍历完，需寻找下一个不为空的桶
              if ((next = (current = e).next) == null && (t = table) != null) {
                	// 找到下一个不为空的桶
                  do {} while (index < t.length && (next = t[index++]) == null);
              }
              return e;
          }
  ```

  ​

* remove 方法

  ```java
          // 移除当前节点
  		public final void remove() {
              Node<K,V> p = current;
              if (p == null)
                  throw new IllegalStateException();
              if (modCount != expectedModCount)// 若遍历时对HashMap进行结构性的修改会抛出异常
                  throw new ConcurrentModificationException();
              current = null;
              K key = p.key;
              removeNode(hash(key), key, null, false, false);	// 移除节点
              expectedModCount = modCount;	// 赋 fail-fast 新值
          }
  ```

  ​

* KeyIterator、ValueIterator 和 EntryIterator 方法

  方法比较简单，直接调用 nextNode 方法来获取下一个节点

  ```java
      final class KeyIterator extends HashIterator
          implements Iterator<K> {
          public final K next() { return nextNode().key; }
      }
      final class ValueIterator extends HashIterator
          implements Iterator<V> {
          public final V next() { return nextNode().value; }
      }
      final class EntryIterator extends HashIterator
          implements Iterator<Map.Entry<K,V>> {
          public final Map.Entry<K,V> next() { return nextNode(); }
      }
  ```

  ​