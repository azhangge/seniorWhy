package com.nd.auxo.recommend.core.api.log;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author way
 *         Created on 2016/6/14.
 */
@Api
@FeignClient(url = "${auxo.log.manage.url}", value = "log-manage-api")
public interface RecommendSdkLogManageApi {

    @ApiOperation("创建日志")
    @RequestMapping(value = "/v1/logs", method = RequestMethod.POST)
    Log create(@ApiParam("日志信息") @RequestBody LogForCreate create);
}
