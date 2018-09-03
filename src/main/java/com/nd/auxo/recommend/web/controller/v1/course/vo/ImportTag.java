package com.nd.auxo.recommend.web.controller.v1.course.vo;

import io.swagger.annotations.ApiParam;
import lombok.Data;

/**
 * Created by way on 2016/10/31.
 */
@Data
public class ImportTag {
    @ApiParam("标签名称 对应taxonname")
    private String name;
    @ApiParam("标签路径 对应taxonpath")
    private String path;
    @ApiParam("标签code 对应taxoncode")
    private String code;
}
