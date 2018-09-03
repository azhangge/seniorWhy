package com.nd.auxo.recommend.core.api.course;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by way on 2016/10/31.
 */
@Api
@FeignClient(url = "${auxo.course.url}", value = "course-api")
public interface RecommendSdkCourseApi
{
    @ApiOperation("从其他来源同步资源课程")
    @RequestMapping(value = "/v1/resource_courses/sync", method = RequestMethod.POST)
    ResourceCourseVo sync(@ApiParam("资源课程") @RequestBody ResourceCourseSyncParam resourceCourseSyncParam);
}
