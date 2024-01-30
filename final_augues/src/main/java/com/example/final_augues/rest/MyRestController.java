package com.example.final_augues.rest;

import com.base.util.DateUtil;
import com.base.util.StringUtil;
import com.base.vo.DeviceEvent;
import com.example.final_augues.aspect.Log;
import com.example.final_augues.multiThread.VolatileTest;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
public class MyRestController {
    private static AtomicInteger i = new AtomicInteger();
    private VolatileTest k = new VolatileTest();

    @RequestMapping(value = "/device/pushEvnet", method = RequestMethod.POST)
    public String pushEvent(DeviceEvent event) throws NoSuchAlgorithmException {

        for (int i = 0; i < event.toString().length(); i++) {
            Object obj = event.equals("9djfdkfd");
            StringUtil.md5(obj.toString());
        }
        System.out.println(i.getAndIncrement() + "=>" + event);
        ;
        return "ok";
    }

    @RequestMapping(value = "/device/pushEvnet2", method = RequestMethod.POST)
    public String pushEvent2(@RequestBody String json) {

        System.out.println(json);
        ;
        return "ok";
    }

    @RequestMapping(value = "device/getAllDeviceNo/{deviceType}/{brand}", method = RequestMethod.POST)
    @ResponseBody
    public List<String> getAllDeviceNo(@PathVariable int deviceType, @PathVariable int brand) {
        try {
            List<String> deviceNoList = new ArrayList<>();
            deviceNoList.add("210501141");
            deviceNoList.add("228030299");
            deviceNoList.add("210800401");
            deviceNoList.add("218031020");
            deviceNoList.add("220300062");
            deviceNoList.add("228030299");
            return deviceNoList;
        } catch (Exception e) {
        }
        return null;
    }

    @RequestMapping(value = "device/getAllDeviceNo2/{deviceType}/{brand}", method = RequestMethod.GET)
    @ResponseBody
    public List<String> getAllDeviceNo2(@PathVariable int deviceType, @PathVariable int brand) {
        try {
            List<String> deviceNoList = new ArrayList<>();
            deviceNoList.add("210501141");
            deviceNoList.add("111");
            return deviceNoList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @GetMapping("/getUserId")
    public String jmeter(String userid) throws InterruptedException {
        System.out.println(DateUtil.defaultDateFormat(new Date()) + ":" + userid);
        System.gc();
        return "=>" + userid;
    }

    @Log(module = "测试模块", description = "测试AOP")
    @GetMapping("/getUserId2")
    public String jmeter2(String userid) throws InterruptedException {
        VolatileTest v = new VolatileTest();

        return "=>" + userid + v;
    }

    public static void main(String[] args) {
        int oldCapacity = 10;
        int newCapacity = oldCapacity + (oldCapacity >> 1);
        System.out.println(newCapacity);
       /* String filePath = "E:/localhost_access_log.2023-09-17.txt"; // 请替换为实际文件路径
        Map<String,Integer> map = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                // 在这里处理每一行的内容
//                System.out.println(line); // 例如，打印每一行
                String time = line.substring(0, 48);
                if(map.containsKey(time)){
                   map.put(time, map.get(time)+1);
                }else{
                    map.put(time,1);
                }
            }
            Set<Map.Entry<String, Integer>> entries = map.entrySet();
            for(Map.Entry<String,Integer> entry : entries){
                System.out.println(entry.getKey() + "==>" + entry.getValue());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }
}
