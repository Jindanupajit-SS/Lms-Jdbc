package com.smoothstack.jan2020.LmsJDBC.mvc;

import java.util.LinkedHashMap;
import java.util.Map;

public abstract class StringObjectMap extends LinkedHashMap<String, Object> {
    public StringObjectMap(int initialCapacity, float loadFactor) {
        super(initialCapacity, loadFactor);
    }

    public StringObjectMap(int initialCapacity) {
        super(initialCapacity);
    }

    public StringObjectMap() {
    }

    public StringObjectMap(Map<? extends String, ?> m) {
        super(m);
    }

    public StringObjectMap(int initialCapacity, float loadFactor, boolean accessOrder) {
        super(initialCapacity, loadFactor, accessOrder);
    }
}
