package com.netty.server.controller;

import com.base.vo.DeviceEvent;
import com.base.vo.ShareDeviceStatus;
import com.netty.server.store.DeviceChannelStore;
import com.netty.server.store.MessageUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
public class DeviceApi  implements InitializingBean {
    @Autowired
    private RestTemplate restTemplate;
    @Value("${new_system_address}")
    private String new_system_address;
    @Value("${mobile_address}")
    private String mobile_address;
    @Value("${wscenter_address}")
    private String wscenter_address;

    @GetMapping("device/openDoor/{deviceNo}/{index}")
    public String openDoor(@PathVariable String deviceNo,@PathVariable int index){
        String key = deviceNo.substring(0,11);
        if(key.equals("13829705950")){//时间紧迫，没时间做安全性方面的
            //TODO 以后再做安全校验
            deviceNo = deviceNo.substring(11);
           return DeviceChannelStore.openDoor(deviceNo,index);
        }
        return "false";
    }
    @GetMapping("device/lightControl/{deviceNo}")
    public String lightControl(@PathVariable String deviceNo,int startHour,
                               int startMinut,int endHour,int endMinut){
        String key = deviceNo.substring(0,11);
        if(key.equals("13829705950")){//时间紧迫，没时间做安全性方面的
            //TODO 以后再做安全校验
            deviceNo = deviceNo.substring(11);
            return DeviceChannelStore.lightControl(deviceNo,startHour,startMinut,endHour,endMinut);
        }
        return "false";
    }
    @GetMapping("device/voiceControl/{deviceNo}")
    public String voiceControl(@PathVariable String deviceNo,int startHour,
                               int startMinut,int endHour,int endMinut){
        String key = deviceNo.substring(0,11);
        if(key.equals("13829705950")){//时间紧迫，没时间做安全性方面的
            //TODO 以后再做安全校验
            deviceNo = deviceNo.substring(11);
            return DeviceChannelStore.voiceControl(deviceNo,startHour,startMinut,endHour,endMinut);
        }
        return "false";
    }
    @GetMapping("device/addDevice/{deivceNo}")
    public String openDoor(@PathVariable String deviceNo){
        DeviceChannelStore.addDeviceInfo(deviceNo);
        return "ok";
    }
    @GetMapping("device/getDeviceInfo/{deviceNo}/{index}")
    public ShareDeviceStatus getDeviceInfo(@PathVariable String deviceNo,@PathVariable int index){
        return DeviceChannelStore.getDeviceInfo(deviceNo, index);
    }
    @GetMapping("printDeviceInfo")
    public String printDeviceInfo(){
        return DeviceChannelStore.printdeviceInfo_test();
    }
    @GetMapping("device/addDeviceInfo")
    public String addDeviceInfo(String deviceNo){
        DeviceChannelStore.addDeviceInfo(deviceNo);
        return "ok";
    }
    @Override
    public void afterPropertiesSet() throws Exception {
        try{
            String url = wscenter_address + "devices/refreshRedis.html?brand=2";
            String forObject = restTemplate.getForObject(url, String.class);
            log.info("refreshRedis:" + forObject);
        }catch (Exception e){
            e.printStackTrace();
        }
        try {
            String url = mobile_address + "device/getAllDeviceNo.do?init=1";
            // 创建请求体
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            // 创建HTTP请求实体
            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(  headers);

            // 发送POST请求
            ResponseEntity<List<String>> responseEntity = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    requestEntity,
                    ParameterizedTypeReference.forType(List.class)
            );

            // 检查响应状态码
            HttpStatus statusCode = responseEntity.getStatusCode();
            if (statusCode == HttpStatus.OK) {
                List<String> result = responseEntity.getBody();
                // 处理返回的List<String>
                for (String deviceNo : result) {
                    DeviceChannelStore.addDeviceInfo(deviceNo);
                }
            } else {
                System.err.println("Request failed with status code: " + statusCode);
            }
        }catch (Exception e){
            log.error("init get deviceNo error", e);
        }
    }
}
