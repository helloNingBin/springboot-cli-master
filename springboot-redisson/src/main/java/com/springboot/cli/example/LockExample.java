package com.springboot.cli.example;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class LockExample {
    private final RedissonClient redissonClient;
    /**
     * 实现分布式锁
     *  private void lock(long leaseTime, TimeUnit unit, boolean interruptibly) throws InterruptedException {
     *         1.去redis获取锁
     *         Long ttl = tryAcquire(-1, leaseTime, unit, threadId);//如果获得了锁，ttl为null的
     *         if (ttl == null) {
     *             return;
     *         }
     *         2.去订阅锁的状态，释放锁会通知
     *         RFuture<RedissonLockEntry> future = subscribe(threadId);
     *
     *         try {
     *             while (true) {
     *                 3.循环的去获取锁
     *                 ttl = tryAcquire(-1, leaseTime, unit, threadId);
     *                 if (ttl == null) {
     *                     break;
     *                 }
     *                 // waiting for message
     *                 if (ttl >= 0) {4.如果还没有获取到锁就阻塞，阻塞时间为ttl。ttl后或收到订阅通知就再次尝试获取锁
     *                 5.订阅解除阻塞是用semopher来实现的
     *                         future.getNow().getLatch().tryAcquire(ttl, TimeUnit.MILLISECONDS);
     *             }
     *         } finally {
     *             unsubscribe(future, threadId);
     *         }
     *     所以Redission实现的锁，就是每次有锁释放都会去抢。大量的redis请求
     *      我有一种比较简单的方法就是，在本地做一次锁的处理，只有获得本地锁才有机会去抢redis中的锁
     *
     *      下面的Lua脚本是，先判断lockKey是否存在，如果不存在，就创建一个hash表，保存threadId，它的value就是可重入次数
     *                     然后设置lockKey的过期时间，返回null
     *                    后面的线程来抢占锁时，会发现自己的threadId并不存在于hash表中，直接返回剩余过期时间，代表没抢到锁
     *  KEYS[1]：Collections.singletonList(getName()), redis中的key
     *  ARGV[1]：internalLockLeaseTime, hash表中的值
     *  ARGV[2]：getLockName(threadId)  hash表中的key
     *    "if (redis.call('exists', KEYS[1]) == 0) then "不存在时  EXISTS KEY_NAME；1 key存在，0 key不存在
     *          把hkey放到hash表中，并设置值为1.hincrby key hkey(hash表中的key) val(在原来基础上相加)。hash表中没有对应的key，会当成0相加
     *        "redis.call('hincrby', KEYS[1], ARGV[2], 1); " +
     *           设置过期时间。PEXPIRE 跟 EXPIRE 基本一样，只是过期时间单位是毫秒。
     *        "redis.call('pexpire', KEYS[1], ARGV[1]); " +
     *        "return nil; " + 返回 nil 表示成功执行了相应操作。到这里不会向下执行了
     *              "end; " +
     *         如果hash表中有key。1是表示有
     *     "if (redis.call('hexists', KEYS[1], ARGV[2]) == 1) then " +
     *            找到hash表中的值并加1
     *         "redis.call('hincrby', KEYS[1], ARGV[2], 1); " +
     *          "redis.call('pexpire', KEYS[1], ARGV[1]); " +  设置过期时间
     *              "return nil; " +
     *              "end; " +
     *         "return redis.call('pttl', KEYS[1]);",  返回剩余过期时间
     */
    public void lockTest() {
        RLock lock = redissonClient.getLock("productId");
        lock.lock();
        try {
            // 执行业务代码
            log.info("执行业务代码");
        } finally {
            lock.unlock();
        }
    }
}
