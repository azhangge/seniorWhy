package com.nd.auxo.recommend.core.api.oldelearning;

import com.nd.auxo.recommend.core.api.oldelearning.repository.org.UcOrg;
import com.nd.auxo.recommend.core.api.oldelearning.repository.project.Project;

/**
 * Created by way on 2016/7/19.
 */
public interface ElearningProjectApi {


    /**
     * 获取项目
     *
     * @param projectId
     * @return
     */
    Project get(Long projectId);

    /**
     * 获取项目
     *
     * @param orgName
     * @return
     */
    UcOrg getUcOrgByName(String orgName);

}
