package com.smoothstack.jan2020.LmsJDBC.entity;

import com.smoothstack.jan2020.LmsJDBC.persistence.Column;
import com.smoothstack.jan2020.LmsJDBC.persistence.Id;
import com.smoothstack.jan2020.LmsJDBC.persistence.JoinColumn;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Objects;

public class FieldInfo{
    private final Class<? extends Entity> cls;
    private final Field field;

    public FieldInfo(final Field field) {

        this.field = Objects.requireNonNull(field);
        this.cls = (Class<? extends Entity>) Objects.requireNonNull(field.getDeclaringClass());
    }

    public Class<? extends Entity> getCls() {
        return cls;
    }

    public Field getField() {
        return field;
    }

    public String getEntityName() {
        return cls.getSimpleName();
    }

    public String getFieldName() {
        return field.getName();
    }

    public String getColumnName() {

        if (field.isAnnotationPresent(Column.class))
            return field.getAnnotation(Column.class).name();

        if (field.isAnnotationPresent(JoinColumn.class))
            return field.getAnnotation(JoinColumn.class).name();

        return field.getName();
    }

    public boolean is(Class annotation) {
        return field.isAnnotationPresent(annotation);
    }

    public boolean isId() {
        return is(Id.class);
    }

    public Object getValue(Entity entity) throws NoSuchFieldException {
        try {
            return entity.entityGet(field.getName());
        }  catch (NoSuchMethodException e) {
            System.err.printf("Must define getter for '%s.%s'", cls.getSimpleName(), field.getName());
            throw new NoSuchFieldException(e.getMessage());
        } catch (InvocationTargetException e) {
            System.err.printf("Could not invoke getter for '%s.%s'", cls.getSimpleName(), field.getName());
            throw new NoSuchFieldException(e.getMessage());
        } catch (IllegalAccessException e) {
            System.err.printf("Must define getter for '%s.%s' as public", cls.getSimpleName(), field.getName());
            throw new NoSuchFieldException(e.getMessage());
        }
    }

    public void setValue(Entity entity, Object value) throws NoSuchFieldException {
        try {
            entity.entitySet(field.getName(), value);
        }  catch (NoSuchMethodException e) {
            System.err.printf("Must define setter for '%s.%s'", cls.getSimpleName(), field.getName());
            throw new NoSuchFieldException(e.getMessage());
        } catch (InvocationTargetException e) {
            System.err.printf("Could not invoke setter for '%s.%s'", cls.getSimpleName(), field.getName());
            throw new NoSuchFieldException(e.getMessage());
        } catch (IllegalAccessException e) {
            System.err.printf("Must define setter for '%s.%s' as public", cls.getSimpleName(), field.getName());
            throw new NoSuchFieldException(e.getMessage());
        }
    }
}
