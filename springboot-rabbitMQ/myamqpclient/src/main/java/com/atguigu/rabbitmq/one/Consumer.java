package com.atguigu.rabbitmq.one;

import com.rabbitmq.client.*;
import com.springboot.cli.mq.RabbitDefine;
import com.util.RabbitMqUtils;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Consumer {

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitMqUtils.getChannel();
        //recived message
        /**
         *     String basicConsume(String queue, boolean autoAck, DeliverCallback deliverCallback,
         *     CancelCallback cancelCallback)
         */
        //成功回调
        DeliverCallback deliverCallback = (consumerTag, message) -> {
            System.out.println("C1 message:" + new String(message.getBody()));
        };
        CancelCallback cancelCallback = consumerTag -> {
            System.out.println(consumerTag);
        };
//        channel.basicConsume(RabbitDefine.QUEUE_HELLO, true,deliverCallback,cancelCallback);
        channel.basicConsume("fanout.queue", true,deliverCallback,cancelCallback);

    }
}
