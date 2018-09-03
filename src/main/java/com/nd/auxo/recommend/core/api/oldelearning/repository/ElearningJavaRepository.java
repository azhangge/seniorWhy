package com.nd.auxo.recommend.core.api.oldelearning.repository;

import com.nd.auxo.recommend.core.api.oldelearning.repository.org.UcOrg;
import com.nd.auxo.recommend.core.api.oldelearning.repository.project.Project;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by way on 2016/7/19.
 */
@FeignClient(url = "${elearning.java.service.url}",value = "elearning-java-service")
public interface ElearningJavaRepository {


    /**
     * 获取项目
     *
     * @param projectId
     * @return
     */
    @ApiOperation("获取项目")
    @RequestMapping(value = "/v1/projects/{project_id}", method = RequestMethod.GET)
    Project getProject(@PathVariable("project_id") Long projectId);


    /**
     * 获取组织
     *
     * @param orgName
     * @return
     */
    @ApiOperation("获取组织")
    @RequestMapping(value = "/v1/organizations", method = RequestMethod.GET)
    UcOrg getUcOrgByName(@RequestParam("org_name") String orgName);
}
