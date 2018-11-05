package com.libang.callAble;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * @author libang
 * @date 2018/9/5 11:15
 */
public class Test {
    public static void main(String[] args) {

        CallAbleDemo callAbleDemo = new CallAbleDemo();
        //借助FutureTask支持来执行callAble
        FutureTask<Integer> futureTask = new FutureTask<>(callAbleDemo);

        for (int i = 0; i <= 10; i++) {
            new Thread(futureTask).start();
        }


        try {
            Integer num = futureTask.get();
            System.out.println(num);
            System.out.println("end-----------------");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

    }
}
