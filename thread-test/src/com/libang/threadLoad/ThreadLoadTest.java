package com.libang.threadLoad;

import java.util.Random;

/**
 * @author libang
 * @date 2018/9/5 17:49
 */
public class ThreadLoadTest {

    private static ThreadLocal<Integer> threadLocal = new ThreadLocal<Integer>();

    public static void main(String[] args) {

        for(int i =0;i<=5;i++){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    int data =new  Random().nextInt(100);
                    System.out.println(Thread.currentThread().getName()+"put data:"+data);
                    threadLocal.set(data);

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    int res =  threadLocal.get();
                    System.out.println(Thread.currentThread().getName()+"get data:"+res);
                }
            }).start();
        }
    }
}
