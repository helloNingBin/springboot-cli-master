package com.example.final_augues.nio.reactor.main;

import com.example.final_augues.nio.reactor.single.Handler;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.concurrent.atomic.AtomicInteger;

public class MainAcceptor implements Runnable {

    private ServerSocketChannel serverSocketChannel;

    //    private AtomicInteger index = new AtomicInteger();
    private int index = 0;
    Selector[] selectors = new Selector[Runtime.getRuntime().availableProcessors() * 2];
    SubReactor[] subReactors = new SubReactor[Runtime.getRuntime().availableProcessors() * 2];

    Thread[] threads = new Thread[Runtime.getRuntime().availableProcessors() * 2];

    public MainAcceptor(ServerSocketChannel serverSocketChannel) throws IOException {

        // 初始化服务员==》 SubReactor==>selector
        // SubReactor会被线程启动
        this.serverSocketChannel = serverSocketChannel;
        for (int i = 0; i < Runtime.getRuntime().availableProcessors() * 2; i++) {
            // 每一个服务员都有个铃铛
            selectors[i] = Selector.open();
            subReactors[i] = new SubReactor(selectors[i]);
            threads[i] = new Thread(subReactors[i]);
            // 每一个服务员都像线程一样启动
            threads[i].start();
        }
    }

    @Override
    public void run() {
        try {
            SocketChannel socketChannel = serverSocketChannel.accept();
            socketChannel.configureBlocking(false);

            // 唤醒阻塞的selector
            selectors[index].wakeup();
            // 唤醒的是第一个服务员，然后注册read事件，交给服务员处理
            socketChannel.register(selectors[index], SelectionKey.OP_READ, new WorkerHandler(socketChannel));

            // 服务员处理了后，下次就给下一个服务员，知道所有服务员都接待了客人，回到第一服务员
            if (++index == threads.length) {
                index = 0;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
