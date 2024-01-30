package com.example.final_augues.nio.reactor.single;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class Handler implements Runnable {

    SocketChannel socketChannel;

    public Handler(SocketChannel socketChannel) {

        this.socketChannel = socketChannel;
    }

    public void run() {
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

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
