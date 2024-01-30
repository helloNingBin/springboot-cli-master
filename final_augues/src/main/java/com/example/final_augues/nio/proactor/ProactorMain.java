package com.example.final_augues.nio.proactor;

import java.io.IOException;

public class ProactorMain {

    public static void main(String[] args) throws IOException {

        new Thread(new AIOProactor(7000), "Main-Thread").start();
    }
}
