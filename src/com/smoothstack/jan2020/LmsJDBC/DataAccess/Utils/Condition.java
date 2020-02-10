package com.smoothstack.jan2020.LmsJDBC.DataAccess.Utils;

import com.smoothstack.jan2020.LmsJDBC.entity.Entity;
import com.smoothstack.jan2020.LmsJDBC.entity.FieldInfo;

public class Condition<T extends Entity> {
    public static final int EQUAL = 0;

    private int condition = EQUAL;
    private FieldInfo fieldInfo = null;
    private Object value = null;


    public Condition(int condition, FieldInfo fieldInfo, Object value) {
        this.value = value;
        this.fieldInfo = fieldInfo;
        this.condition = condition;
    }

    public FieldInfo getFieldInfo() {
        return fieldInfo;
    }

    public void setFieldInfo(FieldInfo fieldInfo) {
        this.fieldInfo = fieldInfo;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public int getCondition() {
        return condition;
    }

    public void setCondition(int condition) {
        this.condition = condition;
    }

    public String getMapper() {

        return String.format("%s %s ?", getFieldInfo().getColumnName(), getConditionIdentifier());
    }

    public String getConditionIdentifier() {
        switch (condition) {
            case EQUAL: return value==null?"IS":"=";
            default: throw new UnsupportedOperationException(String.valueOf(condition));
        }
    }
}
