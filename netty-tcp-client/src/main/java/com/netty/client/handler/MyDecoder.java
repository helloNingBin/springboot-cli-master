package com.netty.client.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.util.List;

public class MyDecoder extends MessageToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext ctx, Object msg, List out) throws Exception {
        ByteBuf in = (ByteBuf)msg;
        byte[] dataBytes = new byte[in.readableBytes()];
        in.readBytes(dataBytes);
        String hexStr = getHexStr(dataBytes);
        System.out.println(hexStr);
        out.add(hexStr);
    }
    public static String getHexStr(byte[] bytes){
        StringBuilder sb = new StringBuilder();
        for(byte b : bytes){
            int i = b >= 0 ? b : b + 256;
            String hexString = Integer.toHexString(i);
            if(hexString.length() == 1){
                hexString = "0" + hexString;
            }
            if(sb.length() == 0){
                sb.append("" + hexString);
            }else{
                sb.append("," + hexString);
            }
        }
        return sb.toString().toUpperCase();
    }
}
