package com.rpc.demo.service.provider;


public class RPCProviderMain {
    public static void main(String[] args) throws Exception {
        HelloProviderServiceImpl service = new HelloProviderServiceImpl();
        System.out.println("RPC提供者启动");
        ProviderReflect.provider(service,8083);
    }
}
