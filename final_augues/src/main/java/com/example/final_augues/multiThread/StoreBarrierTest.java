package com.example.final_augues.multiThread;

public class StoreBarrierTest {
    public static volatile int counter = 1;

    public static void main(String[] args) {
        counter = 2;
        System.out.println(counter);
    }
}
