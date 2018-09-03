package com.nd.auxo.recommend.core.api.erp.constant;

/**
 * Created by way on 2016/7/21.
 */
public enum ErpVirtualType {

    /**
     * 1为积分，2为经验
     */
    POINT(1),
    EXP(2),;
    private int value;

    ErpVirtualType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
