package com.nd.auxo.recommend.core.repository;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Administrator on 2016/11/4 0004.
 */
@Entity
@Table(name = "recommend_point")
@ApiModel(description = "积分经验补齐")
@Data
public class PointExperience {

    @Id
    @ApiModelProperty("标识（只读）")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ApiModelProperty("项目标识")
    @Column(name = "project_id")
    private Long projectId;

    @ApiModelProperty("平台类型( 0 BPL, 1 ERP)")
    @Column(name = "type")
    private Integer type;

    @ApiModelProperty("授予人员id")
    @Column(name = "reward_user_id")
    private Long rewardUserId;

    @ApiModelProperty("授予人员姓名")
    @Column(name = "reward_user_name")
    private String rewardUserName;

    @ApiModelProperty("操作人员姓名")
    @Column(name = "operate_user_name")
    private String operateUserName;

    @ApiModelProperty("授予积分值")
    @Column(name = "points")
    private Integer points;

    @ApiModelProperty("授予经验值")
    @Column(name = "experiences")
    private Integer experiences;

    @ApiModelProperty("授予状态( 0 失败,1 成功)")
    @Column(name = "status")
    private Integer status;

    @ApiModelProperty("授予时间")
    @Column(name = "reward_time")
    private Date rewardTime;

    @ApiModelProperty("授予原因")
    @Column(name = "reward_description")
    private String rewardDescription;

}
