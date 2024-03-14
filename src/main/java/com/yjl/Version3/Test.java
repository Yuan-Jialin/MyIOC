package com.yjl.Version3;

import com.yjl.Version3.IOC.context.ClassPathXmlApplicationContext;
import com.yjl.Version3.POJO.xushao;

/**
 * @Author: DLMU 袁佳林
 * @Date: 2024/3/14 18:35
 * @Description:
 **/

public class Test {

    public static void main(String[] args) {
        ClassPathXmlApplicationContext classPathXmlApplicationContext = new ClassPathXmlApplicationContext("Version3.xml");
        xushao xushaohui = (xushao) classPathXmlApplicationContext.getBean("xushao");
        System.out.println(xushaohui.getClass().getName());
        System.out.println(xushaohui.getApple().getClass().getName());
    }
}
