package com.qg.fangrui.AlgorithmDemo.algorithm;

/**
 * Created by funrily on 17-8-11
 * 哲学家进餐问题
 * @version 1.0.0
 */
public class Philosopher extends Thread{

    private String name;
    private Fork fork;
    public Philosopher(String name,Fork fork){
        super(name);
        this.name=name;
        this.fork=fork;
    }

    public void run(){
        while(true){
            thinking();
            fork.takeFork();
            eating();
            fork.putFork();
        }

    }

    public void eating(){
        System.out.println("I am Eating:"+name);
        try {
            sleep(1000);//模拟吃饭，占用一段时间资源
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public void thinking(){
        System.out.println("I am Thinking:"+name);
        try {
            sleep(10);//模拟思考
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class Fork{
    /*
    5只筷子，初始为都未被用
     */
    private boolean[] used={false,false,false,false,false,false};

    // TODO: 17-8-11 semaphore 信号量的使用

    /*
    只有当左右手的筷子都未被使用时，才允许获取筷子，且必须同时获取左右手筷子
     */
    public synchronized void takeFork(){
        String name = Thread.currentThread().getName();
        int i = Integer.parseInt(name);
        while(used[i]||used[(i+1)%5]){
            try {
                wait();//如果左右手有一只正被使用，等待
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        used[i]= true;      //筷子已经被占用
        used[(i+1)%5]=true;
    }

    /*
    必须同时释放左右手的筷子
     */
    public synchronized void putFork(){
        String name = Thread.currentThread().getName();
        int i = Integer.parseInt(name);

        used[i]= false;     //解放筷子
        used[(i+1)%5]=false;
        notifyAll();//唤醒其他线程
    }

}
