package com.libang.volatileDemo;



/**
 * @author libang
 * @date 2018/9/6 10:31
 */
public class VolatileDemo implements Runnable {

    //使用volatile使变量成为可见的

    private volatile boolean flag=false;

    @Override
    public void run() {

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        flag=true;
        System.out.println("flag change to true");
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }
}
