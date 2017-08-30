## Java 反射机制

[TOC]

### 一、前言

Java 的反射机制是一个非常强大的功能。最近在看 Spring 源码，频繁看到反射的身影。因此记录一下以便以后复习使用。

- Java 反射机制 官方文档 [Quick Start](https://docs.oracle.com/javase/tutorial/reflect/)。



### 二、反射基础

#### 1、Java 反射机制定义

> Java 反射机制是在运行状态中，对于任意一个类，都能够知道这个类的所有属性和方法；对于任意一个对象，都能够调用它的任意一个方法；这种动态获取的信息以及动态调用对象的方法的功能称为java语言的反射机制。　

JVM 包括两种编译：

- 静态编译：在编译时确定类型，绑定对象。
- 动态编译：运行时确定类型，绑定对象。动态编译最大限度发挥了java的灵活性，体现了多态的应用，有以降低类之间的藕合性。(反射机制)

Java反射机制主要提供了以下功能：

- 获取一个对象的类信息。
- 获取一个类的访问修饰符、成员、方法、构造方法以及超类的信息。
- 检查获取属于一个接口的常量和方法声明。
- 创建一个直到程序运行期间才知道名字的类的实例。
- 获取并设置一个对象的成员，甚至这个成员的名字是在程序运行期间才知道，
- 检测一个在运行期间才知道名字的对象的方法。



#### 2、理解 Class 和类

获取一个对象对应的反射类`Class`：在Java中有三种方法可以获取一个对象的反射类。

举个栗子：这三种方法输出的都是同一类对象。结果为`java.lang.String`。

- 通过 .class 

  ```java
          Class class1 = String.class;
          System.out.println(class1.getName());
  ```

  ​

- 通过 getClass 方法

  ```java
          Class class2 = "String".getClass();
          System.out.println(class2.getName());
  ```

  ​

- 通过 forName 方法

  ```java
          Class class3 = Class.forName("java.lang.String");
          System.out.println(class3.getName());
  ```

  ​

### 三、Java 反射相关操作

#### 1、API 操作

- 获得成员方法 Method
- 获得成员变量 Field
- 获得构造函数 Constructor

**先定义一个被反射对象 C:**

```java
class C {
    public C(){}
    // 构造函数 Constructor
    private C (String Constructor){
        System.out.println("执行私有构造方法:" + Constructor);
    }
    // 成员变量 Field
    private String priStr = "private string field";
    public String pubStr = "public string";
    protected String proStr = "protected string field";
    String defStr = "default string field";
    // 成员方法 Method
    public void g() { System.out.println("public C.g()"); }
    protected void v () { System.out.println("protected C.v()"); }
    void u() { System.out.println("package C.u()"); }
    private void w() { System.out.println("private C.w()"); }
}
```

注意，这些方法、属性的权限都是不一样的。在 Java 反射机制中，不管是`public`，`default`，`protect`还是`private`方法，通过反射类我们都可以自由调用。

**测试调用：**

```java
    public static void main(String[] args) {
        try {
            Class clazz = Class.forName("com.qg.fangrui.AlgorithmDemo.bsaedata.myreflect.C");
            Object obj = clazz.newInstance();
            System.out.println("=== 获得成员方法 Method ===");
            Method[] methods = clazz.getDeclaredMethods();
            for (Method method : methods){
                method.setAccessible(true);	// 设置 private 可访问，若是访问 public 则不需要
                System.out.println(method.getName());
                method.invoke(obj);
            }
            System.out.println("=== 获得成员变量 Field ===");
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);  // 同理
                System.out.println(field.get(obj));
            }
            System.out.println("=== 获得构造函数 Constructor ===");
          	// Class的newInstance方法，只能创建只包含无参数的构造函数的类
          	// 带参数的构造函数，得用 Class.getDeclaredConstructor(String.class)
            Constructor constructor = clazz.getDeclaredConstructor(String.class);
            constructor.setAccessible(true);
            constructor.newInstance("Constructor");	// 同理
        }  catch (InvocationTargetException| IllegalAccessException |
                ClassNotFoundException | InstantiationException | NoSuchMethodException e) {
            e.printStackTrace();
        }
    }
```

**实验结果：**

```json
=== 获得成员方法 Method ===
u
package C.u()
v
protected C.v()
w
private C.w()
g
public C.g()
=== 获得成员变量 Field ===
private string field
public string
protected string field
default string field
=== 获得构造函数 Constructor ===
执行私有构造方法:Constructor
```



#### 2、利用动态代理实现面向切面编程

利用动态代理技术只能够代理接口。

先创建一个接口以及其实现：

```java
// 代理接口
public interface ClientInterface {
    public void client();
}
// 实际代理对象
public class Client implements ClientInterface {
    public void client(){
        System.out.println("=== 开始逻辑处理 ===");
        try {
            Thread.sleep(2000); // 模拟逻辑处理
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("=== 结束逻辑处理 ===");
    }
}
```

创建代理接口以及实现类

```java
// 代理接口类，模拟 Spring 中的 Advice 接口
public interface Advice {
    public void before(); // 前置代理
    public void after();  // 后置代理
}
// 代理实现类
public class TimeAdvice implements Advice {
    long startTime, endTime;
    @Override
    public void before() {
        startTime = System.currentTimeMillis(); // 记录开始时间
    }
    @Override
    public void after() {
        endTime = System.currentTimeMillis();   // 记录结束时间
        System.out.println("=== 执行时间：" + (endTime - startTime) + " ===");
    }
}
```

客户端调用：

```java
public class Test {
    public static void main(String[] args) {
        SimpleProxy proxy = new SimpleProxy();
        ClientInterface client = (ClientInterface) proxy.bind(new Client(), new TimeAdvice());
        client.client();
    }
}
```



#### 3、利用反射实现泛型擦除

> Java 中集合的泛型，是为了防止错误输入，只在编译阶段有效，绕过了编译到了运行期就无效了。

直接利用一个例子来实现：

```java
public class ReflectGeneric {
    public static void main(String[] args) {
        List list1 = new ArrayList();               // 没有泛型
        List<String> list2 = new ArrayList<>();     // 有泛型

        // 先进行正常的元素添加方式，在编译期对泛型进行检查
        list2.add("hello");
        System.out.println("List2 的长度为 " + list2.size());

        /*
        通过反射来实现泛型擦除：
        1、先获得反射的类；
        2、通过方法反射绕过编译器来调用add方法
         */
        Class clazz1 = list1.getClass();
        Class clazz2 = list2.getClass();
        System.out.println("检查两个类的类型是否相同：" + (clazz1 == clazz2));
        try {
            Method method = clazz2.getMethod("add", Object.class);  // 反射得到add方法
            method.invoke(list2, 20);
            System.out.println("List2 的长度为 " + list2.size());
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}

```

结果：

```json
List2 的长度为 1
检查两个类的类型是否相同：true
List2 的长度为 2
```

综上可知：

在编译期的时候，泛型会限制集合内元素类型保持一致。但是进入运行期以后，泛型就不再起作用了，通过反射调用添加方法，即使是不同类型的元素也可以插入集合。



### 四、参考资料

- [Java反射机制应用实践](https://www.ziwenxie.site/2017/03/22/java-reflection/#利用动态代理实现面向切面编程)