package com.nd.auxo.recommend.web.controller.v1.course.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.UUID;

/**
 * Created by way on 2016/11/1.
 */
@Data
public class ResourceCount {
    @ApiModelProperty("教材ID")
    private UUID identifier;
    @ApiModelProperty("章节数")
    private Integer chapterCount;

}
