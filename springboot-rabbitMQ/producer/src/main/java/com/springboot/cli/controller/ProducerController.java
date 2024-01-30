package com.springboot.cli.controller;

import com.rabbitmq.client.Channel;
import com.springboot.cli.mq.RabbitDefine;
import com.springboot.cli.producer.RabbitProducer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;

@RestController
@Slf4j
public class ProducerController {
    @Autowired
    private RabbitProducer rabbitProducer;

    /**
     * 直接模式发送消息
     *
     * @param message 发送的信息
     */
    @GetMapping("/sendDirect")
    public void sendDirect(Object message) {
        rabbitProducer.sendDirect(message);
    }
    @GetMapping("/sendDirect2")
    public String sendDirect2(String message) throws InterruptedException {
        Long start = System.currentTimeMillis();
        rabbitProducer.sendDirect2(message + System.currentTimeMillis());
        return "cost time:" + (System.currentTimeMillis() - start) + "";
    }
    /**
     * 分裂模式发送消息
     *
     * @param message 发送的信息
     */
    @GetMapping("/sendFanout")
    public void sendFanout(Object message) {
        rabbitProducer.sendFanout(message);
    }


    /**
     * 主题模式发送消息
     *
     * @param message    发送的信息
     * @param routingKey 匹配的队列名
     */
    @GetMapping("/sendTopic")
    public void sendTopic(Object message, String routingKey) {
        rabbitProducer.sendTopic(message, routingKey);
    }


    /**
     * 发送延迟消息
     *
     * @param message 发送的信息
     * @param delay   延迟时间
     */
    @GetMapping("/sendDelay")
    public void sendDelay(String message, int delay) {
        rabbitProducer.sendDelay(message, delay);
    }

    /**
     * 发送临时消息
     */
    @GetMapping("/sendAndExpire")
    public void sendAndExpire(Object message) {
        rabbitProducer.sendAndExpire(message);
    }

    @RabbitListener(queuesToDeclare = @Queue(RabbitDefine.DIRECT_QUEUE2))
    public void consumer_derect_2(String message, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long tag) throws Exception {
        log.info("消费者1（直通消息）-队列索引：{},接收到消息：{}", tag, message);
         channel.basicAck(tag, false);
        if(tag > 0){
//           throw new Exception("consumer_derect_2 ..........................");
        }
    }
    @PostConstruct
    public void log(){

        new Thread(()->{
            for(int i = 0;i < 999999;i++){
                log.info("log line : " + i);
            }
        }).start();
    }
}
