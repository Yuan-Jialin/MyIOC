package com.yjl.Version3.POJO;

import com.yjl.Version3.IOC.stereotype.Autowired;
import com.yjl.Version3.IOC.stereotype.Component;

/**
 * @Author: DLMU 袁佳林
 * @Date: 2024/3/14 18:38
 * @Description:
 **/
@Component(value = "xushao")
public class xushao {
    @Autowired
    private apple apple;

    public apple getApple() {
        return apple;
    }
}
