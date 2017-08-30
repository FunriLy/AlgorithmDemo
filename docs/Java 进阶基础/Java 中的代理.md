## Java 中的代理

[TOC]

### 一、前言

-------------------------------

代理模式是一种很常见的设计模式，尤其在 Spring 等框架中广泛应用。在 Java 中，一般有三种方法实现代理：

- 静态代理
- JDK 实现动态代理
- CGLIB 实现动态代理

首先，**什么是代理？**

> 在某些情况下，我们不希望或者不能直接访问对象A，而是通过访问一个中介对象B，再由B去访问A来达到目的，这种方式就叫做代理。

代理的优点：

- 隐藏委托的实现(显而易见)。
- 解耦，不改变委托类代码情况下，做一些额外的处理，比如添加初始判断等。



### 二、静态代理

----------------------

静态代理就是代理类在程序运行前已经存在的代理方式。

举个栗子：Class B 中有个 Class A 的实例，通过调用 B 的方法就可以调用 A 的方法，所以说 **A 是被代理类(委托类)，B 是代理类**。

下面实现一个静态代理demo：

1. 定义一个接口 Target 并实现该接口：

   ```java&#39;
   public interface Target {
       public String execute();
   }
   // 被代理类
   public class TargetImpl implements Target {
       @Override
       public String execute() {
           System.out.println("TargetImpl execute！");
           return "execute";
       }
   }
   ```

2. 创建一个代理类：

   ```java
   // 代理类
   public class proxy implements Target {
       private Target target;
       public proxy(Target target) {
           this.target = target;
       }

       @Override
       public String execute() {
           before();
           String result = this.target.execute();
           after();
           return result;
       }

       private void before(){
           System.out.println("before");
       }

       private void after(){
           System.out.println("after");
       }
   }
   ```

3. 创建一个测试类：

   ```java
   public class ProxyTest {
       public static void main(String[] args) {
           Target target = new TargetImpl();
           proxy proxy = new proxy(target);
           System.out.println(proxy.execute());
       }
   }
   ```

4. 测试结果：

   ```json
   before
   TargetImpl execute！
   after
   execute
   ```



静态代理实现简单易懂，但需要针对被代理的方法提前写好代理类。如果需要代理的对象非常多的情况下，会增加许多重复的代码工作。



### 三、JDK 实现动态代理

----------------------------------

代理类在程序运行前不存在、运行时由程序动态生成的代理方式称为动态代理。

JDK 实现动态代理主要是通过反射机制(`java.lang.reflect.Proxy`)，在运行时动态生成代理类。这种方法方便对代理函数做统一或特殊处理，如记录所有函数的执行时间。

```java
// JDK 代理类
public class JDKDynamicProxyHandler implements InvocationHandler {

    private Target target;

    public JDKDynamicProxyHandler(Target target) {
        this.target = target;
    }
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        before();
        Object result = method.invoke(target, args);
        after();
        return result;
    }
    private void before(){
        System.out.println("=== before ===");
    }
    private void after(){
        System.out.println("=== after ===");
    }
}
```

测试类：

```java
public class JDKDynamicProxyTest {
    public static void main(String[] args) {
        // 这里调用了静态代理的被代理对象
        Target target = new TargetImpl();
        JDKDynamicProxyHandler handler = new JDKDynamicProxyHandler(target);
        Target proxySubject = (Target) Proxy.newProxyInstance(TargetImpl.class.getClassLoader(),
                TargetImpl.class.getInterfaces(),handler);
        String result = proxySubject.execute();
        System.out.println(result);
    }
}
```

结果：

```json
=== before ===
TargetImpl execute！
=== after ===
execute
```

但是，无论是 JDK 实现动态代理还是静态带领，都需要定义接口，然后才能实现代理功能。这同样存在局限性。



### 四、CGLIB实现动态代理

-----------------------------------

CGLIB 采用了底层的字节码技术，其原理是通过字节码技术为一个类创建子类，并在子类中采用方法拦截的技术拦截所有父类方法的调用，顺势织入横切逻辑。

```java
public class CglibProxy implements MethodInterceptor {
    private Object target;
    public Object getProxyInstance(Object target) {
        this.target = target;
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(this.target.getClass());
        enhancer.setCallback(this);  // call back method
        return enhancer.create();  // create proxy instance
    }

    @Override
    public Object intercept(Object target, Method method, Object[] args, MethodProxy proxy) throws Throwable {
        before();
        Object result = proxy.invokeSuper(target, args);
        after();
        return result;
    }
    private void before() {
        System.out.println("=== before ===");
    }
    private void after() {
        System.out.println("=== after ===");
    }
}
```

调用测试类：

```java
public class CglibTest {
    public static void main(String[] args) {
        CglibProxy proxy = new CglibProxy();
        Target hello = (Target) proxy.getProxyInstance(new TargetImpl());
        String result = hello.execute();
        System.out.println(result);
    }
}
```

结果：

```json
=== before ===
TargetImpl execute！
=== after ===
execute
```

代理对象的生成过程由Enhancer类实现，大概步骤如下：

1. 生成代理类Class的二进制字节码；
2. 通过Class.forName加载二进制字节码，生成Class对象；
3. 通过反射机制获取实例构造，并初始化代理类对象。



### 五、Spring AOP 原理

-----------------------------------

两种动态代理的区别：

- Java JDK动态代理是利用反射机制生成一个实现代理接口的匿名类，在调用具体方法前调用 InvokeHandler 来处理。
- 而 CGLIB 动态代理是利用 asm 开源包，对代理对象类的class文件加载进来，通过修改其字节码生成子类来处理。

JDK动态代理与CGLIB动态代理均是实现Spring AOP的基础。

**Spring AOP 动态代理策略：**(来源于《Spring 源码深度解析》)

- 如果目标对象实现了接口，默认情况下会采用 JDK 动态代理实现 AOP。
- 如果目标对象实现了接口，可以强制使用 CGLIB 实现 AOP。
- 如果目标对象没有实现接口，必须采用 CGLIB 库，Spring 会自动在 JDK 动态代理和 CGLIB 之间转换。



### 六、参考资料

------------------------------

- 来源于网上资源