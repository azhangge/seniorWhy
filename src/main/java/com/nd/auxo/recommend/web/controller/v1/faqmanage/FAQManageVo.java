package com.nd.auxo.recommend.web.controller.v1.faqmanage;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.UUID;

/**
 * Created by Administrator on 2016/11/14 0014.
 */
@Data
public class FAQManageVo {

    @ApiModelProperty("标识（只读）")
    private UUID id;

    @ApiModelProperty("问题类型")
    private Integer  questionType;

    @ApiModelProperty("问题名称")
    private String questionName;

    @ApiModelProperty("问题回答")
    private String questionAnswer;

    @ApiModelProperty("自定义类型")
    private String  customType;

    @ApiModelProperty("问题创建时间")
    private Date createTime;

    @ApiModelProperty("问题排序号")
    private Integer sortNumber;
}
