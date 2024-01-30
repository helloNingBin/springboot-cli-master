package com.netty.server.tcp;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
@Slf4j
public class MessageDecoder extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        byte[] dataBytes = new byte[in.readableBytes()];
        // in.readBytes(dataBytes);
              in.getBytes(in.readerIndex(),dataBytes);

        Collection<String> contentSet = getHexStrArray("",dataBytes);
        int deal_count = 0;//处理次数
        String[] msgAry = null;
        try {
            for(String content : contentSet){
                deal_count++;
                msgAry = content.split(",");
                if(content.indexOf("AA")<0){
                    throw new Exception("");
                }
                out.add(content);
                in.readBytes(msgAry.length);
            }
        }catch (Exception e){
            if(deal_count < contentSet.size()){
                in.readBytes(msgAry.length);//msgAry.length就是
                log.info("deal_count:{},连续两条不完整数据，丢弃第一条.contentSet:{}",deal_count,contentSet);
            }else{
                log.info("deal_count:{},数据不完整，等待下一次数据.contentSet:{}",deal_count,contentSet);
            }
        }
    }

    public static Collection<String> getHexStrArray(String deviceNo,byte[] bytes){
        List<String> contentSet = new ArrayList<>();//用set保存，可以去除重复的命令
        try {
            String startStr = "FF,";//命令的开始
            String content = com.netty.server.decoder.MessageDecoder.getHexStr(bytes);
            String[] contentArray = content.split(startStr);
            log.info("deviceNo:{},content:{}",deviceNo,content);
            for(String s : contentArray){
                if(s.length() > 2){//加上8位的设备号后，没有命令小于这个长度的
                    contentSet.add(startStr + s);
                    log.info("after split,deviceNo:{},content:{}",deviceNo,startStr + s);
                }
            }
        } catch (Exception e) {
            log.error("处理粘包时，发生错误");
        }
        return contentSet;
    }
}
