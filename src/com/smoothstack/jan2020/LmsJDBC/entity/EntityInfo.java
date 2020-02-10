package com.smoothstack.jan2020.LmsJDBC.entity;

import com.smoothstack.jan2020.LmsJDBC.persistence.*;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

public abstract class EntityInfo {

    private static Map<Class<? extends Entity>, List<FieldInfo>> fieldInfoMap = new LinkedHashMap<>();
    private static Map<Class<? extends Entity>, List<String>> allFieldNameMap = new HashMap<>();
    private static Map<Class<? extends Entity>, Field> idField = new HashMap<>();

    public static String tableNameOf(Class<? extends Entity> cls) {
        if (cls.isAnnotationPresent(Table.class))
            return cls.getAnnotation(Table.class).value();
        else
            return cls.getSimpleName();
    }

    public static List<FieldInfo> allFieldInfoOf(Class<? extends Entity> cls) {
        if (fieldInfoMap.containsKey(cls))
            return fieldInfoMap.get(cls);

        List<FieldInfo> fieldInfoList = new ArrayList<>(Arrays.asList(cls.getDeclaredFields())).stream().map(FieldInfo::new)
                .collect(Collectors.toList());

        fieldInfoMap.put(cls,fieldInfoList);

        return fieldInfoList;
    }

    public static List<String> allFieldNameOf(Class<? extends Entity> cls) throws NoSuchFieldException {
        if (allFieldNameMap.containsKey(cls))
            return allFieldNameMap.get(cls);

        String idFieldName = idFieldOf(cls).getName();

        // Do not include @Id
        List<String> allFieldName =  new ArrayList<>(Arrays.asList(cls.getDeclaredFields())).stream().map(Field::getName)
                .filter(k -> !idFieldName.equals(k)).collect(Collectors.toList());

        allFieldNameMap.put(cls, allFieldName);

        return allFieldName;
    }

    public static List<String> getAllColumnNameOf(Class<? extends Entity> cls) throws NoSuchFieldException {


        List<String> allSQLName = new ArrayList<>(allFieldNameOf(cls));

        allSQLName.replaceAll(s-> getColumnNameOf(cls,s));



        return allSQLName;
    }

    public static Field idFieldOf(Class<? extends Entity> cls) throws NoSuchFieldException {

        if (idField.containsKey(cls))
            return idField.get(cls);

        boolean idPresent = false;
        Field field = null;
        for (Field f : cls.getDeclaredFields()) {
            if (f.isAnnotationPresent(Id.class)) {
                if (field != null) {
                    throw new NoSuchFieldException(cls.getSimpleName()+".@Id Ambiguous (more than one @Id annotated)");
                } else {
                    field = f;
                }
            }
        }

        if (field != null) {

            idField.put(cls, field);

            return field;
        }

        throw new NoSuchFieldException(cls.getSimpleName()+".@Id Not found");
    }

    public static String getColumnNameOf(Field field) {
        FieldInfo fieldInfo = new FieldInfo(field);

        return fieldInfo.getColumnName();
    }

    public static String getColumnNameOf(Class cls, String name) {
        Field field = null;
        try {
            field = cls.getDeclaredField(name);
        } catch (NoSuchFieldException e) {
            return name;
        }
        return getColumnNameOf(field);
    }
}
