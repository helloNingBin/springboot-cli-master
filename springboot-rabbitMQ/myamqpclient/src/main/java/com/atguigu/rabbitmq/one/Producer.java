package com.atguigu.rabbitmq.one;

import com.rabbitmq.client.Channel;
import com.springboot.cli.mq.RabbitDefine;
import com.util.RabbitMqUtils;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Producer {
    public static final String QUEUE_HELLO = "hello";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitMqUtils.getChannel();
        //(String queue, boolean durable, boolean exclusive, boolean autoDelete,
        //                                 Map<String, Object> arguments) throws IOException;
        /**
         * exclusive:是否可以多个消费者消费
         * arguments：应该是死信之类的参数吧
         */
        channel.queueDeclare(RabbitDefine.QUEUE_HELLO,true,false,false, null);
        //send a message
        String message = "hello world";
        /**
         * String exchange, String routingKey, BasicProperties props, byte[] body
         * exchange:不能写null，可以用""代替
         * routingKey:本次填写队列名称就可以了
         */
        channel.basicPublish("", RabbitDefine.QUEUE_HELLO,null,message.getBytes());
        System.out.println("本次消息发送完毕！");

    }
}
