package com.smoothstack.jan2020.LmsJDBC.mvc;

import java.util.Map;

public class RequestParam extends StringObjectMap {
    public RequestParam(int initialCapacity, float loadFactor) {
        super(initialCapacity, loadFactor);
    }

    public RequestParam(int initialCapacity) {
        super(initialCapacity);
    }

    public RequestParam() {
    }

    public RequestParam(Map<? extends String, ?> m) {
        super(m);
    }

    public RequestParam(int initialCapacity, float loadFactor, boolean accessOrder) {
        super(initialCapacity, loadFactor, accessOrder);
    }
}
