package com.netty.server;

import com.netty.server.controller.StudentAPI;

import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ReentrantLockTest {
    public static void main(String[] args) {
        AtomicReference<StudentAPI> af = new AtomicReference<>();
        StudentAPI s1 = new StudentAPI(11,22);
        StudentAPI s2 = new StudentAPI(33,44);
        System.out.println(s1 + "==" + s2);
        af.set(s1);
        boolean b = af.compareAndSet(s1, s2);
        System.out.println(b);
        System.out.println(s1 + "==" + s2 + "====" + af.get());
    }
    private static void insertFirstNode(){
        ReentrantLock lock = new ReentrantLock();
        new Thread(()->{
            try {
                lock.unlock();
                Thread.sleep(10000000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
        new Thread(()->{
            try {
                lock.lock();
                Thread.sleep(10000000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }
}
