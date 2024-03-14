package com.yjl.Version2;

import com.yjl.Version2.IOC.IocContainer;
import com.yjl.beans.Controller.UserController;

/**
 * @Author: DLMU 袁佳林
 * @Date: 2024/3/14 15:07
 * @Description:
 **/

public class Test {

    public static void main(String[] args) {
        IocContainer iocContainer=new IocContainer("Version2.xml");
        UserController userController = (UserController) iocContainer.getBean("userController");
        System.out.println(userController.getClass());
        System.out.println(userController.getUserService());
        System.out.println(userController.getName());
    }
}
