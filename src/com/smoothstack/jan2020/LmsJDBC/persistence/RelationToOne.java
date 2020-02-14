package com.smoothstack.jan2020.LmsJDBC.persistence;

import com.smoothstack.jan2020.LmsJDBC.Debug;
import com.smoothstack.jan2020.LmsJDBC.entity.Entity;
import com.smoothstack.jan2020.LmsJDBC.entity.EntityInfo;

import java.lang.reflect.InvocationTargetException;

@SuppressWarnings("unchecked")
public class RelationToOne<T extends Entity> extends Relation<T> {

    private T object = null;
    private Class cls;

    public RelationToOne(T object, Class cls) {
        this.object = object;
        this.cls = cls;
    }

    public Object getReferencedValue() throws NoSuchFieldException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        if (object == null) return 0;

        return object.entityGet(EntityInfo.idFieldOf(cls).getName());
    }

    public Class<T> getEntityClass() {
        return (Class<T>) cls;
    }
    public T get() {
        return object;
    }

    @Override
    public String toString() {

        try {
            return getReferencedValue().toString();
        } catch (NoSuchFieldException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            Debug.printException(e);
            return "0";
        }
    }
}

