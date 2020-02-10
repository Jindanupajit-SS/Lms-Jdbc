package com.smoothstack.jan2020.LmsJDBC.entity;

import com.smoothstack.jan2020.LmsJDBC.persistence.OneToOne;
import com.smoothstack.jan2020.LmsJDBC.persistence.RelationToOne;
import com.sun.corba.se.impl.io.TypeMismatchException;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.LinkedHashMap;
import java.util.Map;

public interface Entity<T extends Entity> extends Serializable {

    /**
     * Getter using reflection
     *
     * Required proper access modifier and regular declaired getter method
     *
     * @param fieldName filed name
     * @return value returned from get[field Name]();
     * @throws NoSuchFieldException
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    default Object entityGet(String fieldName) throws NoSuchFieldException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        if (fieldName.length() == 0)
            throw new NoSuchFieldException("<empty>");

        Field field = this.getClass().getDeclaredField(fieldName);

        Method getter = this.getClass().getDeclaredMethod("get"+fieldName.substring(0, 1).toUpperCase()
                + (fieldName.length()>1?fieldName.substring(1):""));

        return (field.isAnnotationPresent(OneToOne.class))?
                new RelationToOne<T>((T) getter.invoke(this)).getReferencedValue()
                : getter.invoke(this);

    }

    default Map<String, Object> entityGetAll() throws NoSuchFieldException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {

        Map<String, Object> fieldMap = new LinkedHashMap<>();

        for (Field field : this.getClass().getDeclaredFields()) {
            int modifier = field.getModifiers();
            if (Modifier.isTransient(modifier))
                continue;

            String fieldName = field.getName();

            try {
                Method getter = this.getClass().getDeclaredMethod("get" + fieldName.substring(0, 1).toUpperCase()
                        + (fieldName.length() > 1 ? fieldName.substring(1) : ""));

                if (Modifier.isPublic(getter.getModifiers())) {
                    Object value = (field.isAnnotationPresent(OneToOne.class))?
                            new RelationToOne<T>((T) getter.invoke(this)).getReferencedValue()
                            : getter.invoke(this);
                    fieldMap.put(fieldName, value);
                }
            } catch (NoSuchMethodException ignored) {}
        }
        return fieldMap;

    }


    default void entitySet(String fieldName, Object value) throws NoSuchFieldException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {

       if (fieldName.length() == 0)
            throw new NoSuchFieldException("<empty>");

       Field field = this.getClass().getDeclaredField(fieldName);


       Method setter = this.getClass().getDeclaredMethod("set"+fieldName.substring(0, 1).toUpperCase()
               + (fieldName.length()>1?fieldName.substring(1):""), value.getClass());


       if (value.getClass().equals(field.getType()) && setter.getReturnType().equals(void.class))
                setter.invoke(this, value);
       else
           throw new TypeMismatchException(fieldName);
    }

    default String entityDump()  {
        final StringBuilder sb = new StringBuilder(this.getClass().getSimpleName());
        sb.append("{");
        StringBuilder sbField = new StringBuilder();
        try {
            for (Field field : this.getClass().getDeclaredFields()) {
                String singleQuote = field.getType().equals(String.class)?"'":"";
                sbField .append(",")
                        .append(field.getName())
                        .append("=")
                        .append(singleQuote)
                        .append(this.entityGet(field.getName()))
                        .append(singleQuote);
            }
        } catch (NoSuchFieldException | NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            //e.printStackTrace();
            sbField = new StringBuilder(" <exception-thrown>");
        }
        if (sbField.length()>0) sb.append(sbField.substring(1));
        sb.append('}');
        return sb.toString();
    }


}
