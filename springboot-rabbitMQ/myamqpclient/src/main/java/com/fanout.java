package com;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.springboot.cli.mq.RabbitDefine;
import com.util.RabbitMqUtils;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class fanout {
    static String exchange_fanout = "exchange_fanout";
    static String queue_fanout = "queue_fanout";
    static String queue_fanout2 = "queue_fanout2";
    public static void main(String[] args) throws InterruptedException {
        new Thread(()->{
            try {
                Channel channel = RabbitMqUtils.getChannel();
                //String exchange, String type, boolean durable, boolean autoDelete,
                //                                       Map<String, Object> arguments
                 channel.exchangeDeclare(exchange_fanout, "fanout", true, false, null);
                 channel.queueDeclare(queue_fanout, true, false, false, null);
                 channel.queueBind(queue_fanout, exchange_fanout, "123");

                channel.queueDeclare(queue_fanout2, true, false, false, null);
                channel.queueBind(queue_fanout2, exchange_fanout, "123");
                for(;;){
                    Thread.sleep(2000);
                    channel.basicPublish(exchange_fanout, "123", null, (System.currentTimeMillis()+"").getBytes());
//                    channel.basicPublish("", queue_fanout,null,(System.currentTimeMillis()+"").getBytes());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
        Thread.sleep(233);
        new Thread(()->{
            try {
                Channel channel = RabbitMqUtils.getChannel();
                DeliverCallback deliverCallback = (consumerTag, message) -> {
                    System.out.println("C1 consumerTag:" + consumerTag);
                    System.out.println("C1 message:" + new String(message.getBody()));
                };
                CancelCallback cancelCallback = consumerTag -> {
                    System.out.println(consumerTag);
                };
                channel.basicConsume(queue_fanout, true,deliverCallback,cancelCallback);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
        new Thread(()->{
            try {
                Channel channel = RabbitMqUtils.getChannel();
                DeliverCallback deliverCallback = (consumerTag, message) -> {
                    System.out.println("C2 consumerTag:" + consumerTag);
                    System.out.println("C2 message:" + new String(message.getBody()));
                };
                CancelCallback cancelCallback = consumerTag -> {
                    System.out.println(consumerTag);
                };
                channel.basicConsume(queue_fanout2, true,deliverCallback,cancelCallback);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
        Thread.sleep(222222222);
    }
}
