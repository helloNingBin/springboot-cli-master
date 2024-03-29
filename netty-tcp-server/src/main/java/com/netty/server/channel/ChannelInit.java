package com.netty.server.channel;

import com.netty.server.decoder.MessageDecoder;
import com.netty.server.decoder.MessageDecoder2;
import com.netty.server.handler.MessageHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.concurrent.TimeUnit;

/**
 * Netty 通道初始化
 *
 * @author qiding
 */
@Component
@RequiredArgsConstructor
public class ChannelInit extends ChannelInitializer<SocketChannel> {

    private final MessageHandler messageHandler;

    @Override
    protected void initChannel(SocketChannel channel) {
        channel.pipeline()
                // 心跳时间
                .addLast("idle", new IdleStateHandler(0, 0, 60, TimeUnit.SECONDS))
                // 添加解码器
              /*  .addLast(new StringDecoder())
                // 添加编码器
                .addLast(new StringEncoder())*/

              .addLast(new MessageDecoder())
                // 添加消息处理器
                .addLast("messageHandler", messageHandler);
    }

}


