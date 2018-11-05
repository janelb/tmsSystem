package com.libang.exchanger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Exchanger;

/**
 * @author libang
 * @date 2018/9/6 10:48
 */
public class Test {

    public static void main(String[] args) {

        List<String> buffer1 = new ArrayList<>();
        List<String> buffer2 = new ArrayList<>();

        Exchanger<List<String>> exchanger = new Exchanger<>();

        Producer producerThread = new Producer(buffer1,exchanger);
        Consumer consumerThread = new Consumer(buffer2,exchanger);

        producerThread.start();
        consumerThread.start();

    }
}
