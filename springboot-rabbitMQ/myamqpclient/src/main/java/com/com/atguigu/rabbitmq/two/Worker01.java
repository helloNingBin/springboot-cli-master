package com.com.atguigu.rabbitmq.two;

import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.springboot.cli.mq.RabbitDefine;
import com.util.RabbitMqUtils;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Worker01 {
    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitMqUtils.getChannel();
        //recived message
        /**
         *     String basicConsume(String queue, boolean autoAck, DeliverCallback deliverCallback,
         *     CancelCallback cancelCallback)
         */
        //成功回调
        DeliverCallback deliverCallback = (consumerTag, message) -> {
            System.out.println("C2 consumerTag:" + consumerTag);
            System.out.println("C2 message:" + new String(message.getBody()));
        };
        CancelCallback cancelCallback = consumerTag -> {
            System.out.println(consumerTag);
        };

        channel.basicConsume(RabbitDefine.QUEUE_HELLO, true,deliverCallback,cancelCallback);

    }
}
