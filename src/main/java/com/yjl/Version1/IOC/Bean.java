package com.yjl.Version1.IOC;

import javax.management.monitor.StringMonitorMBean;
import java.util.Formatter;

/**
 * @Author: DLMU 袁佳林
 * @Date: 2024/3/14 12:55
 * @Description:
 **/

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
}
