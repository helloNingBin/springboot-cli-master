package com.com.atguigu.rabbitmq.four;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmCallback;
import com.springboot.cli.mq.RabbitDefine;
import com.util.RabbitMqUtils;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class ConfirmSend {
    static int sendCount = 1000;
    public static void main(String[] args) throws InterruptedException, TimeoutException, IOException {
        /*send01();
        send02();*/
        send03();
    }
    static void send01() throws IOException, TimeoutException, InterruptedException {
        Channel channel = RabbitMqUtils.getChannel();
        channel.confirmSelect();//需要确认发布
        channel.queueDeclare(RabbitDefine.QUEUE_HELLO,true,false,false, null);
        //send a message
        long start = System.currentTimeMillis();
        for (int i = 0; i < sendCount; i++) {
            String message = "hello world" + i;
            channel.basicPublish("", RabbitDefine.QUEUE_HELLO,null,message.getBytes());
            channel.waitForConfirms();
        }
        System.out.println("本次消息发送完毕！" + (System.currentTimeMillis() - start));
    }
    static void send02() throws IOException, TimeoutException, InterruptedException {
        Channel channel = RabbitMqUtils.getChannel();
        channel.confirmSelect();//需要确认发布
        channel.queueDeclare(RabbitDefine.QUEUE_HELLO,true,false,false, null);
        //send a message
        long start = System.currentTimeMillis();
        for (int i = 0; i < sendCount; i++) {
            String message = "hello world" + i;
            channel.basicPublish("", RabbitDefine.QUEUE_HELLO,null,message.getBytes());
            if(i % 100 == 0){
                channel.waitForConfirms();
            }
        }
        channel.waitForConfirms();
        System.out.println("批量 消息发送完毕！" + (System.currentTimeMillis() - start));
    }
    static void send03() throws IOException, TimeoutException, InterruptedException {
        Channel channel = RabbitMqUtils.getChannel();
        channel.confirmSelect();//需要确认发布
        channel.queueDeclare(RabbitDefine.QUEUE_HELLO,true,false,false, null);
        //send a message
         ConfirmCallback ackCallback = (deliveryTag, multiple) -> {
            System.out.println(deliveryTag + "-1-" + multiple);
        };
         ConfirmCallback nackCallback = ackCallback = (deliveryTag, multiple) -> {
             System.out.println(deliveryTag + "-2-" + multiple);
         };
        channel.addConfirmListener(ackCallback,nackCallback);
        long start = System.currentTimeMillis();
        for (int i = 0; i < sendCount; i++) {
            String message = "hello world" + i;
            channel.basicPublish("", RabbitDefine.QUEUE_HELLO,null,message.getBytes());

        }
        System.out.println("异步 消息发送完毕！" + (System.currentTimeMillis() - start));
    }
}
