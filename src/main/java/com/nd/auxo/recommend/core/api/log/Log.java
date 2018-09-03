package com.nd.auxo.recommend.core.api.log;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

/**
 * Created by tzp on 16-5-11.
 */
@Data
@ApiModel(description = "日志")
public class Log {

    @Id
    @ApiModelProperty("标识（只读）")
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;

    @ApiModelProperty("项目标识")

    private String projectId;

    @ApiModelProperty("用户标识")

    private String userId;

    @ApiModelProperty("日志记录时间")

    private Date logDate;

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


}
