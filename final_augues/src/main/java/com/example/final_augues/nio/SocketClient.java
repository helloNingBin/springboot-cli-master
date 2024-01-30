package com.example.final_augues.nio;

import java.io.IOException;
import java.net.Socket;
import java.nio.channels.Selector;

public class SocketClient {
    public static void main(String[] args) throws Exception {
        System.out.println("############");
        Selector serverSelector = Selector.open();
        serverSelector.select();
        System.out.println("===========");
        Socket socket = new Socket("localhost", 7000);
        socket.getInputStream();
        Thread.sleep(333333333);
    }
}
