package com.nd.auxo.recommend.core.api.cloud.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.UUID;

/**
 * Created by way on 2016/10/31.
 */
@Data
@ApiModel("元课程")
public class UnitCourse {
    @ApiModelProperty("元课程标识")
    private Integer unitId;
    @ApiModelProperty("App标识")
    private Integer appId;
    @ApiModelProperty("App端")
    private Integer appCode;
    @ApiModelProperty("源资源的标题")
    private String title;
    @ApiModelProperty("创建时间")
    private Date createTime;
    @ApiModelProperty("创建人标识")
    private Long createUserId;
    @ApiModelProperty("修改时间")
    private Date updateTime;
    @ApiModelProperty("修改人标识")
    private Long updateUserId;
    @ApiModelProperty("简介")
    private String summary;
    @ApiModelProperty("适用人群")
    private String userSuit;
    @ApiModelProperty("关联视频标识")
    private UUID videoId;
    @ApiModelProperty("课程类型")
    private CourseType courseType;
    @ApiModelProperty("知识体系标识")
    private Integer knowledgeStructId;
    @ApiModelProperty("封面云存储对象标识")
    private Integer frontCoverObjectId;
    @ApiModelProperty("NDR元课程标识")
    private String identity;
}
