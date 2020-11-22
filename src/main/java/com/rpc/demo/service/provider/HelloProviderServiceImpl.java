package com.rpc.demo.service.provider;

import com.rpc.demo.domain.TestBean;

public class HelloProviderServiceImpl  {

    public TestBean sayHello(String content) {
//        return "Hello," + content;
        TestBean testBean=new TestBean(content,19);
        return testBean;

    }
}
