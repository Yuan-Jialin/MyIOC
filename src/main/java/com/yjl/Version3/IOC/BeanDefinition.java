package com.yjl.Version3.IOC;

/**
 * Bean 定义类：不存放 Bean，存放 Bean 的信息，如 bean 的 id、className 等
 */
public class BeanDefinition {
    private String id;

    private String beanClassName;

    public BeanDefinition(String id, String beanClassName) {
        this.id = id;
        this.beanClassName = beanClassName;
    }

    public String getBeanClassName() {

        return this.beanClassName;
    }

    public String getId() {
        return id;
    }
}