package com.netty.server.store;

import com.base.util.StringUtil;
import com.base.vo.ShareDeviceException;
import com.base.vo.ShareDeviceStatus;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.LockSupport;

@Slf4j
public class DeviceChannelStore {

    public static long ONE_SECOND_IN_NACOS = 1000000000L; // 阻塞当前线程1秒钟 (1秒 = 1,000,000,000 纳秒)

    public static Map<String,ClientData> clientDataMap = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<String, Channel> deviceChannelMap = new ConcurrentHashMap();
    private static final ConcurrentHashMap<String, ShareDeviceStatus> deviceInfoMap = new ConcurrentHashMap();
    /**
     * 频道绑定 key
     */
    public final static AttributeKey<String> CLIENT_DEVICE_NO = AttributeKey.valueOf("clientDeviceNo");

    public static Channel getDeviceChannel(String deviceNo){
        return deviceChannelMap.get(deviceNo);
    }
    public static Channel setDeviceChannel(String deviceNo,Channel channel){
        return deviceChannelMap.put(deviceNo,channel);
    }
    public static ShareDeviceStatus getDeviceInfo(String deviceNo,int index){
        return deviceInfoMap.get(deviceNo+"_"+index);
    }
    public static void addDeviceInfo(String deviceNo){
        ShareDeviceStatus deviceInfo_1 = DeviceChannelStore.getDeviceInfo(deviceNo, 1);
        if(deviceInfo_1 == null){
            deviceInfo_1 = new ShareDeviceStatus(deviceNo, 1);
            deviceInfo_1.setOnline(false);
            deviceInfoMap.put(deviceNo+"_"+1,deviceInfo_1);
            log.info("put deviceInfo_1:" + DeviceChannelStore.getDeviceInfo(deviceNo, 1));
        }
        ShareDeviceStatus deviceInfo_2 = DeviceChannelStore.getDeviceInfo(deviceNo, 2);
        if(deviceInfo_2 == null){
            deviceInfo_2 = new ShareDeviceStatus(deviceNo, 2);
            deviceInfo_2.setOnline(false);
            deviceInfoMap.put(deviceNo+"_"+2,deviceInfo_2);
            log.info("put deviceInfo_2:" + DeviceChannelStore.getDeviceInfo(deviceNo, 2));
        }
    }
    //===========门===============
    public static boolean isDoorTriger(String deviceNo,int index,boolean isLock) throws ShareDeviceException {
        ShareDeviceStatus bean = getDeviceInfo(deviceNo,index);
        if(bean == null){//走到这一步一定是有值的
            log.error("isDoorTriger=>deviceInfoMap deviceNo not found.deviceNo:" + deviceNo + "-" + index + ",map size:" + deviceInfoMap.size());
            throw new ShareDeviceException("isDoorTriger=>deviceInfoMap deviceNo not found.deviceNo:" + deviceNo);
        }

        Boolean old_state = bean.getLocked();//先保存旧的状态
        //内存中bean的值必需在这里更新
        bean.setLocked(isLock);
        if(old_state == null){//在主板启动时，会上报门的状态，这时不需要触发
            return false;
        }
        //关门=>前20秒内没有开销的话，就不触发了。担心发重复
        boolean result = old_state != isLock;//true 有变化
        if(!result&& bean.isInLockTime(22)){//有一种情况没变化也得触发，就是开锁后自动上锁
            result = true;
        }
        return result;
    }
    //===========电源插头===============
    public static boolean isRechargerTriger(String deviceNo,int index,boolean isConnected) throws ShareDeviceException {

        ShareDeviceStatus bean = getDeviceInfo(deviceNo,index);
        if(bean == null){//走到这一步一定是有值的
            log.error("isRechargerTriger=>deviceInfoMap deviceNo not found.deviceNo:" + deviceNo + "-" + index);
            throw new ShareDeviceException("isRechargerTriger=>deviceInfoMap deviceNo not found.deviceNo:" + deviceNo);
        }

        Boolean old_state = bean.getRecharger();//先保存旧的状态
        //内存中bean的值必需在这里更新
        bean.setRecharger(isConnected);
        if(old_state == null){//在主板启动时，会上报电源的状态，这时不需要触发
            return false;
        }
        return old_state != isConnected;//true 有变化
    }
    //===========遥控===============
    public static boolean isRemoteControlTriger(String deviceNo,int index,boolean isConnected) throws ShareDeviceException {
        ShareDeviceStatus bean = getDeviceInfo(deviceNo,index);
        if(bean == null){//走到这一步一定是有值的
            log.error("isRemoteControlTriger=>deviceInfoMap deviceNo not found.deviceNo:" + deviceNo + "-" + index);
            throw new ShareDeviceException("isRemoteControlTriger=>deviceInfoMap deviceNo not found.deviceNo:" + deviceNo);
        }

        Boolean old_state = bean.getRemoteControl();//先保存旧的状态
        //内存中bean的值必需在这里更新
        bean.setRemoteControl(isConnected);
        if(old_state == null){//在主板启动时，会上报遥控的状态，这时不需要触发
            return false;
        }
        return old_state != isConnected;//true 有变化
    }
    //===========电量插头===============
    public static boolean isBatteryTriger(String deviceNo,int index,int battery) throws ShareDeviceException {
        ShareDeviceStatus bean = getDeviceInfo(deviceNo,index);
        if(bean == null){//走到这一步一定是有值的
            log.error("isBatteryTriger=>deviceInfoMap deviceNo not found.deviceNo:" + deviceNo + "-" + index);
            throw new ShareDeviceException("isBatteryTriger=>deviceInfoMap deviceNo not found.deviceNo:" + deviceNo);
        }

        Double old_state = bean.getBattery();//先保存旧的状态
        //内存中bean的值必需在这里更新
        bean.setBattery(Double.valueOf(battery));
        if(old_state == null){//在主板启动时，会上报电源的状态，这时不需要触发
            return false;
        }
        return old_state.compareTo(bean.getBattery()) != 0;//true 有变化
    }

    /**
     * 简单点，除了ok，其它都是错误信息
     */
    public static String openDoor(String deviceNo,int index){
        String result = "false";
        ClientData clientData = null;
        try{
            String openDoorKey = getOpenDoorKey(deviceNo, index);
            synchronized (openDoorKey){
                clientData = clientDataMap.get(openDoorKey);
                if(clientData == null){
                    clientData = new ClientData(deviceNo);
                    clientDataMap.put(openDoorKey, clientData);
                }
                clientData.setThread(Thread.currentThread());
                // 发送开门指令
                String openDoorStr = MessageUtil.getOpenDoor(deviceNo, index);
                sendDeviceOrder(deviceNo, openDoorStr);
                log.info("begin开始阻塞，等待返回结果！");
                //阻塞并等待返回结果
                LockSupport.parkNanos(ONE_SECOND_IN_NACOS*5);
                result = clientData.getResult();
                clientData.setResult(null);
                log.info("result:" + result);
            }
        }catch (Exception e){
            e.printStackTrace();
            result = e.toString();
        }finally {
            if(clientData != null){
                clientData.setThread(null);
            }
        }
        return result;
    }
    public static String lightControl(String deviceNo,int startHour,
                                             int startMinut,int endHour,int endMinut){
        return lightOrVoiceControl(deviceNo,2,startHour,startMinut,endHour,endMinut);
    }
    public static String voiceControl(String deviceNo,int startHour,
                                      int startMinut,int endHour,int endMinut){
        return lightOrVoiceControl(deviceNo,1,startHour,startMinut,endHour,endMinut);
    }
    public static String lightOrVoiceControl(String deviceNo,int controlType,int startHour,
                                             int startMinut,int endHour,int endMinut){
        String result = "false";
        ClientData clientData = null;
        try{
            //音量计划01 （计划值0-100）、灯光计划02（计划值1为ON、0为OFF）、
            String lightControlKey = getLightControlKey(deviceNo);
            int planVal = 100;
            if(controlType == 2){//灯光计划
                planVal = 1;
            }
            synchronized (lightControlKey){
                clientData = clientDataMap.get(lightControlKey);
                if(clientData == null){
                    clientData = new ClientData(deviceNo);
                    clientDataMap.put(lightControlKey, clientData);
                }
                clientData.setThread(Thread.currentThread());
                // 发送灯光指令
                String lightControlStr = MessageUtil.getLightOrVoiceControl(deviceNo,controlType,startHour,startMinut,endHour,endMinut,planVal);
                sendDeviceOrder(deviceNo, lightControlStr);
                log.info("begin开始阻塞，等待返回结果！");
                //阻塞并等待返回结果
                LockSupport.parkNanos(ONE_SECOND_IN_NACOS*5);
                result = clientData.getResult();
                clientData.setResult(null);
                log.info("result:" + result);
            }
        }catch (Exception e){
            e.printStackTrace();
            result = e.toString();
        }finally {
            if(clientData != null){
                clientData.setThread(null);
            }
        }
        return result;
    }
    /**
     * 向服务器发送命令
     */
    public static void sendDeviceOrder(String deviceNo,String orderStr){
        Channel deviceChannel = getDeviceChannel(deviceNo);
        ByteBuf bufff = DeviceChannelStore.transBuffer(orderStr);
        deviceChannel.writeAndFlush(bufff);
    }
    public static void unLockOpenDoorRequest(String deviceNo,int index){
        String openDoorKey = getOpenDoorKey(deviceNo, index);
        ClientData clientData =  clientDataMap.get(openDoorKey);
        log.info("clientData:" + clientData);
        if(clientData != null){
            Thread thread = clientData.getThread();
            log.info("clientData thread:" + thread);
            if(thread != null){
                LockSupport.unpark(thread);
                clientData.setResult("true");
            }
        }
    }
    public static void unLockLightRequest(String deviceNo){
        String key = getLightControlKey(deviceNo);
        ClientData clientData =  clientDataMap.get(key);
        log.info("clientData:" + clientData);
        if(clientData != null){
            Thread thread = clientData.getThread();
            log.info("clientData thread:" + thread);
            if(thread != null){
                LockSupport.unpark(thread);
                clientData.setResult("true");
            }
        }
    }
    /**
     * 在项目启动时，以及后台有新增设备时，会向deviceInfoMap添加数据
     */
    public static boolean hasDevice(String deviceNo_10) {
        return deviceInfoMap.containsKey(deviceNo_10+"_1");
    }
    public static String printdeviceInfo_test(){
        StringBuilder sb = new StringBuilder();
        Set<Map.Entry<String, ShareDeviceStatus>> entries = deviceInfoMap.entrySet();
        for(Map.Entry<String, ShareDeviceStatus> entry : entries){
            sb.append(entry.getKey() + "=======" + entry.getValue());
        }
        return sb.toString();
    }
    public static ByteBuf transBuffer(String msg){
        ByteBuf bufff = Unpooled.buffer();
//        //对接需要16进制
        bufff.writeBytes(StringUtil.gb(msg));
        return bufff;
    }
    public static String getOpenDoorKey(String deviceNo,int index){
        return "opendoor_" + deviceNo + "_" + index;
    }
    public static String getLightControlKey(String deviceNo){
        return "lightControl_" + deviceNo;
    }
    public static void main(String[] args) throws Exception {
        test_isRechargerTriger();
    }
    private static void test_isRechargerTriger() throws ShareDeviceException, InterruptedException {
        String deviceNo = "123";
        int index = 1;
        ShareDeviceStatus bean = new ShareDeviceStatus();
        bean.setDeviceNo(deviceNo);
        bean.setIndex(index);
        addDeviceInfo(deviceNo);
        //初始化测试
        System.out.println("初始化时，不能触发,期待结果是false，实际结果是：" + isBatteryTriger(deviceNo, index, 1));
        //相同不能触发
        System.out.println("相同不能触发,期待结果是false，实际结果是：" + isBatteryTriger(deviceNo, index, 1));
        //不相同可以触发
        System.out.println("不相同可以触发,期待结果是true，实际结果是：" + isBatteryTriger(deviceNo, index, 2));
    }
    private static void test_isDoorTriger() throws ShareDeviceException, InterruptedException {
        String deviceNo = "123";
        int index = 1;
        ShareDeviceStatus bean = new ShareDeviceStatus();
        bean.setDeviceNo(deviceNo);
        bean.setIndex(index);
        addDeviceInfo(deviceNo);
        //初始化测试
        System.out.println("初始化时，不能触发,期待结果是false，实际结果是：" + isDoorTriger(deviceNo, index, true));
        //相同不能触发
        System.out.println("相同不能触发,期待结果是false，实际结果是：" + isDoorTriger(deviceNo, index, true));
        //不相同可以触发
        System.out.println("不相同可以触发,期待结果是true，实际结果是：" + isDoorTriger(deviceNo, index, false));

        //开锁，在15秒内相同也可以触发
         bean.setOpenLockTimeStamp(System.currentTimeMillis());
         bean.setLocked(true);
         System.out.println("开锁，在15秒内相同也可以触发,期待结果是true，实际结果是：" + isDoorTriger(deviceNo, index, true));
        //开锁，在15秒内,能正常触发
        System.out.println("开锁，在15秒内,能正常触发,期待结果是true，实际结果是：" + isDoorTriger(deviceNo, index, false));

        //开锁，在15秒之后，相同就不能触发
        Thread.sleep(15000);
        bean.setLocked(true);
        System.out.println("开锁，在15秒之后，相同就不能触发,期待结果是false，实际结果是：" + isDoorTriger(deviceNo, index, true));
        System.out.println("开锁，在15秒之后，能正常触发,期待结果是true，实际结果是：" + isDoorTriger(deviceNo, index, false));

    }

}
