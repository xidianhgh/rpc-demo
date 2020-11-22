package com.rpc.demo.domain;

import lombok.Data;

import java.io.Serializable;

@Data
public class TestBean implements Serializable {
    //RPC调用时如果没有实现，则会抛出NotSerializableException
    private String name;
    private Integer age;

    public TestBean(String name, Integer age) {
        this.name = name;
        this.age = age;
    }
}
