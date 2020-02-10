package com.smoothstack.jan2020.LmsJDBC.mvc;

import java.util.Map;

public class Model extends StringObjectMap {
    public Model(int initialCapacity, float loadFactor) {
        super(initialCapacity, loadFactor);
    }

    public Model(int initialCapacity) {
        super(initialCapacity);
    }

    public Model() {
    }

    public Model(Map<? extends String, ?> m) {
        super(m);
    }

    public Model(int initialCapacity, float loadFactor, boolean accessOrder) {
        super(initialCapacity, loadFactor, accessOrder);
    }
}
