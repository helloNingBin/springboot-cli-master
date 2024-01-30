package com.netty.server.handler;

import com.base.util.CommonConstant;
import com.base.util.StringUtil;
import com.base.vo.ShareDeviceStatus;
import com.base.vo.YiGouShareDeviceStatus;
import com.netty.server.store.ChannelStore;
import com.netty.server.store.DeviceChannelStore;
import com.netty.server.store.EventPushUtil;
import com.netty.server.store.MessageUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * 消息处理,单例启动
 *
 * @author qiding
 */
@Slf4j
@Component
@ChannelHandler.Sharable
@RequiredArgsConstructor
public class MessageHandler extends SimpleChannelInboundHandler<YiGouShareDeviceStatus> {
    private final static AttributeKey<Integer> EVENTTRIGGERCOUNT_KEY = AttributeKey.valueOf("eventTriggerCount");

    @Autowired
    private EventPushUtil eventPushUtil;
    AtomicInteger ai = new AtomicInteger();
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, YiGouShareDeviceStatus message) throws Exception {
      /*   log.debug("\n");
       log.debug("channelId:" + ctx.channel().id());
        log.debug("收到消息:{}", message);*/
       /* // 判断是否未登录
        if (!ChannelStore.isAuth(ctx)) {
            // 这里登录逻辑自行实现，我这里为了演示把第一次发送的消息作为客户端ID
            String clientId = message.trim();
            ChannelStore.bind(ctx, clientId);
            log.debug("登录成功");
            ctx.writeAndFlush("login successfully");
            return;
        }*/
        ctx.channel().attr(EVENTTRIGGERCOUNT_KEY).set(0);
       if(CommonConstant.EVENT_CHECK.equals(message.getEvnet()) || "03".equals(message.getControlType())){
           String responseMessage = MessageUtil.getYiGouMessage(message);
           String deviceNo = message.getDeviceNo();
           int index = message.getIndex();
           Integer eventValue = message.getEventValue();
           log.info("event==>" + message.getEvnet());
           if(CommonConstant.EVENT_LINE.equals(message.getEvnet())){//设备初始化（设备上报）
               if(DeviceChannelStore.hasDevice(deviceNo)){
                   Channel oldChannel = DeviceChannelStore.setDeviceChannel(deviceNo, ctx.channel());
                   if(oldChannel != null && oldChannel != ctx.channel()){
                       oldChannel.attr(DeviceChannelStore.CLIENT_DEVICE_NO).set(null);
                       oldChannel.close();
                       log.info("close old channel,deviceNo:{}",deviceNo);
                   }
                   message.setOnline(true);
                   ctx.channel().attr(DeviceChannelStore.CLIENT_DEVICE_NO).set(deviceNo);
                   eventPushUtil.pushEvent(message);
               }else{
                   log.info("设备不合法【#{}】",deviceNo);
               }
            }else if(CommonConstant.EVENT_DOOR.equals(message.getEvnet()) && eventValue != null){//门事件
                boolean isLock = eventValue == 0 ? true : false;//2门状态，取值1 （未关闭）或0（已关闭）
                boolean isDoorTriger = DeviceChannelStore.isDoorTriger(deviceNo, index, isLock);
                message.setLocked(isLock);
                if(isDoorTriger){
                    eventPushUtil.pushEvent(message);
                }

               ShareDeviceStatus deviceInfo = DeviceChannelStore.getDeviceInfo(deviceNo, index);
               if(deviceInfo != null){
                   deviceInfo.setOpenLockTimeStamp(null);
               }
            }else if(CommonConstant.EVENT_RECHARGER.equals(message.getEvnet()) && eventValue != null){//电源事件
                boolean isConnected = eventValue == 0 ? true : false;//充电线，取值1（未插好）或0（已插好）
                boolean isRechargerTriger = DeviceChannelStore.isRechargerTriger(deviceNo, index, isConnected);
                message.setRecharger(isConnected);
                if(isRechargerTriger){
                    eventPushUtil.pushEvent(message);
                }
            }else if(CommonConstant.EVENT_REMOTE_CONTROL.equals(message.getEvnet()) && eventValue != null){//遥控事件
                boolean isConnected = eventValue == 0 ? true : false;//遥控器，取值1（未放好）或0 （已放好）；
                boolean isRemoteControlTriger = DeviceChannelStore.isRemoteControlTriger(deviceNo, index, isConnected);
                message.setRemoteControl(isConnected);
                if(isRemoteControlTriger){
                    eventPushUtil.pushEvent(message);
                }
            }else if(CommonConstant.EVENT_BATTERY.equals(message.getEvnet()) && eventValue != null){//电量事件
                boolean isBatteryTriger = DeviceChannelStore.isBatteryTriger(deviceNo, index, eventValue);
                if(isBatteryTriger){
                    message.setBattery(eventValue == null ? 0.0 : Double.valueOf(eventValue));
                    eventPushUtil.pushEvent(message);
                }
            }else if(CommonConstant.RESPONSE_DOOR.equals(message.getEvnet()) && eventValue != null){//开锁结果
               ShareDeviceStatus deviceInfo = DeviceChannelStore.getDeviceInfo(deviceNo, index);
               if(deviceInfo != null){
                   deviceInfo.setOpenLockTimeStamp(System.currentTimeMillis());
               }
                DeviceChannelStore.unLockOpenDoorRequest(deviceNo,index);
            }else if(CommonConstant.RESPONSE_VOICE.equals(message.getEvnet()) && eventValue != null){//开锁结果
               DeviceChannelStore.unLockLightRequest(deviceNo);
           }else if("INVALID007".equals(message.getEvnet()) && eventValue != null){//无效的关门（结束订单）
                //什么也不用管
           }
            ByteBuf bufff = DeviceChannelStore.transBuffer(responseMessage);
           System.out.println("response:" + responseMessage);
            ctx.writeAndFlush(bufff);
       }
//        log.debug("\n");
//        System.out.println("==，" + ai.incrementAndGet() + "==>" + message);
        // 回复客户端
//        ctx.writeAndFlush("ok");
    }

    /**
     * 指定客户端发送
     *
     * @param clientId 其它已成功登录的客户端
     * @param message  消息
     */
    public void sendByClientId(String clientId, String message) {
        Channel channel = ChannelStore.getChannel(clientId);
        channel.writeAndFlush(message);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        Channel channel = ctx.channel();
        if(channel.isActive())ctx.close();
        super.exceptionCaught(ctx, cause);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        String deviceNo = ctx.channel().hasAttr(DeviceChannelStore.CLIENT_DEVICE_NO) ? (String) ctx.channel().attr(DeviceChannelStore.CLIENT_DEVICE_NO).get() : "";
        log.debug("\n");
        log.debug("断开连接:" + deviceNo);
        if(deviceNo != null && !deviceNo.isEmpty()){
           ShareDeviceStatus bean = DeviceChannelStore.getDeviceInfo(deviceNo, 1);
           if(bean != null){
               bean.setEvnet(CommonConstant.EVENT_LINE);
               bean.setOnline(false);
               eventPushUtil.pushEvent(bean);
           }else{
               log.info("下线，不存在设备号【#{}】",deviceNo);
           }
        }
        ctx.channel().close();
//        ChannelStore.closeAndClean(ctx);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.debug("\n");
        log.debug("成功建立连接,channelId：{}", ctx.channel().id());
        ctx.channel().attr(EVENTTRIGGERCOUNT_KEY).set(0);
        super.channelActive(ctx);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent e = (IdleStateEvent) evt;
            Attribute<Integer> attr = ctx.channel().attr(EVENTTRIGGERCOUNT_KEY);
            int count = 0;
            if(attr != null && attr.get() != null){
                count = attr.get();
            }
            count++;
            String deviceNo = ctx.channel().hasAttr(DeviceChannelStore.CLIENT_DEVICE_NO) ? (String) ctx.channel().attr(DeviceChannelStore.CLIENT_DEVICE_NO).get() : "";
            log.debug("心跳事件时触发,deviceNo{},count:{}",deviceNo,count);
            ctx.channel().attr(EVENTTRIGGERCOUNT_KEY).set(count);
            if(count >= 3){
                ctx.channel().close();
            }
        }
    }

}
