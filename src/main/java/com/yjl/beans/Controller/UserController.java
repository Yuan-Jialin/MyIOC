package com.yjl.beans.Controller;

import com.yjl.beans.Service.*;

/**
 * @Author: DLMU 袁佳林
 * @Date: 2024/3/14 13:48
 * @Description:
 **/

public class UserController {
    private userService userService;
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UserController() {
    }

    public UserController(com.yjl.beans.Service.userService userService) {
        this.userService = userService;
    }

    public com.yjl.beans.Service.userService getUserService() {
        return userService;
    }

    public void setUserService(com.yjl.beans.Service.userService userService) {
        this.userService = userService;
    }
}
