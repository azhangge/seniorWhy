package com.nd.auxo.recommend.core.api.log;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by way on 2016/11/17.
 */
@Data
public class LogForCreate {


    @ApiModelProperty("日志logger")
    private String logger;

    @ApiModelProperty("日志信息")
    private String message;


    @ApiModelProperty("请求ip")
    private String ipAddress;

    @ApiModelProperty("请求uri")
    private String uri;


    @ApiModelProperty("请求参数")
    private String param;


    @ApiModelProperty("请求方式")
    private String method;

    @ApiModelProperty("来源类型 1--项目 ,2--系统")
    private Integer sourceType;

    public static final Integer SOURCE_TYPE_SYSTEM = 2;


}
