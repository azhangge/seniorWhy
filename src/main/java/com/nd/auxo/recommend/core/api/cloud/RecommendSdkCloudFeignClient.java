package com.nd.auxo.recommend.core.api.cloud;

import com.nd.auxo.recommend.core.api.cloud.model.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;


/**
 * Created by way on 2016/10/31.
 */
@Api
@FeignClient(url = "${auxo.cloud.url}", value = "auxo-cloud-api")
public interface RecommendSdkCloudFeignClient {

    @ApiOperation("获取元课程树")
    @RequestMapping(value = "/v1/unitcloud/catalog/tree", method = RequestMethod.GET)
    CloudCatalogTree getCatalogTree(@RequestParam("unitId") Integer unitId, @RequestParam("cloud_token") String cloud_token);

    @ApiOperation("获取元课程树")
    @RequestMapping(value = "/v1/unitcloud/unitcourse/reset", method = RequestMethod.GET)
    void restCloudCatalogTree(@RequestParam("unitId") Integer unitId, @RequestParam("cloud_token") String cloud_token);

    @ApiOperation("创建元课程")
    @RequestMapping(value = "/v1/unitcloud/unitcourse/create", method = RequestMethod.POST)
    UnitCourse create(@RequestParam("cloud_token") String cloud_token,
                      @ApiParam("元课程创建对象") @RequestBody UnitCourseCreate unitCourseCreate);


    @ApiOperation("修改元课程")
    @RequestMapping(value = "/v1/unitcloud/unitcourse/update", method = RequestMethod.POST)
    UnitCourse update(@RequestParam("cloud_token") String cloud_token,
                      @RequestParam("unitId") Integer unitId,
                      @ApiParam("元课程创建对象") @RequestBody UnitCourseCreate unitCourseCreate);

    @ApiOperation("获取元课程")
    @RequestMapping(value = "/v1/unitcloud/unitcourse/get", method = RequestMethod.GET)
    UnitCourse get(@RequestParam("cloud_token") String cloud_token,
                      @RequestParam("unitId") Integer unitId
                      );


    @ApiOperation("创建章节 批量")
    @RequestMapping(value = "/v1/unitcloud/catalog/appendbatch", method = RequestMethod.POST)
    void appendBatch(@RequestParam("cloud_token") String cloud_token,
                @RequestParam("unitId") Integer unitId,
                @RequestParam("parentId") Integer parentId,
                @RequestBody CatalogBatchCreate catalogBatchCreate
    );

    @ApiOperation("创建章节")
    @RequestMapping(value = "/v1/unitcloud/catalog/append", method = RequestMethod.GET)
    UnitCourseChapter append(@RequestParam("cloud_token")String cloudToken,
                             @RequestParam("unitId")Integer unitId,
                             @RequestParam("parentId") Integer parentId,
                             @RequestParam("title") String title,
                             @RequestParam("index")Integer index);

    @ApiOperation("添加NDR资源到基础平台")
    @RequestMapping(value = "/unitndr/createbatch", method = RequestMethod.POST)
    void createBatchNDRResource(@RequestParam("cloud_token") String cloud_token,
                      @ApiParam("NDR资源对象") @RequestBody CloudNDRCreate cloudNDRCreate);



}
