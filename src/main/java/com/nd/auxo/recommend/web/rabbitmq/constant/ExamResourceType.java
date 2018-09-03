package com.nd.auxo.recommend.web.rabbitmq.constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * <p>测评消息类型</p>
 *
 * @author  zhangchh
 * @date    2016/7/18
 * @version latest
 */
public enum ExamResourceType {

    EXAM(1),      // 考试
    USER_EXAM(2); // 用户考试

    private int value;

    ExamResourceType(int value) {
        this.value = value;
    }

    @JsonValue
    public int getValue() {
        return value;
    }

    @JsonCreator
    public static ExamResourceType valueOf(int value){
        for (ExamResourceType it : ExamResourceType.values()) {
            if(it.getValue() == value)
                return it;
        }
        return null;
    }
}
