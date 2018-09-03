package com.nd.auxo.recommend.core.api.cloud.model;

import com.nd.gaea.core.utils.jackson.UserEnumType;

/**
 * Created by way on 2016/10/31.
 */
public enum ResourceStatus implements UserEnumType {
    //编辑
    CREATE(0),
    //就绪
    READY(1),
    //禁用
    DISABLED(2),
    //过期
    EXPIRED(3),
    //转码中
    ENCODING(4),
    //同步中
    SYNC(5),
    //草稿
    DRAFT(6),
    //删除
    DELETE(7),
    //错误
    ERROR(9);

    private int value;

    ResourceStatus(int value) {
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
