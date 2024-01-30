package com.exmaple;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;

import java.util.concurrent.TimeUnit;

public class MyCuratorFrameworkFactory {

    /**
     * 尚硅谷的方法
     * @return
     */
    public static CuratorFramework getCuratorFramework_sgg(){
        ExponentialBackoffRetry policy = new ExponentialBackoffRetry(3000, 3);
        CuratorFramework client = CuratorFrameworkFactory.builder()
                .connectString(ZookeeperFacotry.connectionString).connectionTimeoutMs(2000)
                .sessionTimeoutMs(2000).retryPolicy(policy).build();
        client.start();
        return client;
    }
    public void lock_test_sgg() throws Exception {
        InterProcessMutex lock = new InterProcessMutex(getCuratorFramework_sgg(), "locks");
        lock.acquire();
        lock.acquire(34, TimeUnit.SECONDS);
    }
}




















