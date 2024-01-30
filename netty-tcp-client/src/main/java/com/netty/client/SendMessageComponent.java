package com.netty.client;

import org.springframework.stereotype.Component;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Component
public class SendMessageComponent {
    public String sendMessage(Message message){
        /**
         * 现在的需求是：每次调用sendMessage都记录下调用时间，message.phone为key。
         * 并且调用sendMessage时，检查上一次的发送时间，如果上一次的发送时间如果与当前调用时间相隔小于2s，
         * 则延迟2s调用send方法，要求是异步的，不能产生阻塞。在2s后，检查一下在假时期间有没有相同的phone来调用message方法
         * ，如果没有就调用send方法，有则被取消调用send访求。
         * 举个例子：现在调用sendMessage方法，参数是Message("123","xxx")。这可以正常调用sned方法，因为前2s内phone=123没有调用过sendMessage，
         * 。然后在1s后，再次调用sendMessage方法，参数是Message("123","xxxxxxx")，这时就不能调用send方法了，因为前1s，phone=123刚调用过sendMessage。
         * 这时得延迟2s，如果这2s内没有phone=123调用sendMessage，就正常调用send方法，有的话就取消调用send方法
         */
        send(message.content);
        return "ok";
    }
    private static void send(String conent){
        System.out.println(conent);
    }
    private final static ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(8);

    public static void main(String[] args) {
        System.out.println("========");
        scheduler.schedule(() -> send("ddddd"), 5, TimeUnit.SECONDS);
        System.out.println("-----------");
    }
}
class Message{
    String phone;
    String content;
    public Message(String phone,String content){
        this.phone = phone;
        this.content = content;
    }
}