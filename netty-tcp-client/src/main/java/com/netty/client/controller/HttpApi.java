package com.netty.client.controller;

import com.alibaba.fastjson.JSONObject;
import com.base.util.StringUtil;
import com.netty.client.server.TcpClient;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 模拟发送api
 *
 * @author qiding
 */
@RequiredArgsConstructor
@RestController
@Slf4j
public class HttpApi {

    private final TcpClient tcpClient;

    /**
     * 消息发布
     */
    @GetMapping("/send")
    public String send(String message) {
        String str = "FF,FF,FF,FF,FF,01,01,00,00,01,8A,2A,B0,DE,F0,00,00,00,00,00,00,00,00,6E,49,D1,E3,16,76,E6,DA,91,3F,35,98,E9,FE,C3,89,00,00,00,0B,00,DC,32,31,30,35,30,31,31,34,31".replaceAll(",","");
        ByteBuf bufff = Unpooled.buffer();
//        //对接需要16进制
        bufff.writeBytes(StringUtil.gb(str));
        tcpClient.getSocketChannel().writeAndFlush(bufff);
        return "发送成功";
    }

    /**
     * 消息发布
     */
    @PostMapping("/send/json")
    public String send(@RequestBody JSONObject body) {
        tcpClient.getSocketChannel().writeAndFlush(body.toJSONString());
        return "发送成功";
    }

    /**
     * 连接
     */
    @GetMapping("connect")
    public String connect(String ip, Integer port) throws Exception {
        tcpClient.connect(ip, port);
        return "重启指令发送成功";
    }

    /**
     * 重连
     */
    @GetMapping("reconnect")
    public String reconnect() throws Exception {
        tcpClient.reconnect();
        return "重启指令发送成功";
    }
}
