package com.example.final_augues.nio.reactor.single;

import java.io.IOException;

public class SingleMain {

    public static void main(String[] args) throws IOException {

        new Thread(new Reactor(8080, "single-main")).start();

    }
}
