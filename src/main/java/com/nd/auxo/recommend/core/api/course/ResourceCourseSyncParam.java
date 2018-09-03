package com.nd.auxo.recommend.core.api.course;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.UUID;

/**
 * Created by way on 2016/10/31.
 */
@Data
@ApiModel(description = "资源课程同步参数")
public class ResourceCourseSyncParam {
    public static final int SOURCE_CLOUD = 1;

    @ApiModelProperty(value = "同步来源（目前只支持cloud）", allowableValues = "1-cloud")
    @NotNull
    @Max(1)
    @Min(1)
    private Integer source;
    @NotNull
    private UUID sourceId;
}
