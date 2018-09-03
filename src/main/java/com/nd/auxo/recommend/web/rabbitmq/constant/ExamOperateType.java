package com.nd.auxo.recommend.web.rabbitmq.constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * <p>测评操作类型</p>
 *
 * @author  zhangchh
 * @date    2016/7/18
 * @version latest
 */
public enum ExamOperateType {

    CREATE(0),  // 新增
    UPDATE(1),  // 修改
    DELETE(2),  // 删除
    IMPORT(3),  // 导入
    START(100), // 开始
    FINISH(200);// 结束

    private int value;

    ExamOperateType(int value) {
        this.value = value;
    }

    @JsonValue
    public int getValue() {
        return value;
    }

    @JsonCreator
    public static ExamOperateType valueOf(int value){
        for (ExamOperateType it : ExamOperateType.values()) {
            if(it.getValue() == value)
                return it;
        }
        return null;
    }
}
