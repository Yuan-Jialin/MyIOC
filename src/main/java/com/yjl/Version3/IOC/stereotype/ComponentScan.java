package com.yjl.Version3.IOC.stereotype;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
//@Repeatable(ComponentScans.class)
public @interface ComponentScan {
    String[] value() default {};
}
