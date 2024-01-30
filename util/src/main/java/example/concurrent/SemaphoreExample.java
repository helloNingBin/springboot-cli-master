package example.concurrent;

import java.util.concurrent.Semaphore;

/**
 * Semaphore和reentrant一样，它的内部类实现了AQS。
 * 竞争的线程拿到的state>0，就能拿到锁，同时将state减1。即每acquire成功一次，state减1；
 * 这个例子就是，初始的state为0； 所有的竞争线程都会进入到block状态。直到有其它线程去released(n)，即把state加n
 * 就会有n个线程可以去获取锁权限
 */
public class SemaphoreExample {
    private static final int MAX_AVAILABLE = 0;
    private final static Semaphore semaphore = new Semaphore(MAX_AVAILABLE);

    public void doSomething() throws InterruptedException {
        semaphore.acquire();
        try {
            // 临界区代码
            System.out.println("Accessing the shared resource...");
            Thread.sleep(2000); // 模拟对共享资源的操作
        } finally {
            semaphore.release();
        }
    }

    public static void main(String[] args) {
        SemaphoreExample example = new SemaphoreExample();
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                try {
                    example.doSomething();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        }
        semaphore.release(1);
    }
}
