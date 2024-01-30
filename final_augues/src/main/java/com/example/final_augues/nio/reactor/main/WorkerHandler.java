package com.example.final_augues.nio.reactor.main;

import javax.net.ssl.StandardConstants;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

public class WorkerHandler implements Runnable {

    private SocketChannel socketChannel;


    public WorkerHandler(SocketChannel socketChannel) {
        this.socketChannel = socketChannel;

    }

    @Override
    public void run() {
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

        try {
            socketChannel.read(byteBuffer);
            String message = new String(byteBuffer.array(), StandardCharsets.UTF_8);
            System.out.println(socketChannel.getRemoteAddress() + ":" + message);
            socketChannel.write(ByteBuffer.wrap("消息收到了".getBytes(StandardCharsets.UTF_8)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
