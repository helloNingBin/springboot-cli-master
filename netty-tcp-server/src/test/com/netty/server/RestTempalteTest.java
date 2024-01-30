//package com.netty.server;
//
//import com.base.util.CommonConstant;
//import com.base.vo.ShareDeviceStatus;
//import com.netty.server.store.EventPushUtil;
//import net.sf.json.JSONObject;
//import org.junit.jupiter.api.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.context.ApplicationContext;
//import org.springframework.core.ParameterizedTypeReference;
//import org.springframework.http.*;
//import org.springframework.test.context.junit4.SpringRunner;
//import org.springframework.util.LinkedMultiValueMap;
//import org.springframework.util.MultiValueMap;
//import org.springframework.web.client.RestTemplate;
//
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//@SpringBootTest
//public class RestTempalteTest {
//    @Autowired
//    EventPushUtil eventPushUtil;
//    @Autowired
//    RestTemplate restTemplate;
//
//    @Test
//    public void postTest() throws InterruptedException {
//        for(int tc=0;tc<1;tc++){
//            Thread t = new Thread(()->{
//                Long start = System.currentTimeMillis();
//                for(int i = 0;i<1;i++){
//                    ShareDeviceStatus shareDeviceStatus = new ShareDeviceStatus("1234",2);
//                    shareDeviceStatus.setEvnet(CommonConstant.EVENT_LINE);
//                    shareDeviceStatus.setOnline(true);
//                    eventPushUtil.pushEvent(shareDeviceStatus);
//                }
//                System.out.println(System.currentTimeMillis() - start);
//            });
//            t.start();
//        }
//        Thread.sleep(1000 * 10);
//    }
//    @Test
//    public void getAllDeivceNoTest(){
//        String url = "http://localhost:8080/device/getAllDeviceNo";
//        //去获取所有的设备号，以验证连接服务器主板的合法性
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        Map<String,Object> params = new HashMap<>();
//        params.put("deviceType", 0);
//        params.put("brand", 2);
//        HttpEntity<Map<String,Object>> requestEntity = new HttpEntity<>(params, headers);
//        ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
//        System.out.println(requestEntity);
//    }
//    @Test
//    public void getAllDeivceNoTest3(){
//        String url = "http://localhost:8080/device/getAllDeviceNo/1/2";
//        // 定义请求参数
//        int deviceType = 1;
//        int brand = 2;
//
//        // 创建请求体
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//
//        // 创建HTTP请求实体
//        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(  headers);
//
//        // 发送POST请求
//        ResponseEntity<List<String>> responseEntity = restTemplate.exchange(
//                url,
//                HttpMethod.POST,
//                requestEntity,
//                ParameterizedTypeReference.forType(List.class)
//        );
//
//        // 检查响应状态码
//        HttpStatus statusCode = responseEntity.getStatusCode();
//        if (statusCode == HttpStatus.OK) {
//            List<String> result = responseEntity.getBody();
//            // 处理返回的List<String>
//            for (String item : result) {
//                System.out.println(item);
//            }
//        } else {
//            System.err.println("Request failed with status code: " + statusCode);
//        }
//
//    }
//
//    @Test
//    public void tewst3(){
//        String url = "http://139.159.196.163:8088/wscenter/devices/events";
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        HttpEntity<String> requestEntity = null;
//        ShareDeviceStatus bean = new ShareDeviceStatus("123456", 1);
//        bean.setEvnet(CommonConstant.EVENT_LINE);
//        String evnet = bean.getEvnet();
//        if(CommonConstant.EVENT_LINE.equals(evnet) || CommonConstant.EVENT_DOOR.equals(evnet) || CommonConstant.EVENT_RECHARGER.equals(evnet)
//                || CommonConstant.EVENT_REMOTE_CONTROL.equals(evnet) || CommonConstant.EVENT_BATTERY.equals(evnet)){
//            if(CommonConstant.EVENT_DOOR.equals(evnet)){
//                //关门=>前20秒内没有开销的话，就不触发了。担心发重复
//            }
//            requestEntity = new HttpEntity<String>(CommonConstant.DEVICE_JOSN_STAR_DES + JSONObject.fromObject(bean), headers);
//        }
////        System.out.println(requestEntity + "===================pushEvent==========================");
//        if(requestEntity != null){
//            ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
//            System.out.println(responseEntity.getBody());
//        }
//    }
//
//    @Test
//    public void tt(){
//        String url = "http://139.159.196.163:8088/wscenter/device/refreshRedis.html?brand=2";
//        String forObject = restTemplate.getForObject(url, String.class);
//System.out.println(forObject);
//    }
//}
