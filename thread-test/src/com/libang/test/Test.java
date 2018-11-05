package com.libang.test;

/**
 * @author libang
 * @date 2018/9/4 11:21
 */
public class Test {

    public static void main(String[] args) {

        Thread t1 = new Thread(new Runnable() {  //匿名局部内部类
            @Override
            public void run() {

                for(int i=0;i<100;i++){
                    System.out.println("t1---->"+i);
                }
            }
        });

        t1.start();
        for (int i=0;i<100;i++){

            System.out.println("main---->"+i);
        }

        //如果直接调用子类中的run方法那么代码会顺序进行执行，没有创建线程，只是调用了run方法
        // 当调用start方法后创建了子线程，才能进行同步


    }

}
