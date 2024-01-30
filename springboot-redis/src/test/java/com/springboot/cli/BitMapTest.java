package com.springboot.cli;

public class BitMapTest {
    public static void main(String[] args) {
        System.out.println(getUserIdHashCode("abcdf"));
    }
    private static int getUserIdHashCode(String userId) {

        int hashCode = userId.hashCode();
        // 1073741823是Redis位图最大支持长度(2^30-1)，可根据实际需求调整
        return Math.abs(hashCode % 1073741823);
    }
}
