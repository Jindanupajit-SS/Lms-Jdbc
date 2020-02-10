package com.smoothstack.jan2020.LmsJDBC.DataAccess.Utils;

public class OneTable extends Table {

    private String name;

    protected OneTable(String name) {
        this.name = name;
    }

    public static OneTable of(String name) {
        return new OneTable(name);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return getName();
    }
}
