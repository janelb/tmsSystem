package com.libang.exchanger;

import java.util.List;
import java.util.concurrent.Exchanger;
import java.util.concurrent.TimeUnit;

/**
 * @author libang
 * @date 2018/9/6 10:48
 */
public class Consumer extends Thread{
    //定义消费者和生产者的交换的数结构

    private List<String> buffer;

    //生产者和消费交换对象

    private Exchanger<List<String>> exchanger;

    //定义构造方法

    public Consumer(List<String> buffer,Exchanger<List<String>> exchanger){
        this.buffer = buffer;
        this.exchanger = exchanger;
    }


    @Override
    public void run(){
        for(int i=1;i<5;i++){
            try {
                TimeUnit.SECONDS.sleep(1);
                buffer = exchanger.exchange(buffer);
                System.out.println("消费者交换完成....");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("消费者第"+i+"次提取");
            //取走之后进行移除
            for(int j = 1;j<=3;j++){
                System.out.println("消费者 :"+buffer.get(0));
                //移除第一个元素
                buffer.remove(0);
            }


        }

    }


}
