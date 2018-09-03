package com.nd.auxo.recommend.core.api.cloud;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * Created by way on 2016/10/31.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class CloudCatalogTree {
    @ApiModelProperty("目录id")
    private Integer id;

    private String title;

    private Integer sortNumber;

    private Integer parentId;

    private Integer status;

    @ApiModelProperty("元课程id")
    private Integer unitId;

    private List<CloudCatalogTree> children;


}
