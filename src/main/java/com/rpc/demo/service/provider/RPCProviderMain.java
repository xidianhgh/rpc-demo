package com.rpc.demo.service.provider;

import com.rpc.demo.service.HelloService;
import com.rpc.demo.service.HelloServiceImpl;

public class RPCProviderMain {
    public static void main(String[] args) throws Exception {
        HelloService service = new HelloServiceImpl();
        ProviderReflect.provider(service,8083);
        System.out.println("RPC提供者启动");
    }
}
