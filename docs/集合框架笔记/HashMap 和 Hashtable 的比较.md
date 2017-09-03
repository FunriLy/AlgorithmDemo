## HashMap 和 Hashtable 的比较

[TOC]

### 一、前言

最近完成了 HashMap、Hashtable、HashSet 和 ConcurrentHashMap 几个 hashCode 集合的源码，感觉他们还是挺像的就拿他们几个做了一下比较。

> 这里所有的集合都来自于 JDK 1.8 。



### 二、HashMap 和 Hashtable 的区别

我们都知道 HashMap 和 Hashtablle 都实现了 Map 接口，他们之间的最大区别就是 是否具有线程安全性 的问题。其实两者的区别还是挺大的：

- 首先，从可存元素来说，HashMap 几乎可以等价于 Hashtable，HashMap  可以存入 null 值，而 Hashtable 则不行。

- 从共享资源来说，​Hashtable 的基本操作是 synchronized 修饰的，这意味着 Hashtable 是线程安全的，多个线程可以安全共享同一个 Hashtable。而如果没有正确同步 HashMap，多个线程是不能共享同一个 HashMap 的。

- 遍历方面，HashMap 采用的是 fail-fast 机制的 Iterator 迭代器；Hashtable 采用了enumerator 枚举器。两者的最大区别就是，enumerator 枚举器没有 remove 方法。两者接口如下：

  ```java
  public interface Enumeration<E> {
      boolean hasMoreElements();
      E nextElement();
  }
  public interface Iterator<E> {
      boolean hasNext();
      E next();
      void remove();
  }
  ```

- 从数据结构方面来说，在 JDK 1.7 中，两者都是由 链表+数组 来实现的。但是在 JDK 1.8 中，Hashtable 没有变动，HashMap 增加一个阀值，超过阀值将链表拆为红黑树，所以HashMap 是由 链表+数组+红黑树 构成的。

- 速率方面，因为 Hashtable 是由关键字 synchronized 修饰的，所以在单线程中它的速率肯定比 HashMap 低。

- HashMap 设计上是线程不安全的，但利用 `Collections.synchronizeMap(new HashMap)`可以实现线程安全。




关于 HashMap 线程不安全这一点，《Java并发编程的艺术》一书中是这样说的：

> HashMap 在并发执行 put 操作时会引起死循环，导致 CPU 利用率接近 100%。因为多线程会导致 HashMap 的 Node 链表形成环形数据结构，一旦形成环形数据结构，Node 的 next 节点永远不为空，就会在获取 Node 时产生死循环。

相关原因：

- [HashMap在java并发中如何发生死循环](http://firezhfox.iteye.com/blog/2241043)




### 三、一个有趣的问答(转，很重要)

这个问答转载自：http://www.54tianzhisheng.cn/2017/06/10/HashMap-Hashtable/

- Q：**什么是HashMap？你为什么用到它？**

  A：……………………(这个问题很简单)

- Q：**你知道HashMap的工作原理吗，比如get()方法？**

  A：HashMap是基于hashing的原理，我们使用put(key, value)存储对象到HashMap中，使用get(key)从HashMap中获取对象。当我们给put()方法传递键和值时，我们先对键调用hashCode()方法，返回的hashCode用于找到bucket位置来储存Entry对象。

- Q：**当两个键的hashCode值相同，你是如何获取value值？**

  A：因为hashcode相同，所以它们的bucket位置相同，‘碰撞’会发生。因为HashMap是采用链表/红黑树来存储对象，通过调用 queals() 方法来遍历找到对应的对象的value值。

- Q：**用什么做键值比较好？**

  A：采用不可变、声明为final的对象，不可变性使得能够缓存不同键的hashcode，这将提高整个获取对象的速度，使用String，Interger这样的wrapper类作为键是非常好的选择。

  - Q：**为什么String, Interger这样的wrapper类适合作为键？**

    A：因为String是不可变的，也是final的，而且已经重写了equals()和hashCode()方法了。其他的wrapper类也有这个特点。不可变性是必要的，因为为了要计算hashCode()，就要防止键值改变，如果键值在放入时和获取时返回不同的hashcode的话，那么就不能从HashMap中找到你想要的对象。不可变性还有其他的优点如线程安全。

    如果你可以仅仅通过将某个field声明成final就能保证hashCode是不变的，那么请这么做吧。因为获取对象的时候要用到equals()和hashCode()方法，那么键对象正确的重写这两个方法是非常重要的。如果两个不相等的对象返回不同的hashcode的话，那么碰撞的几率就会小些，这样就能提高HashMap的性能。

  - Q：**我们可以使用自定义的对象作为键吗？**

    A：可以使用任何对象作为键，只要它遵守了equals()和hashCode()方法的定义规则，并且当对象插入到Map中之后将不会再改变了。如果这个自定义对象时不可变的，那么它已经满足了作为键的条件，因为当它创建之后就已经不能改变了。

- Q：**如果HashMap的大小超过了负载因子(load factor)定义的容量，怎么办？**

  A：默认的负载因子大小为0.75，也就是说，当一个map填满了75%的bucket时候，和其它集合类(如ArrayList等)一样，将会创建原来HashMap大小的两倍的bucket数组，来重新调整map的大小，并将原来的对象放入新的bucket数组中。这个过程叫作rehashing，因为它调用hash方法找到新的bucket位置。

- Q：**重新调整HashMap大小会发生什么问题？**

  A：在多线程下，会产生条件竞争。当重新调整HashMap大小的时候，确实存在条件竞争，因为如果两个线程都发现HashMap需要重新调整大小了，它们会同时试着调整大小。在调整大小的过程中，存储在链表中的元素的次序会反过来，因为移动到新的bucket位置的时候，HashMap并不会将元素放在链表的尾部，而是放在头部，这是为了避免尾部遍历(tail traversing)。如果条件竞争发生了，那么就死循环了。

- Q：**可以使用CocurrentHashMap来代替Hashtable吗？**

  A：ConcurrentHashMap同步性能比 Hashtable 更好，因为它仅仅根据同步级别对map的一部分进行上锁。ConcurrentHashMap 当然可以代替 HashTable，但是 HashTable 提供更强的线程安全性。但是一般情况下，的确可以使用 ConcurrentHashMap 来代替 Hashtable 确保速率。

