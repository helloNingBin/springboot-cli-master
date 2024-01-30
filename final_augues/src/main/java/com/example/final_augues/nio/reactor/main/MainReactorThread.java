package com.example.final_augues.nio.reactor.main;

import java.io.IOException;

public class MainReactorThread {

    public static void main(String[] args) throws IOException {

        new Thread(new MainReactor(8080), "Mian-Thread").start();
    }
}
