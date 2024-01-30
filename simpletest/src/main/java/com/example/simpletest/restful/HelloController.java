package com.example.simpletest.restful;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    public static void main(String[] args) {
        System.out.println(5000*0.0068996212121212675 +3);
    }
    @GetMapping("/hello")
    public Object hello(Object obj){
        return Runtime.getRuntime().availableProcessors()+"->" + obj;
    }
}
