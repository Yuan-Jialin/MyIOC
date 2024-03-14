package com.yjl.Version1;

import com.yjl.Version1.IOC.IocContainer;
import com.yjl.beans.Controller.StudentController;

/**
 * @Author: DLMU 袁佳林
 * @Date: 2024/3/14 13:13
 * @Description:
 **/

public class Test {

    public static void main(String[] args) {
        //读取配置文件，创建IOC
        IocContainer iocContainer=new IocContainer("Version1.xml");
        StudentController userController = (StudentController)iocContainer.getBean("studentController");
        System.out.println(userController.getClass().getName());
    }
}
