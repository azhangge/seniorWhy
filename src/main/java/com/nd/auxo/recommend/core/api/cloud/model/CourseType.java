package com.nd.auxo.recommend.core.api.cloud.model;

import com.nd.gaea.core.utils.jackson.UserEnumType;

/**
 * Created by way on 2016/10/31.
 */
public enum CourseType  implements UserEnumType {
    NORMAL(0),EXAM(1);

    private int value;

    CourseType(int value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    public Integer getValue() {
        return value;
    }
}
