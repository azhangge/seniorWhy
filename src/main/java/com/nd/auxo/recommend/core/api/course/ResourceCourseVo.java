package com.nd.auxo.recommend.core.api.course;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.sql.Timestamp;
import java.util.UUID;

/**
 * Created by way on 2016/10/31.
 */
@Data
@ApiModel(description = "资源课程")
public class ResourceCourseVo {
    @ApiModelProperty("标识")
    private UUID id;
    @ApiModelProperty("项目标识")
    private Long projectId;
    @ApiModelProperty(value = "课程名称")
    private String name;
    @ApiModelProperty("介绍")
    private String summary;
    @ApiModelProperty("课程适合人群")
    private String userSuit;
    @ApiModelProperty("封面")
    private UUID frontCoverObjectId;
    @ApiModelProperty("使用云存储的封面图片url")
    private String frontCoverUrl;
    @ApiModelProperty("业务课程最新上线时间")
    private Timestamp statusUpdateTime;
    @ApiModelProperty("有效报名总人数")
    private Integer accessTotalCount;
    @ApiModelProperty("视频个数")
    private Integer videoCount;
    @ApiModelProperty("视频时长")
    private Integer videoDuration;
    @ApiModelProperty("文档个数")
    private Integer documentCount;
    @ApiModelProperty("文档页数")
    private Integer documentPageCount;
    @ApiModelProperty("习题个数")
    private Integer exerciseCount;
    @ApiModelProperty("习题题目个数")
    private Integer exerciseQuestionCount;
    @ApiModelProperty("url个数")
    private Integer urlCount;
    @ApiModelProperty("vr个数")
    private Integer vrCount;
}

