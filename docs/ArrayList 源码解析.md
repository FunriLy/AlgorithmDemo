## ArrayList 源码解析(基于 JDK 1.8)

[TOC]

### 一、类图

------

![](https://tuchuang001.com/images/2017/08/21/_002.png)



### 二、ArrayList 定义

---------------------

```java
public class ArrayList<E> extends AbstractList<E>
        implements List<E>, RandomAccess, Cloneable, java.io.Serializable{}
```

ArrayList 是一个**数组队列**，相当于**动态数组**，其特点就在于数组容量能够动态增加。

- ArrayList 继承了 AbstractList，实现了 List 接口，提供了相关的添加、删除、修改、遍历等操作。
- ArrayList 实现了 RandomAccess 接口，提供了随机访问的功能。
  - RandomAccess 的目的是为了给 List 提供快速访问的功能。
  - 在 ArrayList 中，可以通过元素的序号快速获取元素对象(快速随机访问)。
- ArrayList 实现了 Cloneable 接口，覆盖了 clone() 方法。
- ArrayList 实现了java.io.Serializable 接口，即 ArrayList 支持序列化传输。
- **ArrayList 操作不是线程安全**。所以，在单线程中一般使用 ArrayList， 在多线程中可以选择 Vector 或者 CopyOnWriteArrayList。


### 三、ArrayList 属性

---------------------------------------------------

```java
    /**
     * The size of the ArrayList (the number of elements it contains).
     */
	// ArrayList 中实际数据的数量
    private int size;

    /**
     * The array buffer into which the elements of the ArrayList are stored.
     * The capacity of the ArrayList is the length of this array buffer. Any
     * empty ArrayList with elementData == DEFAULTCAPACITY_EMPTY_ELEMENTDATA
     * will be expanded to DEFAULT_CAPACITY when the first element is added.
     */
	//保存 ArrayList 中数据的数组
    transient Object[] elementData; 
```

1. size : 记录着动态数组的实际大小。
2. elementData : Object[] 类型的数组， 是一个动态数组。

### 四、ArrayList 构造方法

---------------------------------------------

```java
	// Constructs an empty list with an initial capacity of ten.
	public ArrayList() {
		this.elementData = DEFAULTCAPACITY_EMPTY_ELEMENTDATA;
	}

	public ArrayList(int initialCapacity) {
        if (initialCapacity > 0) {
            this.elementData = new Object[initialCapacity];
        } else if (initialCapacity == 0) {
            this.elementData = EMPTY_ELEMENTDATA;
        } else {
            throw new IllegalArgumentException("Illegal Capacity: "+
                                               initialCapacity);
        }
    }

    public ArrayList(Collection<? extends E> c) {
        elementData = c.toArray();
        if ((size = elementData.length) != 0) {
            // c.toArray might (incorrectly) not return Object[] (see 6260652)
            if (elementData.getClass() != Object[].class)
                elementData = Arrays.copyOf(elementData, size, Object[].class);
        } else {
            // replace with empty array.
            this.elementData = EMPTY_ELEMENTDATA;
        }
    }
```

* 第一个构造方法：调用默认的共享数组实例，一个初始容量为十的空列表。
* 第二个构造方法：根据 initialCapacity 来初始化 elementData 数组的大小。
* 第三个构造方法：将提供的集合转成数组返回给 elementData。
  * 先调用 toArray() 方法来给 elementData 赋值；
  * 如果数组不为0，并且 toArray() 返回不是 Object[]，则调用 Arrays.copyOf() 方法将其转为 Object[]。

### 五、Java API 方法摘要

-----------------------------------

* [Java JDK1.7 在线 API 文档](http://tool.oschina.net/apidocs/apidoc?api=jdk_7u4)

### 六、ArrayList 重要方法解析

---------------------------------

#### 1、增加

```java
    // 添加一个元素
	public boolean add(E e) {
        ensureCapacityInternal(size + 1);  // 进行检查
        elementData[size++] = e;	//将元素添加至List数据尾部，容量+1
        return true;
    }  

	// 在指定位置添加一个元素
	public void add(int index, E element) {
        rangeCheckForAdd(index);	//检查添加范围，判断索引是否越界
        ensureCapacityInternal(size + 1);  // 动态检查容量，是否需要进行扩容
      	// 对数组进行复制处理，目的就是空出index的位置插入element，并将index后的元素位移一个位置
        System.arraycopy(elementData, index, elementData, index + 1,
                         size - index);
        elementData[index] = element; // 将指定的index位置赋值为element
        size++;	//容量+1
    }

	// 添加一个集合
    public boolean addAll(Collection<? extends E> c) {
        Object[] a = c.toArray(); //转化为数组，并一定是 Object[] 数组
        int numNew = a.length;
        ensureCapacityInternal(size + numNew);  // 动态检查容量
      	// 将整个数组复制到List数据尾部
        System.arraycopy(a, 0, elementData, size, numNew);
        size += numNew;	// 容量+数组长度
        return numNew != 0;
    }

	// 在指定位置添加一个集合
    public boolean addAll(int index, Collection<? extends E> c) {
        rangeCheckForAdd(index);	//检查添加范围，判断索引是否越界
        Object[] a = c.toArray();	//转化为数组
        int numNew = a.length;
        ensureCapacityInternal(size + numNew);  // Increments modCount
        int numMoved = size - index;	// 计算需要移动的长度
      	// 数组复制，空出第index到index+numNum的位置，即将数组index后的元素向右移动numNum个位置
        if (numMoved > 0)
            System.arraycopy(elementData, index, elementData, index + numNew,
                             numMoved);
      	// 将要插入的集合元素复制到数组空出的位置中
        System.arraycopy(a, 0, elementData, index, numNew);
        size += numNew;
        return numNew != 0;
    }

	//检查添加范围，判断索引是否越界
    private void rangeCheckForAdd(int index) {
        if (index > size || index < 0)
            throw new IndexOutOfBoundsException(outOfBoundsMsg(index));
    }

	// 数组容量检查，不够时进行扩容
    private void ensureCapacityInternal(int minCapacity) {
      	// 如果是空数组
        if (elementData == DEFAULTCAPACITY_EMPTY_ELEMENTDATA) {
            minCapacity = Math.max(DEFAULT_CAPACITY, minCapacity);
        }
        ensureExplicitCapacity(minCapacity);
    }
	// 检查是否需要扩容
    private void ensureExplicitCapacity(int minCapacity) {
        modCount++;	// 记录结构被修改的次数
        // overflow-conscious code
        if (minCapacity - elementData.length > 0)
            grow(minCapacity);
    }
	// 扩容操作
    private void grow(int minCapacity) {
        // overflow-conscious code
        int oldCapacity = elementData.length;	//当前数组长度
      	// 加上0.5倍容量：>> 右移运算符,相当于除以2
        int newCapacity = oldCapacity + (oldCapacity >> 1);	
      	// 如果新扩容的数组长度还是比最小需要的容量小，则以最小需要的容量为长度进行扩容
        if (newCapacity - minCapacity < 0)
            newCapacity = minCapacity;
      	// 如果新扩容的数组长度比数组最大容量还要大，重新检查扩容最小标准，并将结果返回给新扩容数组长度
        if (newCapacity - MAX_ARRAY_SIZE > 0)
            newCapacity = hugeCapacity(minCapacity);
        // minCapacity is usually close to size, so this is a win:
       	// 进行数据拷贝，Arrays.copyOf底层实现是System.arrayCopy()
        elementData = Arrays.copyOf(elementData, newCapacity);
    }
```

#### 2、删除

```java
    // 根据索引位置删除元素
	public E remove(int index) {
        rangeCheck(index);	// 数组越界检查
        modCount++;
        E oldValue = elementData(index);	// 取出要删除的元素
        int numMoved = size - index - 1;
      	// 数组复制，将index之后的元素前移一个位置
        if (numMoved > 0)
            System.arraycopy(elementData, index+1, elementData, index, numMoved);
      // 将数组最后一个元素置空并数量-1  
      elementData[--size] = null; // clear to let GC do its work
        return oldValue;
    }

	// 根据元素内容删除，只删除匹配的第一个
    public boolean remove(Object o) {
      // 对要删除的元素进行null判断
        if (o == null) {	// 因为 null 需要使用 == 来比较
            for (int index = 0; index < size; index++)
                if (elementData[index] == null) {
                    fastRemove(index);
                    return true;
                }
        } else {	// 非 null 需要使用 equals 来比较
            for (int index = 0; index < size; index++)
                if (o.equals(elementData[index])) {
                    fastRemove(index);
                    return true;
                }
        }
        return false;
    }

	// 私有移除方法，跳过边界检查
    private void fastRemove(int index) {
        modCount++;
        int numMoved = size - index - 1;
        if (numMoved > 0)
            System.arraycopy(elementData, index+1, elementData, index,
                             numMoved);
        elementData[--size] = null; // clear to let GC do its work
    }

	// 边界检查
    private void rangeCheck(int index) {
        if (index >= size)
            throw new IndexOutOfBoundsException(outOfBoundsMsg(index));
    }
```



#### 3、更新

```java
	// 将指定元素更新为新元素
  	public E set(int index, E element) {
        rangeCheck(index);	// 数组越界检查
        E oldValue = elementData(index);	// 获取旧元素
        elementData[index] = element;		// 更新为新元素
        return oldValue;
    }
```



#### 4、 查找

```java
    // 查找指定位置上的元素
	public E get(int index) {
        rangeCheck(index);
        return elementData(index);
    }
```



#### 5、是否包含

```java
	// 判断是否包括一个元素
	public boolean contains(Object o) {
        return indexOf(o) >= 0;
    }
	// 从头到尾遍历查找一个元素，同样区分 null 与非 null
    public int indexOf(Object o) {
        if (o == null) {
            for (int i = 0; i < size; i++)
                if (elementData[i]==null)
                    return i;
        } else {
            for (int i = 0; i < size; i++)
                if (o.equals(elementData[i]))
                    return i;
        }
        return -1;
    }
	//从头到尾遍历查找一个元素
    public int lastIndexOf(Object o) {
        if (o == null) {
            for (int i = size-1; i >= 0; i--)
                if (elementData[i]==null)
                    return i;
        } else {
            for (int i = size-1; i >= 0; i--)
                if (o.equals(elementData[i]))
                    return i;
        }
        return -1;
    }
```



#### 6、其他

综上，ArrayList 两个最大消耗的操作：

1. 数组扩容
2. 数组复制

在最坏的情况下，时间复杂度可以达到 O(n)。



**数组扩容：**

![](https://tuchuang001.com/images/2017/08/22/1637925-95f433d97c577f33.jpg)

**数组复制和新增元素：**

![](https://tuchuang001.com/images/2017/08/22/903361a421fb855.jpg)

> 需要注意的是，数组扩容伴随着开辟新建的内存空间以创建新数组然后进行数据复制，而数组复制不需要开辟新内存空间，只需将数据进行复制。

**当删除大量元素后，会空出大量空间。**所以 ArrayList 提供了一个释放空间的方法：

```java
    // 将容量空间调整为当前实际元素数量大小，从而释放空间
	public void trimToSize() {
        modCount++;
        if (size < elementData.length) {
            elementData = (size == 0)
              ? EMPTY_ELEMENTDATA
              : Arrays.copyOf(elementData, size);
        }
    }
```



#### 7、 总结

1. ArrayList 是一个动态扩展的 Object[] 数组，其默认容量大小为10。
2. 当 ArrayList 扩容时，会增长到原来的1.5倍，**新容量=旧容量+旧容量>>2**。
3. ArrayList 的 Clone 函数，就是将全部元素克隆到一个数组。
4. ArrayList实现java.io.Serializable的方式。
   * 当写入到输出流时，先写入“容量”，再依次写入“每一个元素”；
   * 当读出输入流时，先读取“容量”，再依次读取“每一个元素”。



### 七、ArrayList 遍历方式

------------------------

**ArrayList 支持三种遍历方式。**

#### 1、通过迭代器(Iterator)来遍历

```java
	Object value = null;
	Iterator it = list.Iterator();
	while(it.hasNext()){
 		value = (Object) it.next();
	}
```

#### 2、通过索引值遍历

```java
	Object value = null;
	int size = list.size();
	for(int i=0; i<size; i++){
 		value = (Object) list.get(i);
	}
```

#### 3、foreach循环遍历

```java
	object value = null;
	for(Object obj : list){
    	value = obj;
	}
```

#### 4、三种遍历方法效率实验

##### （1）实验过程：

**实验代码：**

```java
public class MyArrayList {
    public static void main(String[] args) {
        List<Integer> list = new ArrayList<>();
        for (int i=0; i<5000000; i++){
            list.add(i);
        }
        System.out.println("当前list数组长度为 " + list.size());
        iteratorThroughIterator(list);
        iteratorThroughRandomAccess(list);
        iteratorThroughForForeach(list);
    }
    // 通过迭代器(Iterator)来遍历
    public static void iteratorThroughIterator(List list) {
        Integer value = 0;
        long startTime=0, endTime=0;
        startTime = System.currentTimeMillis();
        for(Iterator iter = list.iterator(); iter.hasNext(); ) {
            value = (Integer) iter.next();
        }
        endTime = System.currentTimeMillis();
        long interval = endTime - startTime;
        System.out.println("iteratorThroughIterator：" + interval+" ms");
    }
    // 通过索引值遍历
    public static void iteratorThroughRandomAccess(List list) {
        int value = 0;
        long startTime=0, endTime=0;
        startTime = System.currentTimeMillis();
        for (int i=0; i<list.size(); i++) {
            value = (Integer) list.get(i);
        }
        endTime = System.currentTimeMillis();
        long interval = endTime - startTime;
        System.out.println("iteratorThroughRandomAccess：" + interval+" ms");
    }
    // foreach循环遍历
    public static void iteratorThroughForForeach(List list) {
        Integer value = 0;
        long startTime=0, endTime=0;
        startTime = System.currentTimeMillis();
        for(Object obj:list) {
            value = (Integer)obj;
        }
        endTime = System.currentTimeMillis();
        long interval = endTime - startTime;
        System.out.println("iteratorThroughForForeach：" + interval+" ms");
    }
}

```

**实验结果：**

```json
	// 第一次
	当前list数组长度为 100000
	iteratorThroughIterator：12 ms
	iteratorThroughRandomAccess：10 ms
	iteratorThroughForForeach：14 ms
	// 第二次
	当前list数组长度为 100000
	iteratorThroughIterator：10 ms
	iteratorThroughRandomAccess：10 ms
	iteratorThroughForForeach：9 ms
	// 第三次
	当前list数组长度为 1000000
	iteratorThroughIterator：17 ms
	iteratorThroughRandomAccess：11 ms
	iteratorThroughForForeach：13 ms
	// 第四次
	当前list数组长度为 5000000
	iteratorThroughIterator：47 ms
	iteratorThroughRandomAccess：32 ms
	iteratorThroughForForeach：41 ms
```

##### （2）实验结论及分析

**结论：**

> 从实验结果来看，RandomAccess 遍历方法效率最高；在数量较小的情况下，Iterator 和 Foreach 的效率较为暧昧；随着数量增加，Iterator 的效率要高于 Foreach。

* RandomAccess 遍历方法效率最高。

  ArrayList 实现 RandomAccess 接口。查看注释可以看到一段有趣的解释

  ```json
  As a rule of thumb, a List implementation should implement this interface if, for typical instances of the class, this loop:
   * <pre>
   *     for (int i=0, n=list.size(); i &lt; n; i++)
   *         list.get(i);
   * </pre>
   * runs faster than this loop:
   * <pre>
   *     for (Iterator i=list.iterator(); i.hasNext(); )
   *         i.next();
   * </pre>
  ```

  ​

* Iterator 的效率要高于 Foreach。

  通过查找资料和调试 Froeach，可以发现其归根到底还是调用 Iterator 来执行，同时也附带了一些其他辅助性操作(如，赋值等)，所以 Iterator 的效率要高于 Foreach 也就不为出奇。

  Foreach 语法糖经过编译器处理成了Iterator的遍历，有关foreach语法糖的细节可以参考《[Java语法糖之foreach](http://blog.csdn.net/u013256816/article/details/50736498)》。

  **Foreach 的关键字是 for，其语句是由 Iterator 来实现的。**