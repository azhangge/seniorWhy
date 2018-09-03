package com.nd.auxo.recommend.core.constant;

/**
 * Created by way on 2016/7/19.
 * <p/>
 * 人员类型（1:人员,2:组织,3:部门,0:全部）
 */
public enum ObjectType {

    USER(1),
    ORG(2),
    DEPARTMENT(3),
    ALL(0),
    ;
    private Integer value;

    ObjectType(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return this.value;
    }

}
