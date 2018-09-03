package com.nd.auxo.recommend.core.constant;

/**
 * Created by way on 2016/7/19.
 */
public enum TaskType {

    ERP(1),
    PBL(0),;
    private Integer value;

    TaskType(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }

}
