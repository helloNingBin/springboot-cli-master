package com.netty.server;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ThreadTest {
    public static void main(String[] args) throws Exception {
        wait_notify();
    }
    public static void wait_notify() throws InterruptedException {
        Object obj = new Object();
        Thread t1 = new Thread(()->{
            try {
                synchronized (obj){
                    obj.wait();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        t1.start();
        Thread t2 = new Thread(()->{
            try {
                synchronized (obj){
                    Thread.sleep(3000);
                    obj.notify();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        t2.start();
        t1.join();
    }

    private static void join()throws Exception{
        Thread t1 = new Thread(()->{
            try {
                synch();
                Thread.sleep(10000);
                log.info("sleep end........");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        t1.start();
        Thread t2 = new Thread(()->{
            try {
                synch();
                Thread.sleep(3000);
                t1.interrupt();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        t2.start();
        System.out.println("join........");
        t1.join();
    }
    private synchronized static void synch(){
        log.info(Thread.currentThread().getName());
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
