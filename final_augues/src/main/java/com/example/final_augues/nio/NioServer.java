package com.example.final_augues.nio;

import org.hibernate.sql.Select;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Set;

public class NioServer {

    static NioServer nioServer = new NioServer();

    public static void main(String[] args) throws IOException {
        nioServer.start();
    }

    public void start() throws IOException {
        //1.open server channel
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        //2.set port and blocking
        serverSocketChannel.bind(new InetSocketAddress(7000));
        serverSocketChannel.configureBlocking(false);
        //3.bind acceptor event
        Selector serverSelector = Selector.open(); // 打开Selector处理Channel，底层调用epoll_create
        // 把ServerSocketChannel注册到selector上，并且selector对客户端accept连接操作感兴趣，底层调用epoll_ctl
        serverSocketChannel.register(serverSelector, SelectionKey.OP_ACCEPT);
        System.out.println("服务启动成功!");
        //4.handler acceptor channel
        Selector clientSelector = Selector.open();
        new Thread(new socketChannelRunnable(clientSelector)).start();
        while (true) {
            int select = serverSelector.select();
            System.out.println("select:" + select);
            if (select > 0) {
                Iterator<SelectionKey> iterator = serverSelector.selectedKeys().iterator();
                //5.bind read event
                while (iterator.hasNext()) {
                    SelectionKey accpetKey = iterator.next();
                    if (accpetKey.isAcceptable()) {
                        SocketChannel socketChannel = ((ServerSocketChannel) accpetKey.channel()).accept();
                        socketChannel.configureBlocking(false);
                        System.out.println("注册前");
                        clientSelector.wakeup();
                        socketChannel.register(clientSelector, SelectionKey.OP_READ);//
                        System.out.println("注册后");
                    }
                    iterator.remove();//

                }

            }
        }

        //6.handle read channel
    }

    private class socketChannelRunnable implements Runnable {
        private Selector selector;

        public socketChannelRunnable(Selector selector) {
            this.selector = selector;
        }

        @lombok.SneakyThrows
        @Override
        public void run() {
            while (true) {
                int select = selector.select();
                if (select > 0) {
                    Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                    while (iterator.hasNext()) {
                        SelectionKey selectionKey = iterator.next();
                        if (selectionKey.isReadable()) {
                            SocketChannel channel = (SocketChannel) selectionKey.channel();
                            ByteBuffer byteBuffer = ByteBuffer.allocate(128);
                            channel.read(byteBuffer);
                            System.out.println(new String(byteBuffer.array()));
                            byteBuffer.flip();
                        }
                        iterator.remove();
                    }
                }
            }
        }
    }
}
