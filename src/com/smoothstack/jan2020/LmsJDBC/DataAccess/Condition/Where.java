package com.smoothstack.jan2020.LmsJDBC.DataAccess.Condition;

import com.smoothstack.jan2020.LmsJDBC.entity.Entity;
import com.smoothstack.jan2020.LmsJDBC.entity.FieldInfo;

public class Where<T extends Entity> extends Condition<T> {
    public Where(int condition, FieldInfo fieldInfo, Object value) {
        super(condition, fieldInfo, value);
    }
}
