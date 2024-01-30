package com.ning.date;

public class TimeCal {
    long start = System.currentTimeMillis();
    public void printTimeCost(){
        System.out.println("cost time:" + (System.currentTimeMillis() - start));
    }
    public long getCostTime(){
        return System.currentTimeMillis() - start;
    }
    public void reset(){
        this.start = System.currentTimeMillis();
    }
    public void resetAndPrintTimeCost(){
        reset();
        printTimeCost();
    }
    public long resetAndGetCostTime(){
        reset();
        return getCostTime();
    }
}
