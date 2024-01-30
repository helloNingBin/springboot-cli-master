package com.util;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class RabbitMqUtils {
    public static Channel getChannel() throws IOException, TimeoutException {
        //create a connectionFactory
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("124.71.80.207");
        factory.setUsername("guest");
        factory.setPassword("guest");
        //create a connection from factory
        Connection connection = factory.newConnection();
        //create a channel from connection
        Channel channel = connection.createChannel();
        return channel;
    }
}
