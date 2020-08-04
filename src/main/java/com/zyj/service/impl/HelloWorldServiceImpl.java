package com.zyj.service.impl;

import com.zyj.domain.User;
import org.springframework.stereotype.Service;
import com.zyj.service.IHelloWorldService;

@Service
public class HelloWorldServiceImpl implements IHelloWorldService {

    @Override
    public void save(User user) {
        System.out.println(user);
    }
}
