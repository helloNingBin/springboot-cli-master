package com.example.final_augues.nio.reactor.mult;

import java.io.IOException;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MutilDispatchHandler implements Runnable {

    SocketChannel socketChannel;

    private Executor executor = Executors.newCachedThreadPool();

    public MutilDispatchHandler(SocketChannel socketChannel) {
        this.socketChannel = socketChannel;
    }

    public void run() {
        executor.execute(new ReaderHandler(socketChannel));
    }


    static class ReaderHandler implements Runnable {
        SocketChannel socketChannel;

        public ReaderHandler(SocketChannel socketChannel) {
            this.socketChannel = socketChannel;
        }

        public void run() {
            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

            int length = 0, total = 0;
            String message = "";
            try {
                do {
                    length = socketChannel.read(byteBuffer);
                    message += new String(byteBuffer.array());
                    System.out.println(length);
                    // 就是判断数据是否有没有读完
                } while (length > byteBuffer.capacity());
                System.out.println(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
