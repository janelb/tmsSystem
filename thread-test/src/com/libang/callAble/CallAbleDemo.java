package com.libang.callAble;

import java.util.concurrent.Callable;

/**
 * @author libang
 * @date 2018/9/5 11:12
 */


public class CallAbleDemo implements Callable<Integer> {

//实现callable接口，是子线程执行完才往下执行
    @Override
    public Integer call() throws Exception {
        int num = 0;
        for (int i = 0; i <= 100; i++) {
            num = num + i;
        }
        System.out.println(num);
        return num;
     /*   for (;;){

            System.out.println("xxx");
        }*/
    }

}
