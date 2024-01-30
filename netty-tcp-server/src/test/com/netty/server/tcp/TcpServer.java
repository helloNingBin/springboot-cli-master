package com.netty.server.tcp;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

@Slf4j
public class TcpServer {
    public static void main(String[] args) {
        int port = 8888;
        EventLoopGroup connectGroup = new NioEventLoopGroup();//连接处理的轮循组
        EventLoopGroup workerGroup =new NioEventLoopGroup();//读写处理的轮循组
        try {
            log.info("开始启动nettyTcpServer");
            ServerBootstrap b = new ServerBootstrap();//启动类
            b.group(connectGroup,workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .localAddress(new InetSocketAddress(port))
                    .childHandler(new ChannelInitializer<SocketChannel>() {

                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new MessageDecoder());
                            ch.pipeline().addLast(new IdleStateHandler(0, 0, 460, TimeUnit.SECONDS));
                            ch.pipeline().addLast(new MessageHandler());

                        }
                    });
            log.info("启动nettyTcpServer完毕");
            ChannelFuture sync = b.bind(port).sync();
            if (sync.isSuccess() == true) {
                log.info("nettyTcpServer启动成功");
            } else {
                log.error("nettyTcpServer启动失败", sync.cause());
            }
            sync.channel().closeFuture().sync();
            log.info("关闭nettyTcpServer");
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            //释放资源
            connectGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
            log.info("finally里释放nettyTcpServer");
        }
    }
}
