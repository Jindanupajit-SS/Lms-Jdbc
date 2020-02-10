package com.smoothstack.jan2020.LmsJDBC.DataAccess.Utils;

import com.smoothstack.jan2020.LmsJDBC.entity.Entity;
import com.smoothstack.jan2020.LmsJDBC.entity.EntityInfo;

public abstract class Table {

    public static OneTable of (String name) {
        return OneTable.of(name);
    }

    public JoinTable join(Table right) {
        return join(this, right);
    }

    public JoinTable joinLeft(Table left) {
        return joinLeft(left, this);
    }

    public JoinTable joinRight(Table right) {
        return joinRight(this, right);
    }

    public static OneTable of (Class<? extends Entity> cls) {
        return of(EntityInfo.tableNameOf(cls));
    }

    public static OneTable of (Entity object) {
        return of(EntityInfo.tableNameOf(object.getClass()));
    }

    public static JoinTable join(Table left, Table right) {
        return JoinTable.inner(left, right);
    }

    public static JoinTable joinLeft(Table left, Table right) {
        return JoinTable.left(left, right);
    }

    public static JoinTable joinRight(Table left, Table right) {
        return JoinTable.right(left, right);
    }


}
