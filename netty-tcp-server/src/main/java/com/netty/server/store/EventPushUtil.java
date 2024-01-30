package com.netty.server.store;

import com.base.util.CommonConstant;
import com.base.util.JsonUtil;
import com.base.vo.DeviceEvent;
import com.base.vo.ShareDeviceStatus;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Component
public class EventPushUtil {
    @Value("${wscenter_address}")
    private String wscenter_address;
    @Autowired
    private RestTemplate restTemplate;

    public void pushEvent(ShareDeviceStatus bean){
        String url = wscenter_address + "/devices/events";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestEntity = null;
        String evnet = bean.getEvnet();
        if(CommonConstant.EVENT_LINE.equals(evnet) || CommonConstant.EVENT_DOOR.equals(evnet) || CommonConstant.EVENT_RECHARGER.equals(evnet)
                || CommonConstant.EVENT_REMOTE_CONTROL.equals(evnet) || CommonConstant.EVENT_BATTERY.equals(evnet)){
            String deviceNo = bean.getDeviceNo();
            int index = bean.getIndex();
            ShareDeviceStatus deviceInfo = DeviceChannelStore.getDeviceInfo(deviceNo, index);

            Map<String,Object> requestMap = new HashMap<>();
            requestMap.put("evnet", bean.getEvnet());
            requestMap.put("deviceNo", deviceNo);
            requestMap.put("index", index);
            if(CommonConstant.EVENT_DOOR.equals(evnet)){
                //关门=>前20秒内没有开销的话，就不触发了。担心发重复
                requestMap.put("locked", bean.isLocked());
                deviceInfo.setLocked(bean.isLocked());
            }else if(CommonConstant.EVENT_LINE.equals(evnet)){
                requestMap.put("online", bean.isOnline());
                deviceInfo.setOnline(bean.isOnline());
            }else if(CommonConstant.EVENT_REMOTE_CONTROL.equals(evnet)){
                requestMap.put("remoteControl", bean.isRemoteControl());
                deviceInfo.setRemoteControl(bean.isRemoteControl());
            }else if(CommonConstant.EVENT_RECHARGER.equals(evnet)){
                requestMap.put("recharger", bean.isRecharger());
                deviceInfo.setRecharger(bean.isRecharger());
            }else if(CommonConstant.EVENT_BATTERY.equals(evnet)){
                requestMap.put("battery", bean.getBattery());
                deviceInfo.setBattery(bean.getBattery());
            }
            requestEntity = new HttpEntity<>(CommonConstant.DEVICE_JOSN_STAR_DES + JSONObject.fromObject(requestMap), headers);
        System.out.println("pushEvent=====》" + CommonConstant.DEVICE_JOSN_STAR_DES + JSONObject.fromObject(requestMap));
        }
        if(requestEntity != null){
            ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
            System.out.println(responseEntity.getBody());
        }
    }

   /* @Deprecated
    public void pushEvent2(ShareDeviceStatus bean){
        String url = wscenter_address + "device/pushEvnet";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<DeviceEvent> requestEntity = null;

        String evnet = bean.getEvnet();
        String deviceNo = bean.getDeviceNo();
        int index = bean.getIndex();
        if(CommonConstant.EVENT_LINE.equals(evnet)){//上线、下线
            int statusValue = bean.isOnline() ? 1 : 0;
            DeviceEvent deviceEvent = new DeviceEvent(statusValue, CommonConstant.SHARE_PRODUCT_TYPE_CAR, deviceNo, evnet, index);
            requestEntity = new HttpEntity<>(deviceEvent, headers);
        }else if(CommonConstant.EVENT_DOOR.equals(evnet) || CommonConstant.EVENT_RECHARGER.equals(evnet)
             || CommonConstant.EVENT_REMOTE_CONTROL.equals(evnet) || CommonConstant.EVENT_BATTERY.equals(evnet)){
            int statusValue = 0;
            if(CommonConstant.EVENT_DOOR.equals(evnet)){
                //关门=>前20秒内没有开销的话，就不触发了。担心发重复
            }
            DeviceEvent deviceEvent = new DeviceEvent(statusValue, CommonConstant.SHARE_PRODUCT_TYPE_CAR, deviceNo, evnet, index);
            requestEntity = new HttpEntity<>(deviceEvent, headers);
        }
//        System.out.println(requestEntity + "===================pushEvent==========================");
        if(requestEntity != null){
            ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
        }
    }*/
}
