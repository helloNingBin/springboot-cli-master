package com.ning.math;

public class MathUtil {
    public static String hexToBinary(String hex){
        return Long.toBinaryString(Long.valueOf(hex, 16));
    }

    public static void main(String[] args) {
        System.out.println(hexToBinary("00000000a1f07d90"));
        System.out.println(1<<20);
    }
}
