package com.example.final_augues.nio;

import java.io.IOException;
import java.nio.channels.Channel;
import java.nio.channels.InterruptibleChannel;

public class MyChannel implements InterruptibleChannel {
    @Override
    public boolean isOpen() {
        return false;
    }

    @Override
    public void close() throws IOException {

    }

    public static void main(String[] args) throws IOException {
        InterruptibleChannel channel = new MyChannel();
        channel.close();
    }
}
