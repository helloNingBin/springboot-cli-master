package com.zookeeperClient;

import com.exmaple.ZookeeperFacotry;
import com.exmaple.ZookeeperRegistry;
import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Slf4j
@Component
public class BalanceLoader implements InitializingBean {
    @Autowired
    private ServerWatch watch;
    private List<String> urlList;

    int index;
    public String dispatcher(){
        return null;
    }

    private void init() throws InterruptedException, IOException, KeeperException {
        urlList = ZookeeperRegistry.getRegistered(watch);
        log.info("init urlList:{}",urlList);
    }
    @Override
    public void afterPropertiesSet() throws Exception {
        init();
    }

    public static void main(String[] args)throws Exception {
        Watcher watcher = (event)->{
        };
        List<String> registered = ZookeeperRegistry.getRegistered(watcher);
        System.out.println(registered);
     /*   ZooKeeper zookeeper = new ZooKeeper(ZookeeperFacotry.connectionString, ZookeeperFacotry.timeout, (event)->{
        });
        String serverRoot = "/distributed";
        List<String> children = zookeeper.getChildren(serverRoot, watcher);
        System.out.println(children);*/
    }
}
