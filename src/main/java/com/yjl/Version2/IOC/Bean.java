package com.yjl.Version2.IOC;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: DLMU 袁佳林
 * @Date: 2024/3/14 13:53
 * @Description:
 **/
/*
与Version1 不同 Version1中 bean中没有存放属性 Version2中需要往bean中注入属性
 */
public class Bean {
    //<bean id="userServiceImpl"
    //          class="com.yjl.beans.Service.Impl.userServiceImpl">
    //</bean>
    //以上面的配置为例，id字段用于存id，beanClassName用来存class
    private String id;
    private String beanClassName;

    public Bean(String id, String beanClassName) {
        this.id = id;
        this.beanClassName = beanClassName;
    }

    public String getId() {
        return id;
    }

    public String getBeanClassName() {
        return beanClassName;
    }

    /*
    下面是与Version1不同的部分
     */
    //存放bean的所有属性名称
    private final List<String> propertyNames = new ArrayList<>();
    //存放bean构造方法的参数
    private final List<String> constructorArguments = new ArrayList<>();

    private final List<String>propertyValue=new ArrayList<>();

    public List<String> getPropertyNames() {
        return propertyNames;
    }

    public List<String> getConstructorArguments() {
        return constructorArguments;
    }

    boolean ConstructorArgumentsIsEmplty(){
        return constructorArguments.isEmpty();
    }

    public List<String> getPropertyValue() {
        return propertyValue;
    }
}
