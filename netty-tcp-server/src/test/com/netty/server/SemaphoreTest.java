package com.netty.server;

import java.util.concurrent.Semaphore;

public class SemaphoreTest {
    public static void main(String[] args) throws InterruptedException {
        Semaphore sp = new Semaphore(0);
        sp.acquire(0);
        System.out.println(sp);
    }
}
