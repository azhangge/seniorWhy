package com.nd.auxo.recommend.core.api.erp.constant;

/**
 * Created by way on 2016/7/21.
 */
public enum EreResponseResultType {

    SUCCESS(1);

    private int value;

    EreResponseResultType(int value){
        this.value = value;
    }
    public int getValue(int value){
        return value;
    }
}
