package com.libang.exchanger;

import java.util.List;
import java.util.concurrent.Exchanger;
import java.util.concurrent.TimeUnit;

/**
 * @author libang
 * @date 2018/9/6 10:48
 */
public class Producer extends Thread {

    private List<String> buffer;
    private Exchanger<List<String>> exchanger;

    public Producer(List<String> buffer,Exchanger<List<String>> exchanger){
        this.buffer = buffer;
        this.exchanger = exchanger;
    }

    @Override
    public void run(){
        for(int i=1;i<5;i++){
            System.out.println("生产者第"+i+"次提供");
            //TODO
            for(int j=1;j<=3;j++){
                System.out.println("生产者装入"+i+"----"+j);
                buffer.add("buffer"+i+"---"+j);
            }
            System.out.println("生产者装满，等待消费者消费");
            try {
                TimeUnit.SECONDS.sleep(1);
                exchanger.exchange(buffer);
                System.out.println("交换完成");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

}
