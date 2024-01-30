package com.zookeeperClient;

import com.exmaple.ZookeeperRegistry;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class ServerWatch implements Watcher {
    @SneakyThrows
    @Override
    public void process(WatchedEvent event) {
        log.info("WatchedEvent:{}",event);
        List<String> registered = ZookeeperRegistry.getRegistered(this);
        log.info("registered:{}",registered);

    }
}
