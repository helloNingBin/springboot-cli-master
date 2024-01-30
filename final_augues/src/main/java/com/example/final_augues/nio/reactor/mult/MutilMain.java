package com.example.final_augues.nio.reactor.mult;

import java.io.IOException;

public class MutilMain {

    public static void main(String[] args) throws IOException {

        new Thread(new MutilReactor(8080, "single-main")).start();

    }
}
