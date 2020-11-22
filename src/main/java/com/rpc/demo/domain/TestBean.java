package com.rpc.demo.domain;

import lombok.Data;

@Data
public class TestBean {
    private String name;
    private Integer age;

    public TestBean(String name, Integer age) {
        this.name = name;
        this.age = age;
    }
}
