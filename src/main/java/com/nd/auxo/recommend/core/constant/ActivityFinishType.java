package com.nd.auxo.recommend.core.constant;

/**
 * Created by way on 2016/7/21.
 */
public enum ActivityFinishType {

    JOIN(1),
    PASS(2),

    ;
    private Integer value;

    ActivityFinishType(int value){
        this.value = value;
    }

    public Integer getValue(){
        return this.value;
    }
}
