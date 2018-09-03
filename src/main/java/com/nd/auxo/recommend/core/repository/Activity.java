package com.nd.auxo.recommend.core.repository;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.annotations.Type;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;
import java.util.UUID;

/**
 * @author way
 *         Created on 2016/7/18.
 */
@Entity
@Table(name = "activity")
@ApiModel(description = "活动")
@Data
public class Activity {

    @Id
    @ApiModelProperty("标识（只读）")
    @Type(type = "uuid-char")
    @Column(name = "id")
    private UUID id;

    @ApiModelProperty("项目标识")
    @Column(name = "project_id")
    private Long projectId;


    @ApiModelProperty("创建时间")
    @Column(name = "create_time")
    private Date createTime;

    @ApiModelProperty("创建人")
    @Column(name = "create_user_id")
    private Long createUserId;

    @ApiModelProperty("修改时间")
    @Column(name = "update_time")
    private Date updateTime;

    @ApiModelProperty("修改人")
    @Column(name = "update_user_id")
    private Long updateUserId;

    public Activity() {
        this.id = UUID.randomUUID();
    }

    @Length(max = 50)
    @NotBlank
    @ApiModelProperty(value = "自定义类型")
    @Column(name = "custom_type")
    private String customType;

    @Length(max = 50)
    @ApiModelProperty(value = "自定义ID")
    @Column(name = "custom_id")
    private String customId;


    @NotBlank
    @Length(max = 50)
    @ApiModelProperty(value = "活动名称", required = true)
    @Column(name = "title")
    private String title;

    @Column(name = "enabled")
    private boolean enabled;

    @ApiModelProperty(value = "活动开始时间")
    @Column(name = "start_time")
    private Date startTime;

    @ApiModelProperty(value = "活动结束时间")
    @Column(name = "end_time")
    private Date endTime;


    @ApiModelProperty(value = "奖励积分")
    @Column(name = "reward_points")
    private Integer rewardPoints;

    @ApiModelProperty(value = "奖励经验")
    @Column(name = "reward_experience")
    private Integer rewardExperience;


    @ApiModelProperty(value = "参加人员类型（1:人员,2:组织,3:部门,0:全部）")
    @Column(name = "join_object_type")
    private Integer joinObjectType;

    @ApiModelProperty(value = "参加人员（参加人员类型1:传普通用户账号；接收对象类型2:传组织id；接收对象类型3:传部门号）")
    @Column(name = "join_object")
    private String joinObject;

    @ApiModelProperty(value = "参加人员组织id")
    @Column(name = "join_object_org_id")
    private Long joinObjectOrgId;

    @ApiModelProperty(value = "活动关联组织的组织类型")
    @Column(name = "join_object_org_type")
    private Integer joinObjectOrgType;

    @ApiModelProperty(value = "活动描述")
    @Column(name = "description")
    private String description;

    @ApiModelProperty(value = "任务类型，1：ERP日常活动，0:PBL活动")
    @Column(name = "task_type")
    private Integer taskType;

    @ApiModelProperty(value = "任务id")
    @Column(name = "task_id")
    private String taskId;

    @ApiModelProperty(value = "最终跳转的cmp地址")
    @Column(name = "target_cmp_url")
    private String targetCmpUrl;


    @ApiModelProperty(value = "活动类型，0:一日三题")
    @Column(name = "activity_type")
    private Integer activityType;

    @ApiModelProperty(value = "活动完成标准,1:参加，2:通过")
    @Column(name = "activity_finish_type")
    private Integer activityFinishType;

    @ApiModelProperty(value = "互动跳转的cmp地址")
    @Column(name = "jump_cmp_url")
    private String jumpCmpUrl;
}
