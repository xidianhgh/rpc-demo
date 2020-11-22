package com.rpc.demo.service.consumer;

import com.rpc.demo.domain.TestBean;

public class RPCConsumerMain {
    public static void main(String[] args) throws InterruptedException {
        HelloConsumerService service = ConsumerProxy.consume(HelloConsumerService.class,"127.0.0.1",8083);
        for (int i = 0;i < 1000;i++) {
            TestBean hello = service.sayHello("你好_" + i);
            System.out.println(hello.getName()+hello.getAge());
            Thread.sleep(1000);
        }
    }
}
