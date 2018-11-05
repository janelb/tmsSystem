package com.libang.volatileDemo;

/**
 * @author libang
 * @date 2018/9/6 10:31
 * volatile是关键字
 * 不具有原子性
 */
public class Test {

    public static void main(String[] args) {
        VolatileDemo volatileDemo = new VolatileDemo();

        new Thread(volatileDemo).start();

        while (true) {
            if (volatileDemo.isFlag()) {
                System.out.println("stop...");
                break;
            }
        }
    }
}
