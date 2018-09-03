package com.nd.auxo.recommend.core.constant;

/**
 * uc组织类型
 * Created by linf on 2016/12/5.
 */
public enum OrganizationType {

    REAL(0),
    VIRTUAL(1),;
    private Integer value;

    OrganizationType(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }

}
