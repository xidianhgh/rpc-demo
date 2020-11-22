package com.rpc.demo.service.provider;

import com.rpc.demo.service.HelloService;

public class HelloProviderServiceImpl implements HelloService {

    public String sayHello(String content) {
        return "Hello," + content;
    }
}
