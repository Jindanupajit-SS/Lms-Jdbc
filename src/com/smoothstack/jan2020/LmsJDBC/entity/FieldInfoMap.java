package com.smoothstack.jan2020.LmsJDBC.entity;

import java.lang.reflect.Field;
import java.util.*;

public class FieldInfoMap<T extends Entity> {

    private final static Map<Class<? extends Entity>, FieldInfoMap<? extends Entity>> singletonMap = new HashMap<>();

    private final Class<T> cls;
    private final Map<String, FieldInfo> fieldInfoMapByFieldName = new LinkedHashMap<>();
    private final Map<String, FieldInfo> fieldInfoMapByColumnName = new LinkedHashMap<>();
    private final Set<FieldInfo> idSet = new LinkedHashSet<>();

    protected FieldInfoMap(Class<T> cls) {
        this.cls = cls;
        Arrays.stream(cls.getDeclaredFields()).map(FieldInfo::new)
                .forEach(fieldInfo -> {
                    fieldInfoMapByFieldName.put(fieldInfo.getFieldName(),fieldInfo);
                    fieldInfoMapByColumnName.put(fieldInfo.getColumnName(), fieldInfo);
                    if (fieldInfo.isId())
                        idSet.add(fieldInfo);
                });
    }

    public static FieldInfoMap<? extends Entity> of(Class<? extends Entity> cls) {
        if (singletonMap.containsKey(cls))
            return singletonMap.get(cls);

        singletonMap.put(cls, new FieldInfoMap<>(cls));
        return singletonMap.get(cls);
    }

    public static FieldInfoMap<? extends Entity> of(Entity object) {
        return of(object.getClass());
    }

    public Class<? extends Entity> getEntityClass() {
        return cls;
    }

    public FieldInfo getByFieldName(String fieldName) {
        return fieldInfoMapByFieldName.getOrDefault(fieldName, null);
    }

    public FieldInfo getByColumnName(String fieldName) {
        return fieldInfoMapByColumnName.getOrDefault(fieldName, null);
    }

    public List<String> fieldNameSet() {
        return new ArrayList<>(fieldInfoMapByFieldName.keySet());
    }

    public List<String> columnNameSet() {
        return new ArrayList<>(fieldInfoMapByColumnName.keySet());
    }

    public int size() {
        return fieldInfoMapByFieldName.size();
    }

    public Set<FieldInfo> getIdSet() {
        return idSet;
    }
}
