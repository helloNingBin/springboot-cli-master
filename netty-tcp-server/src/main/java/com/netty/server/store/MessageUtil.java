package com.netty.server.store;

import com.base.util.CommonConstant;
import com.base.util.DateUtil;
import com.base.util.StringUtil;
import com.base.vo.ShareDeviceStatus;
import com.base.vo.YiGouShareDeviceStatus;
import com.netty.server.handler.MessageHandler;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.unix.Buffer;
import lombok.extern.slf4j.Slf4j;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;
@Slf4j
public class MessageUtil {
    public static final String yigouKey = "F3D7B88A9DF3AA9AEAD3064A3BA2DD77";
    public static String getYiGouMessage(YiGouShareDeviceStatus bean){
//        System.out.print(DateUtil.defaultDateFormat(new Date()) + "=>");
        StringBuilder sb = new StringBuilder();
        String startMsg = "FFFFFFFFFF";
        String controlType = bean.getControlType();
        String messageId = bean.getMessageId();
        String timeStamp = StringUtil.paddingZero(Long.toHexString(System.currentTimeMillis()), 16);
        String sessionId = bean.getSessionId();
        String event = bean.getEvnet();
        String dataLength = null;
        String mainData = null;
        if(CommonConstant.EVENT_CHECK.equals(event)){
            dataLength = "00000009";
//            mainData = "323130353031313431";
            mainData = bean.getMainData().substring(4);
        }else if("03".equals(controlType)){
            String[] dataArray =StringUtil.transToArray(bean.getMainData());
            String orderId = StringUtil.trimArray(dataArray, 0, 2);
            log.info("命令ID：" + orderId);

            mainData = "00";
            if("0001".equals(orderId)){//设备初始化
                mainData = getMainData_0001(dataArray,bean);
            }else if("000D".equals(orderId)){//日志上报
                mainData = getMainData_000D(dataArray,bean);
            }else if("0009".equals(orderId)){//上传设备状态
                mainData = getMainData_0009(dataArray,bean);
            }else if("0005".equals(orderId)){//开锁结果
                mainData = getMainData_0005(dataArray,bean);
            }else if("0007".equals(orderId)){//订单结算（即开锁后，自动上锁，如果门是关上的，就结算，我们这算是关门动作吧）
                mainData = getMainData_0007(dataArray,bean);
            }else if("0008".equals(orderId)){//设备控制计划响应
                mainData = getMainData_0008(dataArray,bean);
            }
            dataLength = StringUtil.paddingZero(Integer.toHexString(mainData.length()/2), 8);
            log.info("dataLength:" + dataLength + ";mainData:" + mainData);
        }

        String md5 = StringUtil.yiGouMD5(mainData, yigouKey);
        sb.append(startMsg).append(controlType).append(messageId).append(timeStamp).append(sessionId)
                .append(md5).append(dataLength).append(mainData);
//        log.info("response:" + sb);
        return sb.toString();
    }

    /**
     * 上传设备状态
     * 上传设备状态。
     * 1电量，取值范围0-100；
     * 2门状态，取值1 （未关闭）或0（已关闭）；
     * 3遥控器，取值1（未放好）或0 （已放好）；
     * 4充电线，取值1（未插好）或0（已插好）；
     * 5 电池状态，取值1（异常）或0（正常）；
     * 1需要回复，0不需
     * 要回复
     */
    private static String getStateDesc(String type,String index,String value,ShareDeviceStatus bean){
        String deviceType = "";
        if(type.equals("0101")){
            deviceType = "电量";
            bean.setEvnet(CommonConstant.EVENT_BATTERY);
        }else if(type.equals("0102")){
            deviceType = "门状态";
            bean.setEvnet(CommonConstant.EVENT_DOOR);
        }else if(type.equals("0103")){
            deviceType = "遥控器";
            bean.setEvnet(CommonConstant.EVENT_REMOTE_CONTROL);
        }else if(type.equals("0104")){
            deviceType = "充电线";
            bean.setEvnet(CommonConstant.EVENT_RECHARGER);
        }else if(type.equals("0105")){
            deviceType = "电池状态";
        }
        int eventValue = Integer.valueOf(value.substring(2), 16);
        bean.setEventValue(eventValue);
        return "通道：" + index + ";" + deviceType + "==>" + value;
    }
    public static String getMainData_0009(String[] dataArray,ShareDeviceStatus bean){
        int begin = 2;
        int length = 1;
        String customSign1 = StringUtil.trimArray(dataArray, begin, length);
//        System.out.print(",customSign1：" + customSign1);

        begin+= length;
        length = 2;
        String customSign2 = StringUtil.trimArray(dataArray, begin, length);
//        System.out.print(",customSign2：" + customSign2);

        begin+= length;//设备类型	0x01 1字节
        length = 2;
        String deviceType = StringUtil.trimArray(dataArray, begin, length);
//        System.out.print(",deviceType：" + deviceType);

        begin+= length;//设备编号 0x03 4字节
        length = 5;
        String deviceNo = StringUtil.trimArray(dataArray, begin, length);
        String deviceNo_10 = getDeviceNoFromYiGou(deviceNo);
        log.info("deviceNo：" + deviceNo + "/" + deviceNo_10);

        begin+= length;//通道号 0x01 1字节
        length = 2;
        String deviceIndex = StringUtil.trimArray(dataArray, begin, length);
//        System.out.print(",deviceIndex：" + deviceIndex);

        begin+= length;//状态类型 0x01 1字节
        length = 2;
        String stateType = StringUtil.trimArray(dataArray, begin, length);
//        System.out.print(",stateType：" + stateType);

        begin+= length;//状态值 0x02 2字节
        length = 3;
        String stateValue = StringUtil.trimArray(dataArray, begin, length);
        log.info(",stateValue：" + stateValue);

        log.info(getStateDesc(stateType, deviceIndex,stateValue,bean));
        begin+= length;//是否需要回复 0x01 1字节
        length = 2;
        String needResponse = StringUtil.trimArray(dataArray, begin, length);
//        log.info("needResponse：" + needResponse + ("00".equals(needResponse) ? "不需要回复" : "需要回复"));
        bean.setDeviceNo(deviceNo_10);
        bean.setIndex(getDeviceIndexFromYiGou(deviceIndex));
        String responseOrderId = "0109";
        String responseCode = "00";

          /*命令ID 0x0109
            响应码 1字节（无符号）
            自定义标签名2 2字节（无符号）
            设备类型 0x01 1字节
            设备编号 0x03 4字节
            通道号 0x01 1字节
            状态类型 0x01 1字节
            状态值 0x02 2字节*/
        return responseOrderId + responseCode + customSign2 + deviceType + deviceNo + deviceIndex + stateType + stateValue;
    }
    public static String getMainData_000D(String[] dataArray, ShareDeviceStatus bean){
        int begin = 2;
        int length = 1;
        String customSign1 = StringUtil.trimArray(dataArray, begin, length);
        log.info("customSign1：" + customSign1);

        begin+= length;
        length = 2;
        String customSign2 = StringUtil.trimArray(dataArray, begin, length);
        log.info("customSign2：" + customSign2);

        begin+= length;//设备类型	0x01 1字节
        length = 2;
        String deviceType = StringUtil.trimArray(dataArray, begin, length);
        log.info("deviceType：" + deviceType);

        begin+= length;//设备编号 0x03 4字节
        length = 5;
        String deviceNo = StringUtil.trimArray(dataArray, begin, length);
        String deviceNo_10 = getDeviceNoFromYiGou(deviceNo);
        bean.setDeviceNo(deviceNo_10);
        log.info("deviceNo：" + deviceNo + "/" + deviceNo_10);

        begin+= length;//时间戳(日志发生时间) 0x04 8字节
        length = 9;
        String timeStamp = StringUtil.trimArray(dataArray, begin, length);
        log.info("timeStamp：" + timeStamp);

        begin+= length;//IMEI(15字节) 0x07 2字节 n字节（UTF-8字符串）
        length = dataArray.length - begin;
        String logData = StringUtil.trimArray(dataArray, begin, length);
        log.info("logData：" + logData);

        String responseOrderId = "010D";
        String responseCode = "00";
        String responseTimeStamp = StringUtil.paddingZero(Long.toHexString(System.currentTimeMillis()), 16);

          /*命令ID 0x010D
             响应码 1字节（无符号）
            自定义标签名2 2字节（无符号）
            时间戳(日志发生时间) 0x04 8字节*/
        return responseOrderId + responseCode + customSign2 + responseTimeStamp;
    }
    public static String getMainData_0001(String[] dataArray, ShareDeviceStatus bean){
        int begin = 0;
        int length = 2;
        /*String orderId = StringUtil.trimArray(dataArray, begin, length);
        log.info("命令ID：" + orderId);*/

        begin+= length;
        length = 1;
        String hardType = StringUtil.trimArray(dataArray, begin, length);
        log.info("hardType：" + hardType);

        begin+= length;
        length = 2;
        String customSign = StringUtil.trimArray(dataArray, begin, length);
        log.info("customSign：" + customSign);

        begin+= length;//设备类型	0x01 1字节
        length = 2;
        String deviceType = StringUtil.trimArray(dataArray, begin, length);
        log.info("deviceType：" + deviceType);

        begin+= length;//设备编号 0x03 4字节
        length = 5;
        String deviceNo = StringUtil.trimArray(dataArray, begin, length);
        String deviceNo_10 = getDeviceNoFromYiGou(deviceNo);
        bean.setDeviceNo(deviceNo_10);
        bean.setIndex(1);//这里给一个值，不然找不到设备信息
        log.info("deviceNo：" + deviceNo + "/" + deviceNo_10);

        begin+= length;//固件版本号 0x02 2字节
        length = 3;
        String hardVersion = StringUtil.trimArray(dataArray, begin, length);
        log.info("hardVersion：" + hardVersion + "/" + Integer.valueOf(hardVersion.substring(2),16));

        begin+= length;//IMEI(15字节) 0x07 2字节 n字节（UTF-8字符串）
        length = 18;
        String IMEI = StringUtil.trimArray(dataArray, begin, length);
        log.info("IMEI：" + IMEI);

        begin+= length;//iccid（20字节） 0x07 2字节 n字节（UTF-8字符串）
        length = 23;
        String iccid = StringUtil.trimArray(dataArray, begin, length);
        log.info("iccid：" + iccid);

        begin+= length;//通道总数 0x01 1字节
        length = 2;
        String trannelCount = StringUtil.trimArray(dataArray, begin, length);
        log.info("trannelCount：" + trannelCount);

        begin+= length;//当前设备是否存在订单  0x02 2字节
        length = 3;
        String orderCount = StringUtil.trimArray(dataArray, begin, length);
        log.info("orderCount：" + orderCount);

        begin+= length;//通道状态类型1  0x02 2字节
        length = 3;
        String trannelState1 = StringUtil.trimArray(dataArray, begin, length);
        log.info("trannelState1：" + trannelState1);

        begin+= length;//通道状态类型1  0x02 2字节
        length = 3;
        String trannelState2 = StringUtil.trimArray(dataArray, begin, length);
        log.info("trannelState2：" + trannelState2);

        begin+= length;//通道状态类型1  0x02 2字节
        length = 3;
        String trannelState3 = StringUtil.trimArray(dataArray, begin, length);
        log.info("trannelState3：" + trannelState3);
        String responseOrderId = "0101";
        String responseCode = DeviceChannelStore.hasDevice(deviceNo_10) ? "00" : "01";
          /*命令ID 0x0101
                响应码 1字节（无符号）
                自定义标签名2 2字节（无符号）
                设备类型 0x01 1字节
                设备编号 0x03 4字节*/
        return responseOrderId + responseCode + customSign + deviceType + deviceNo;
    }

    /**
     * open door result
     */
    public static String getMainData_0005(String[] dataArray, ShareDeviceStatus bean){
        int begin = 0;
        int length = 2;
        String orderId = StringUtil.trimArray(dataArray, begin, length);
       /* log.info("命令ID：" + orderId);*/

        begin+= length;
        length = 1;
        String responseCode = StringUtil.trimArray(dataArray, begin, length);
        log.info("responseCode：" + responseCode);

        begin+= length;
        length = 2;
        String optSign = StringUtil.trimArray(dataArray, begin, length);
        log.info("optSign：" + optSign);

        begin+= length;//设备类型	0x01 1字节
        length = 2;
        String deviceType = StringUtil.trimArray(dataArray, begin, length);
        log.info("deviceType：" + deviceType);

        begin+= length;//设备编号 0x03 4字节
        length = 5;
        String deviceNo = StringUtil.trimArray(dataArray, begin, length);
        String deviceNo_10 = getDeviceNoFromYiGou(deviceNo);
        bean.setDeviceNo(deviceNo_10);
        log.info("deviceNo：" + deviceNo + "/" + deviceNo_10);


        begin+= length;//通道号 0x01 1字节
        length = 2;
        String deviceIndex = StringUtil.trimArray(dataArray, begin, length);
        log.info("deviceIndex：" + deviceIndex);

         //订单号（流水号） 0x07 2字节 n字节（UTF-8字符串），这个没用，不解析了
        begin+= length;//通道号 0x01 1字节
        length = dataArray.length - begin;
        String orderNo = StringUtil.trimArray(dataArray, begin, length);
        log.info("orderNo：" + orderNo);

        bean.setDeviceNo(deviceNo_10);
        bean.setIndex(getDeviceIndexFromYiGou(deviceIndex));
        bean.setEventValue(Integer.valueOf(responseCode));

        return orderId + responseCode + optSign + deviceType + deviceNo + deviceIndex + orderNo;

//        return  responseCode +  deviceType + deviceNo;//这个不用回复的，只是为了不报错
    }
    /**
     * open door result
     */
    public static String getMainData_0007(String[] dataArray, ShareDeviceStatus bean){
        int begin = 0;
        int length = 2;
//        String orderId = StringUtil.trimArray(dataArray, begin, length);

        begin+= length;
        length = 1;//自定义标签名1   1字节（无符号）
        String customSign1 = StringUtil.trimArray(dataArray, begin, length);

        begin+= length;
        length = 2;//自定义标签名2 2字节（无符号）
        String customSign2 = StringUtil.trimArray(dataArray, begin, length);

        begin+= length;//设备类型	0x01 1字节
        length = 2;
        String deviceType = StringUtil.trimArray(dataArray, begin, length);

        begin+= length;//设备编号 0x03 4字节
        length = 5;
        String deviceNo = StringUtil.trimArray(dataArray, begin, length);
        String deviceNo_10 = getDeviceNoFromYiGou(deviceNo);
        bean.setDeviceNo(deviceNo_10);
        log.info("deviceNo：" + deviceNo + "/" + deviceNo_10);


        begin+= length;//通道号 0x01 1字节
        length = 2;
        String deviceIndex = StringUtil.trimArray(dataArray, begin, length);
        log.info("deviceIndex：" + deviceIndex);

        begin+= length;//结算日期-年（2021-2000） 0x01 1字节
        length = 2;
        String year = StringUtil.trimArray(dataArray, begin, length);

        begin+= length;//month
        length = 2;
        String month = StringUtil.trimArray(dataArray, begin, length);

        begin+= length;//day
        length = 2;
        String day = StringUtil.trimArray(dataArray, begin, length);

        begin+= length;//hour
        length = 2;
        String hour = StringUtil.trimArray(dataArray, begin, length);

        begin+= length;//minute
        length = 2;
        String minute = StringUtil.trimArray(dataArray, begin, length);

        begin+= length;//second
        length = 2;
        String second = StringUtil.trimArray(dataArray, begin, length);
        log.info("after lockClose deviceNo:" + deviceNo + "_" + deviceIndex
                + "," + year + "-" + month + "-" + day + " " + hour + ":" + minute + ":" + second);

        begin+= length;//orderNo
        length = dataArray.length - begin;
        String orderNo = StringUtil.trimArray(dataArray, begin, length) + second + minute + deviceNo;

        bean.setDeviceNo(deviceNo_10);
        bean.setIndex(getDeviceIndexFromYiGou(deviceIndex));
        bean.setEventValue(0);

        ShareDeviceStatus deviceInfo = DeviceChannelStore.getDeviceInfo(deviceNo_10, bean.getIndex());
        bean.setEvnet(CommonConstant.EVENT_DOOR);

        if(deviceInfo != null){//一般开锁到自动上锁，至少有15秒，但发现有的2秒就反馈了，这时得忽略
            Long openLockTimeStamp = deviceInfo.getOpenLockTimeStamp();
            log.info("openLockTimeStamp:" + openLockTimeStamp + ";currentTime:" + System.currentTimeMillis());
            if(openLockTimeStamp == null){
                bean.setEvnet("INVALID007");
                log.info("上锁后已经关门但还是结束订单." + deviceNo + "_" + deviceInfo.getIndex());
            }else{
                if(System.currentTimeMillis() - openLockTimeStamp < 13000){
                    log.info("上锁后" + (System.currentTimeMillis() - openLockTimeStamp) + ",结束订单." + deviceNo + "_" + deviceInfo.getIndex());
                    bean.setEvnet("INVALID007");
                }
            }
        }

       /* 命令ID 0x0107
       响应码 1字节（无符号）
        自定义标签名2 2字节（无符号）
        设备类型 0x01 1字节
        设备编号 0x03 4字节
        通道号 0x01 1字节
        订单号 0x07 2字节 n字节（UTF-8字符串）*/
        return  "0107" + "00" + customSign2 +  deviceType + deviceNo + deviceIndex + orderNo;
    }

    /**
     * 灯光/音响设置结果
     */
    public static String getMainData_0008(String[] dataArray, ShareDeviceStatus bean){
        int begin = 0;
        int length = 2;
        String orderId = StringUtil.trimArray(dataArray, begin, length);

        begin+= length;
        length = 1;
        String responseCode = StringUtil.trimArray(dataArray, begin, length);
        log.info("responseCode：" + responseCode);

        begin+= length;
        length = 2;//自定义标签名2 2字节（无符号）
        String customSign2 = StringUtil.trimArray(dataArray, begin, length);

        begin+= length;//设备类型	0x01 1字节
        length = 2;
        String deviceType = StringUtil.trimArray(dataArray, begin, length);

        begin+= length;//设备编号 0x03 4字节
        length = 5;
        String deviceNo = StringUtil.trimArray(dataArray, begin, length);
        String deviceNo_10 = getDeviceNoFromYiGou(deviceNo);
        bean.setDeviceNo(deviceNo_10);
        log.info("deviceNo：" + deviceNo + "/" + deviceNo_10);
        //订单号（流水号） 0x07 2字节 n字节（UTF-8字符串），这个没用，不解析了
        begin+= length;//通道号 0x01 1字节
        length = dataArray.length - begin;
        String orderNo = StringUtil.trimArray(dataArray, begin, length);
        log.info("orderNo：" + orderNo);
        bean.setDeviceNo(deviceNo_10);
        bean.setEventValue(Integer.valueOf(responseCode));
        bean.setBn(customSign2.substring(0,1));//音量计划01  、灯光计划02
        return orderId + responseCode + customSign2 + deviceType + deviceNo + orderNo;
    }
    public static String getDeviceNoFromYiGou(String deivceNoYiGou){
        //易购的设备号是：03+deviceNo
        String deviceNo_16 = deivceNoYiGou.substring(2);
        return Long.valueOf(deviceNo_16, 16)+"";
    }
    public static int getDeviceIndexFromYiGou(String indexYiGou){
        //易购的设备Index 是：01+Index
        String index_16 = indexYiGou.substring(2);
        return Integer.valueOf(index_16, 16) ;
    }
    /**
     *send open door to client
     */
    public static String getOpenDoor(String deviceNoStr,int index){
        StringBuilder sb = new StringBuilder();
        String startMsg = "FFFFFFFFFF";
        String controlType = "03";
        String messageId = "00";
        Calendar c = Calendar.getInstance();
        String timeStamp = StringUtil.paddingZero(Long.toHexString(c.getTimeInMillis()), 16);
        String sessionId = "0000000000000000";
        //=================main data=========================
        StringBuilder mainData = new StringBuilder();
        String orderId = "0105";//命令ID 0x0105
        mainData.append(orderId);
        String customSign = "00";//自定义标签名1 1字节（无符号）
        mainData.append(customSign);
        //操作标签为0则代表借车开门，1代表还车开门；
        String optsign = "0000";//操作标签 2字节（无符号）
        mainData.append(optsign);
        String deviceType = "0151";//设备类型 0x01 1字节
        mainData.append(deviceType);
        String deviceNo = "03" + StringUtil.toHex(Long.valueOf(deviceNoStr),8);//设备编号 0x03 4字节
        mainData.append(deviceNo);
        String deviceIndex = "010" + index;//通道号 0x01 1字节
        mainData.append(deviceIndex);
        //操作类型：01为业务操作，02为运营操作，03为其它类型；
        String optType = "0101";//操作类型 0x01 1字节
        mainData.append(optType);
        String optModel = "0101";//操作模式(先付后享或先享后付 0x01 1字节
        mainData.append(optModel);
        //01使用次数，02使用时间，03液体流量，04 其它；
        String optObjectType = "0101";//操作对象类型 0x01 1字节
        mainData.append(optObjectType);
        //操作值（分不同场景）：根据操作对象类型来定，如操作值为0时，停止当前操作
        String optValue = "0300000001";//操作值（次数、时长、流量） 0x03 4字节
        String opt_year = "01" + StringUtil.paddingZero(Integer.toHexString(c.get(Calendar.YEAR) % 100),2);
        String opt_month = "01" + StringUtil.paddingZero(Integer.toHexString(c.get(Calendar.MONTH) + 1),2);
        String opt_day = "01" + StringUtil.paddingZero(Integer.toHexString(c.get(Calendar.DAY_OF_MONTH)),2);
        String opt_hour = "01" + StringUtil.paddingZero(Integer.toHexString(c.get(Calendar.HOUR_OF_DAY)),2);
        String opt_minute = "01" + StringUtil.paddingZero(Integer.toHexString(c.get(Calendar.MINUTE)),2);
        String opt_second = "01" + StringUtil.paddingZero(Integer.toHexString(c.get(Calendar.SECOND)),2);
        String orderNo = "070000000000000000";
        mainData.append(optValue).append(opt_year).append(opt_month).append(opt_day)
                .append(opt_hour).append(opt_minute).append(opt_second).append(orderNo);

        String dataLength = StringUtil.paddingZero(Integer.toHexString(mainData.length()/2), 8);
        log.info("dataLength:" + dataLength + ";mainData:" + mainData);

        String md5 = StringUtil.yiGouMD5(mainData.toString(), yigouKey);
        sb.append(startMsg).append(controlType).append(messageId).append(timeStamp).append(sessionId)
                .append(md5).append(dataLength).append(mainData);
        log.info("send open door:" + sb);
        return sb.toString();
//        MessageHandler.c.writeAndFlush(transBuffer(sb.toString()));
    }
    public static String getLightOrVoiceControl(String deviceNo,int controlType
            ,int startHour,int startMinut,int endHour,int endMinut,int planVal){
        return getLightOrVoiceControl(deviceNo, controlType,
                StringUtil.toHex(startHour, 2),StringUtil.toHex(startMinut, 2),
                StringUtil.toHex(endHour, 2),StringUtil.toHex(endMinut, 2),planVal);
    }
    /**
     *设置灯光/音响计划
     * controlType ：
     * 音量计划01 （计划值0-100）、灯光计划02（计划值1为ON、0为OFF）、
     * 其它计划03（暂未定义计划值），预留字段4字节
     * startHour, startMinut, endHour, endMinut 是2个字串的16进制
     */
    public static String getLightOrVoiceControl(String deviceNoStr,int controlType
        ,String startHour,String startMinut,String endHour,String endMinut,int planVal){
        StringBuilder sb = new StringBuilder();
        String startMsg = "FFFFFFFFFF";
        String messageId = "00";
        Calendar c = Calendar.getInstance();
        String timeStamp = StringUtil.paddingZero(Long.toHexString(c.getTimeInMillis()), 16);
        String sessionId = "0000000000000000";
        //=================main data=========================
        StringBuilder mainData = new StringBuilder();
        String orderId = "0108";//命令ID 0x0108
        mainData.append(orderId);
        String customSign = "00";//自定义标签名1 1字节（无符号）
        mainData.append(customSign);
        String optsign = "000" + controlType;//自定义标签 2字节（无符号）
        mainData.append(optsign);
        String deviceType = "0151";//设备类型 0x01 1字节
        mainData.append(deviceType);
        String deviceNo = "03" + StringUtil.toHex(Long.valueOf(deviceNoStr),8);//设备编号 0x03 4字节
        mainData.append(deviceNo);
        //计划类型 0x01 1字节
        mainData.append("010" + controlType);
        //计划 0x09 2字节 n字节（数组）  其实这个也不知有什么用的？
        String planStr = "09" + startHour + startMinut + endHour + endHour;
        //数据段长度 2字节
        String planLength = StringUtil.toHex(planStr.length()/2,2);
        mainData.append(planStr);
        mainData.append(planLength);
        mainData.append("01" + startHour);
        mainData.append("01" + startMinut);
        mainData.append("01" + endHour);
        mainData.append("01" + endHour);
        //计划-计划值 0x01 1字节
        mainData.append("0101" + StringUtil.toHex(planVal, 2));
        //计划-预留 0x03 4字节
        mainData.append("0300000000");
        //流水号 0x07 2字节 n字节（UTF-8字符串）
        mainData.append("07" + deviceNo);

        String dataLength = StringUtil.paddingZero(Integer.toHexString(mainData.length()/2), 8);
        log.info("dataLength:" + dataLength + ";mainData:" + mainData);

        String md5 = StringUtil.yiGouMD5(mainData.toString(), yigouKey);
        sb.append(startMsg).append(controlType).append(messageId).append(timeStamp).append(sessionId)
                .append(md5).append(dataLength).append(mainData);
        log.info("send light control:" + sb);
        return sb.toString();
//        MessageHandler.c.writeAndFlush(transBuffer(sb.toString()));
    }
    public static void main(String[] args) {
        String mainData = "00,09,00,00,00,01,51,03,0D,21,83,1E,01,01,01,02,02,00,00,01,01";
//        log.info(getMainData_0009(mainData.split(",")));
//        openDoor();
        t("0000018AC1232A47");
        t("0000018AC1233231");
        t("0000018AC1233A1E");
    }

    private static void t(String hex){
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(Long.valueOf(hex,16));
        log.info(DateUtil.defaultDateFormat(c.getTime()));
    }
}
