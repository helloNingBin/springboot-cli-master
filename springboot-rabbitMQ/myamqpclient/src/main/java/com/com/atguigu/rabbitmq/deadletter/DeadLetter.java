package com.com.atguigu.rabbitmq.deadletter;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.springboot.cli.mq.RabbitDefine;
import com.util.RabbitMqUtils;
import org.springframework.amqp.core.ExchangeTypes;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class DeadLetter {
    static String NORMAL_EXCHANGE = "normal_exchange";
    static String NORMAL_QUEUE = "normal_queue";
    static String DEADLETTER_EXCHANGE = "deadletter_exchange";
    static String DEADLETTER_QUEUE = "deadletter_queue";
    public static void main(String[] args) throws Exception{
         normal();
        deadLetter();
        Channel channel = RabbitMqUtils.getChannel();
        for(;;){
            AMQP.BasicProperties p = new AMQP.BasicProperties();
            p = p.builder().expiration("5000").build();
            channel.basicPublish(NORMAL_EXCHANGE, NORMAL_QUEUE,p,(System.currentTimeMillis()+"").getBytes());
//            channel.basicPublish(DEADLETTER_EXCHANGE, DEADLETTER_QUEUE,p,(System.currentTimeMillis()+"").getBytes());
            System.out.println("===================");
       Thread.sleep(26000);
        }
    }
    static void normal()throws Exception{
        new Thread(()->{
            try {
                Channel channel = RabbitMqUtils.getChannel();
                channel.exchangeDeclare(NORMAL_EXCHANGE, ExchangeTypes.DIRECT);
                Map<String,Object> params = new HashMap<>();
                params.put("x-dead-letter-exchange",DEADLETTER_EXCHANGE);
                params.put("x-dead-letter-routing-key",DEADLETTER_QUEUE);
                channel.queueDeclare(NORMAL_QUEUE, true, false, false, params);
                channel.queueBind(NORMAL_QUEUE, NORMAL_EXCHANGE, NORMAL_QUEUE);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
    static  void deadLetter()throws Exception{
        new Thread(()->{
            try {
                Channel channel = RabbitMqUtils.getChannel();
                channel.exchangeDeclare(DEADLETTER_EXCHANGE, ExchangeTypes.DIRECT);
                channel.queueDeclare(DEADLETTER_QUEUE, true, false, false,null);
                channel.queueBind(DEADLETTER_QUEUE, DEADLETTER_EXCHANGE, DEADLETTER_QUEUE);
                DeliverCallback deliverCallback = (consumerTag, message) -> {
                    System.out.println("DEADLETTER_QUEUE consumerTag:" + consumerTag);
                    System.out.println("DEADLETTER_QUEUE message:" + new String(message.getBody()));
                };
                CancelCallback cancelCallback = consumerTag -> {
                    System.out.println(consumerTag);
                };
                channel.basicConsume(DEADLETTER_QUEUE, true,deliverCallback,cancelCallback);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
}
