package com.netty.server.decoder;

import com.base.vo.YiGouShareDeviceStatus;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.MessageToMessageDecoder;
import lombok.extern.slf4j.Slf4j;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

@Slf4j
public class MessageDecoder2 extends ByteToMessageDecoder {

    protected void decode2(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        System.out.println("=================decode=======================");
        byte[] dataBytes = new byte[114];
        in.getBytes(in.readerIndex(),dataBytes);
        Collection<String> contentSet = getHexStrArray(dataBytes);
        if(contentSet.size() > 0){
            try {
                for(String content : contentSet){
                    System.out.println("=>" + content);

                }
            }catch(Exception e){
                System.out.println(e);
            }finally {
                in.readBytes(dataBytes);
            }
        }
    }
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        System.out.println("=================decode=======================");
        byte[] dataBytes = new byte[in.readableBytes()];
//        in.readBytes(dataBytes);
        in.getBytes(in.readerIndex(),dataBytes);
        Collection<String> contentSet = getHexStrArray(dataBytes);
        if(contentSet.size() > 0){
            int skipLength = 0;
            try {
                for(String content : contentSet){
                    System.out.println("=>" + content);
                    String[] msgAry = content.split(",");
                    int begin = 0;
                    int length = 5;
                    String startMsg = trimArray(msgAry, begin, length);//FF,FF,FF,FF,FF,（起始符 5个字节的0xFF）

                    begin+= length;
                    length = 1;
                    String controlType = trimArray(msgAry, begin, length);//01,（控制码 1字节）

                    begin+= length;
                    length = 1;
                    String messageId = trimArray(msgAry, begin, length);//01,（消息ID  1字节）,


                    //00,00,01,8A,20,12,99,11,（时间戳 8字节）
                    begin+= length;
                    length = 8;
                    String timestamp = trimArray(msgAry, begin, length);


                    //00,00,00,00,00,00,00,00,（SESSION ID 8字节）
                    begin+= length;
                    length = 8;
                    String sessionId = trimArray(msgAry, begin, length);


                    //（MD5  16字节）
                    begin+= length;
                    length = 16;
                    String md5 = trimArray(msgAry, begin, length);


                    //00,00,00,0B,（数据⻓度 4字节）
                    begin+= length;
                    length = 4;
                    String messageLength = trimArray(msgAry, begin, length);

                    begin+= length;
                    length = Integer.valueOf(messageLength,16);
                    String mainData = trimArray(msgAry, begin, length);
                    YiGouShareDeviceStatus bean = new YiGouShareDeviceStatus(controlType,messageId, timestamp, sessionId, md5, messageLength, mainData);
                    out.add(bean);

                    in.readBytes(msgAry.length);
                }
            }catch(Exception e){
                System.out.println(e);
            }finally {
//                in.skipBytes(skipLength);
            }
        }
    }
    /**
     * 防止粘包，要拆开
     */
    public static Collection<String> getHexStrArray(byte[] bytes){
        List<String> contentSet = new ArrayList<>();//用set保存，可以去除重复的命令
        try {
            String startStr = "FF,FF,FF,FF,FF,";//命令的开始
            String content = getHexStr(bytes);
            String[] contentArray = content.split(startStr);
            for(String s : contentArray){
                if(s.length() > 10){//加上8位的设备号后，没有命令小于这个长度的
                    contentSet.add(startStr + s);
                    System.out.println(startStr + s);
                }
            }
        } catch (Exception e) {
            log.error("处理粘包时，发生错误");
        }
        return contentSet;
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
    public static String trimArray(String[] strArray,int begin,int length){
        StringBuilder sb = new StringBuilder();
        for(;length > 0;length--){
            sb.append(strArray[begin]);
            begin++;
        }
        return sb.toString();
    }
    public static void main2(String[] args) throws NoSuchAlgorithmException {
       // 要加密的数据
        String str = "00DC323130353031313431";
        str = "0220504948534849495249";
        String key = "F3D7B88A9DF3AA9AEAD3064A3BA2DD77";

        String md5Str = md5(str);//加密后的数据
        System.out.println("首次加密：" + md5Str);

        //拼接到key后面再加密
        String finalMd5 = md5(key + md5Str);

        System.out.println(finalMd5);//66e41e1969a8df92e062a257901bbde2

        String s = "";
        StringBuilder sb = new StringBuilder();
        String[] data = "ac,dc,e4,0f,ae,fd,bf,40,85,4b,6a,4a,27,b5,12,b9".split(",");
        for (String datum : data) {
            s += Integer.valueOf(datum,16);
            sb.append(Integer.valueOf(datum,16));
        }
        System.out.println(sb.toString());


        finalMd5 = md5(key + sb);

        System.out.println(finalMd5);//66e41e1969a8df92e062a257901bbde2
    }
    public static String md5(String str) throws NoSuchAlgorithmException {
        MessageDigest md5 = MessageDigest.getInstance("md5");
        byte[] digest = md5.digest(str.getBytes());
        return new BigInteger(1, digest).toString(16);
    }

    public static String md5(byte[] bytes) throws NoSuchAlgorithmException {
        MessageDigest md5 = MessageDigest.getInstance("md5");
        byte[] digest = md5.digest(bytes);
        return new BigInteger(1, digest).toString(16);
    }
    public static byte[] getBytes(String[] str){
        byte[] bytes = new byte[str.length];
        int index = 0;
        for (String s : str) {
            bytes[index] = Integer.valueOf(s, 16).byteValue();
            index++;
        }
        return bytes;
    }
    public static byte[] String2Bytes(String str){
        int length = str.length()/2;
        byte[] bytes = new byte[length];
        for(int i = 0;i < length;i++){
            System.out.println(str.substring(i*2, i*2+2));
            bytes[i] = Integer.valueOf(str.substring(i*2, i*2+2),16).byteValue();
        }
        return bytes;
    }
    public static void main(String[] args) throws Exception {
       String str = "FF,FF,FF,FF,FF,3A,30,20,69,72,31,3A,30,20,69,72,32,3A,30,20,63,68,61,72,67,65,31,3A,30,20,63,68,61,72,67,65,32,3A,30";
       str="FF,FF,FF,FF,FF,03,01,00,00,01,8A,4F,47,EC,69,00,00,00,00,00,00,00,00,41,F8,8C,C9,5F,84,4F,6D,5B,57,42,F0,E0,BB,A2,C4,00,00,00,47,00,0D,00,00,00,01,51,03,0D,97,77,5B,04,00,00,01,8A,4F,47,EC,69,07,00,2F,64,6F,6F,72,31,3A,30,20,64,6F,6F,72,32,3A,30,20,69,72,31,3A,30,20,69,72,32,3A,30,20,63,68,61,72,67,65,31,3A,30,20,63,68,61,72,67,65,32,3A,30";
        System.out.println(str.split(",").length);
        String key = "F3D7B88A9DF3AA9AEAD3064A3BA2DD77";
 /*
        //拼接到key后面再加密
        String finalMd5 = md5(String2Bytes(key + firstMd5));

        System.out.println(finalMd5);//66e41e1969a8df92e062a257901bbde2
        System.out.println(StringUtil.yiGouMD5(str, key));*/
         decode_test(str);
    }
    public static String transToByteAndMd5(String str) throws NoSuchAlgorithmException {
        return md5(String2Bytes(str));
    }
    protected static void decode_test(String str) throws Exception {
        Collection<String> contentSet = new HashSet<>();
        contentSet.add(str);
        if(contentSet.size() > 0){
            for(String content : contentSet){
               /* FF,FF,FF,FF,FF,（起始符 5个字节的0xFF）
                01,（控制码 1字节）
                01,（消息ID  1字节）,
                00,00,01,8A,20,12,99,11,（时间戳 8字节）
                00,00,00,00,00,00,00,00,（SESSION ID 8字节）
                A1,ED,A8,10,91,80,21,3D,79,94,A6,22,10,2F,2F,F1,（MD5  16字节）
                00,00,00,0B,（数据⻓度 4字节）,
                00,DC,32,32,30,33,30,30,30,36,32,
                00,DC,32,31,30,35,30,31,31,34,31*/
                String[] msgAry = content.split(",");
                int begin = 0;
                int length = 5;
                String startMsg = trimArray(msgAry, begin, length);//FF,FF,FF,FF,FF,（起始符 5个字节的0xFF）

                begin+= length;
                length = 1;
                String controlType = trimArray(msgAry, begin, length);//01,（控制码 1字节）

                begin+= length;
                length = 1;
                String messageId = trimArray(msgAry, begin, length);//01,（消息ID  1字节）,


                //00,00,01,8A,20,12,99,11,（时间戳 8字节）
                begin+= length;
                length = 8;
                String timestamp = trimArray(msgAry, begin, length);


                //00,00,00,00,00,00,00,00,（SESSION ID 8字节）
                begin+= length;
                length = 8;
                String sessionId = trimArray(msgAry, begin, length);


                //（MD5  16字节）
                begin+= length;
                length = 16;
                String md5 = trimArray(msgAry, begin, length);


                //00,00,00,0B,（数据⻓度 4字节）
                begin+= length;
                length = 4;
                String messageLength = trimArray(msgAry, begin, length);

                begin+= length;
                length = Integer.valueOf(messageLength,16);
                System.out.println("length:" + length);
                String mainData = trimArray(msgAry, begin, length);
                log.info("控制码:{},消息ID:{},时间戳 :{},SESSION ID:{}" +
                                ",MD5:{},数据⻓度:{},mainData:{}",
                        controlType,messageId,timestamp,sessionId,md5,messageLength,mainData );
                YiGouShareDeviceStatus bean = new YiGouShareDeviceStatus(controlType,messageId, timestamp, sessionId, md5, messageLength, mainData);
            }
        }
    }

}
