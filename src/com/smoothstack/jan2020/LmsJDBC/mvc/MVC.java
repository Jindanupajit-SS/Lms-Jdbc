package com.smoothstack.jan2020.LmsJDBC.mvc;

import java.lang.reflect.Method;

public class MVC {

    public static String template(Method method) {
        if (method.isAnnotationPresent(com.smoothstack.jan2020.LmsJDBC.mvc.Template.class)) {
            return method.getAnnotation(com.smoothstack.jan2020.LmsJDBC.mvc.Template.class).value();
        } else
            throw new RuntimeException(method.getDeclaringClass().getSimpleName()+"::"+method.getName()+ " is NOT a template");

    }

    public static String controller(Method method) {
        if (method.isAnnotationPresent(com.smoothstack.jan2020.LmsJDBC.mvc.Mapping.class)) {
            return method.getAnnotation(com.smoothstack.jan2020.LmsJDBC.mvc.Mapping.class).value();
        } else
            throw new RuntimeException(method.getDeclaringClass().getSimpleName()+"::"+method.getName()+ " is NOT a controller");

    }

    public static String redirect(Method method) {
        return "redirect:"+controller(method);
    }
}
