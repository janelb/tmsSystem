package com.libang.semaphoreTest;

import java.util.concurrent.Semaphore;

/**
 * @author libang
 * @date 2018/9/6 14:43
 */
public class SemaphoreTest {
    public static void main(String[] args) {

       int N =8;
       Semaphore semaphore = new Semaphore(1);
       for(int i=1;i<N;i++){
                new Worker(i,semaphore).start();
       }

    }

}

class Worker extends  Thread{

    private int num ;
    private Semaphore semaphore;

    public Worker(int num ,Semaphore semaphore){
                this.num = num;
                this.semaphore=semaphore;
    }

    @Override
    public void run(){

    }


}
