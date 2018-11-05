package com.libang.condition;


import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author libang
 * @date 2018/9/5 12:17
 * 编写一个程序，开启三个线程，这三个线程的id分别为A，B，C，要求输出ABCABCABC..,10次
 */
public class ConditionTest {

    public static void main(String[] args) {
        ConditionDemo conditionDemo = new ConditionDemo();
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i <= 10; i++) {
                    conditionDemo.loopA();

                }
            }
        }, "A").start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i <= 10; i++) {
                    conditionDemo.loopB();
                }
            }
        }, "B").start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i <= 10; i++) {
                    conditionDemo.loopC();
                }
            }
        }, "C").start();

    }
}

class ConditionDemo {

    private int number = 1;
    Lock lock = new ReentrantLock();
    Condition condition1 = lock.newCondition();
    Condition condition2 = lock.newCondition();
    Condition condition3 = lock.newCondition();

    public void loopA() {
        lock.lock();
        try {
            if (number != 1) {
                condition1.await();
            }
            System.out.print(Thread.currentThread().getName());
            number = 2;
            condition2.signal();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }


    public void loopB() {
        lock.lock();
        try {
            if (number != 2) {
                condition2.await();
            }
            System.out.print(Thread.currentThread().getName());
            number = 3;
            condition3.signal();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }

    }

    public void loopC() {
        lock.lock();
        try {
            if (number != 3) {
                condition3.await();
            }
            System.out.print(Thread.currentThread().getName());
            number = 1;
            condition1.signal();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }

    }
}
