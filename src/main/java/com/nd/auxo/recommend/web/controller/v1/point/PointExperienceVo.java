package com.nd.auxo.recommend.web.controller.v1.point;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * Created by Administrator on 2016/11/4 0004.
 */
@Data
public class PointExperienceVo {

    @ApiModelProperty("授予人员姓名")
    @JsonProperty("reward_user_name")
    private String rewardUserName;

    @ApiModelProperty("操作人员姓名")
    @JsonProperty("operate_user_name")
    private String operateUserName;

    @ApiModelProperty("授予积分值")
    private Integer points;

    @ApiModelProperty("授予经验值")
    private Integer experiences;

    @ApiModelProperty("授予状态(0 失败, 1 成功)")
    private Integer status;

    @ApiModelProperty("平台类型( 0 BPL, 1 ERP)")
    private Integer type;

    @ApiModelProperty("授予时间")
    @JsonProperty("reward_time")
    private Date rewardTime;

    @ApiModelProperty("授予原因说明")
    @JsonProperty("reward_description")
    private String rewardDescription;

    @ApiModelProperty("授予人员ids(用逗号隔开)")
    @JsonProperty("reward_object")
    private String rewardObject;

    @ApiModelProperty("操作权限验证")
    private String auth;

    @ApiModelProperty("送积分失败(id用逗号隔开)")
    private String failedPoints;

    @ApiModelProperty("送经验失败(id用逗号隔开)")
    private String failedExp;

}
