package com.smoothstack.jan2020.LmsJDBC.persistence;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface JoinColumn {

    String name();
    String prompt() default "";
    String referencedColumnName() default "";
}
