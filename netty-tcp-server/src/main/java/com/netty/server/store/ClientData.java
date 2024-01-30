package com.netty.server.store;

public class ClientData{
    String deviceNo;
    Thread thread;
    String result;
    public ClientData(String deviceNo) {
        this.deviceNo = deviceNo;
        this.thread = Thread.currentThread();
    }


    public String getDeviceNo() {
        return deviceNo;
    }

    public void setDeviceNo(String deviceNo) {
        this.deviceNo = deviceNo;
    }

    public Thread getThread() {
        return thread;
    }

    public void setThread(Thread thread) {
        this.thread = thread;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}