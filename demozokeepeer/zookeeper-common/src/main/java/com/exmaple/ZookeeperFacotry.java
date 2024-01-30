package com.exmaple;

import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;

public class ZookeeperFacotry {
    private static ZooKeeper zooKeeperCli;
    public static String connectionString = "139.159.196.163:3111,139.159.196.163:3121,139.159.196.163:3131";
    public static int timeout = 15000;
    private ZookeeperFacotry(){}
    public static ZooKeeper getZookeeper() throws IOException {
        //像这些有属性来标记它的实例化状态的，就用不着加volatile了
        if(zooKeeperCli == null || !zooKeeperCli.getState().isConnected()){
            synchronized (ZookeeperFacotry.class){
                if(zooKeeperCli == null || !zooKeeperCli.getState().isConnected()){
                    zooKeeperCli = new ZooKeeper(connectionString, timeout, (event)->{
                    });
                }
            }
        }
        return zooKeeperCli;
    }
}
