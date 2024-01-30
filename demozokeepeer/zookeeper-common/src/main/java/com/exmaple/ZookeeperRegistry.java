package com.exmaple;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.List;

public class ZookeeperRegistry {
    public static void register(String serverName,String url) throws IOException, KeeperException, InterruptedException {
        ZooKeeper zookeeper = ZookeeperFacotry.getZookeeper();
        String serverRoot = "/distributed";
        Stat exists = zookeeper.exists(serverRoot, false);
        if(exists == null){
            zookeeper.create(serverRoot, null, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        }
        Stat serverExists = zookeeper.exists(serverRoot + "/" + serverName, null);
        if(serverExists == null){
            zookeeper.create(serverRoot+"/"+serverName, url.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
            System.out.println(serverName +" 注册了，url：" + url);
        }else{
            System.out.println(serverName +" 重复注册了，url：" + url);
        }
    }
    public static List<String> getRegistered(Watcher watcher) throws IOException, KeeperException, InterruptedException {
        ZooKeeper zookeeper = ZookeeperFacotry.getZookeeper();
        //ZooKeeper zookeeper = new ZooKeeper(ZookeeperFacotry.connectionString, ZookeeperFacotry.timeout, null);
        String serverRoot = "/distributed";
        List<String> children = zookeeper.getChildren(serverRoot, watcher);
        return children;
    }
    public static void main(String[] args) throws InterruptedException, IOException, KeeperException {
      Watcher watcher = (event)->{
        };
        List<String> registered = getRegistered(watcher);
        System.out.println(registered);
//        Thread.sleep(1000);
    }
}
