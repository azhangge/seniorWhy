package com.nd.auxo.recommend.core.api.pbl.repository;

import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by way on 2016/7/19.
 *
 * 注意pbl只有预生产生产能用
 *
 */
@FeignClient(url = "${pbl.server.url}", value = "pbl-api")
public interface PblRepository {

    /**
     * 一日三题，事件接口
     */
    @ApiOperation("创建")
    @RequestMapping(value = "/v0.9/app/{appName}/trigger/event/doOneDay3Questions", method = RequestMethod.POST)
    PblResponse doOneDay3Questions(@PathVariable("appName") String appName, @RequestBody TriggerEvent triggerEvent);

}
