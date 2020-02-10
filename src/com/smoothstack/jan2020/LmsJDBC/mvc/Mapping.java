package com.smoothstack.jan2020.LmsJDBC.mvc;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Mapping {
    String value();
}
