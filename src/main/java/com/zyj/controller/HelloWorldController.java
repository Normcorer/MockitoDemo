package com.zyj.controller;

import com.zyj.domain.User;
import com.zyj.service.IHelloWorldService;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;

@Controller
public class HelloWorldController {
    @Resource
    IHelloWorldService helloWorldService;

    public void hello(Throwable cause) {
        if ((cause instanceof NullPointerException)) {
            helloWorldService.save(new User("zyj", 18));
        } else {
            helloWorldService.save(new User("zyj1", 19));
        }

    }
}
