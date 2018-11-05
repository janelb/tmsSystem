package com.libang.threadPool;

/**
 * @author libang
 * @date 2018/9/5 11:33
 */
public class Test {

    public static void main(String[] args) {
        RunableTread runableTread = new RunableTread();
        for (int i = 0; i <= 10; i++) {
            new Thread(runableTread).start();

        }
        System.out.println("end-------");
    }
}
