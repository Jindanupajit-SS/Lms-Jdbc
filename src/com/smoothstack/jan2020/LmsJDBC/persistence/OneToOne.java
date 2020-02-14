package com.smoothstack.jan2020.LmsJDBC.persistence;

import com.smoothstack.jan2020.LmsJDBC.entity.Entity;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface OneToOne {
    Class value();
}
