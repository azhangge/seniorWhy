package com.nd.auxo.recommend.core.api.cloud.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.UUID;

/**
 * Created by way on 2016/10/31.
 */
@Data
@ApiModel("元课程(新增、编辑用)")
public class UnitCourseCreate {
    @ApiModelProperty("标题")
    private String title;
    @ApiModelProperty("简介")
    private String summary;
    @ApiModelProperty("适用人群")
    private String userSuit;
    @ApiModelProperty("关联视频标识")
    private Integer videoId;
    @ApiModelProperty("资源分类标识")
    private Integer resourceCatalogId;
    @ApiModelProperty("课程类型")
    private CourseType courseType;
    @ApiModelProperty("知识体系标识")
    private Integer knowledgeStructId;
    @ApiModelProperty("封面云存储对象标识")
    private Integer frontCoverObjectId;
}
