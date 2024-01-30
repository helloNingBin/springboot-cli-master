package com.example.final_augues.nio.proactor;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.util.concurrent.CountDownLatch;

public class AIOProactor implements Runnable {

    public CountDownLatch latch;

    public AsynchronousServerSocketChannel serverSocketChannel;

    public AIOProactor(int prot) throws IOException {
        serverSocketChannel = AsynchronousServerSocketChannel.open();
        serverSocketChannel.bind(new InetSocketAddress(prot));
    }

    @Override
    public void run() {

        latch = new CountDownLatch(1);
        serverSocketChannel.accept(this, new AIOAcceptorHandler());
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
