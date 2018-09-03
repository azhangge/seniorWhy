package com.nd.auxo.recommend.web.rabbitmq.message;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

/**
 * Created by Administrator on 2016/12/23 0023.
 */
@Data
public class RecommendKVMessage implements Serializable {

    @ApiModelProperty("参数id")
    private UUID KVId;
    @ApiModelProperty("参数key")
    private String KVKey;
    @ApiModelProperty("操作类型")
    private String operateType;
    @ApiModelProperty("操作时间")
    private Date operateTime;
    @ApiModelProperty("参数隔离类型")
    private Integer isolationStrategy;
    @ApiModelProperty("操作人id")
    private Long userId;
}
