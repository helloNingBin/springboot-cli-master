package com.netty.server;

import lombok.SneakyThrows;

import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadPoolExecutorTest {
    public static void main(String[] args) throws InterruptedException {
        boolean gt_5 = false;
        ThreadPoolExecutor executor = new ThreadPoolExecutor(5, 25, 0, TimeUnit.SECONDS,new LinkedBlockingQueue<Runnable>(1000),new ThreadPoolExecutor.DiscardPolicy());
        long sleep = 10;
        for(int i = 0;i < 100;i++){
            Thread.sleep(sleep);
            ((ThreadPoolExecutor) executor).submit(new MyTask(executor,"=>"));
            if(executor.getQueue().size() >5 && !gt_5){
                executor.setCorePoolSize(8);
                gt_5 = true;
                System.out.println("88888888888888");
                sleep  = 32;
            }else if(executor.getQueue().size() <= 3 && gt_5){
                executor.setCorePoolSize(3);
//                sleep = 8;
                gt_5 = false;
                System.out.println("333333333333333333333");
            }
        }
        for(int i = 0;i < 100;i++){
            Thread.sleep(200);
            System.out.println("#>" + Thread.currentThread().getName()+",ActiveCount:" +executor.getActiveCount() +
                    ",PoolSize:" + executor.getPoolSize() + ",CorePoolSize:" + executor.getCorePoolSize() + ",Queue:" + executor.getQueue().size()
            );
        }
        Thread.sleep(9999999);
    }
}
class MyTask implements Runnable{
    ThreadPoolExecutor executor;
    String mark;
    public MyTask(ThreadPoolExecutor executor,String mark){
        this.executor = executor;
        this.mark = mark;
    }
    @SneakyThrows
    @Override
    public void run() {
        System.out.println(mark + Thread.currentThread().getName()+",ActiveCount:" +executor.getActiveCount() +
            ",PoolSize:" + executor.getPoolSize() + ",Queue:" + executor.getQueue().size()
        );
        Thread.sleep(153);
    }
}