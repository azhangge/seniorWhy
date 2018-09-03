package com.nd.auxo.recommend.core.api.cloud;

import com.nd.auxo.recommend.core.api.cloud.model.*;
import com.nd.gaea.core.exception.BaseRuntimeException;
import com.nd.gaea.uranus.common.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * Created by way on 2016/10/31.
 */
@Service
@Slf4j
public class RecommendSdkCloudApi {

    @Autowired
    private RecommendSdkCloudClient cloudClient;


    public CloudCatalogTree getCatalogTree(Integer unitId, String cloudToken,boolean mqNotice) {
        CloudCatalogTree cloudCatalogTree = cloudClient.getCatalogTree(unitId
                , cloudToken,mqNotice);
        return cloudCatalogTree;
    }

    public void restCloudCatalogTree(Integer unitId, String cloudToken,boolean mqNotice) {
        try{
            cloudClient.restCloudCatalogTree(unitId, cloudToken,mqNotice);
        }catch (BaseRuntimeException e){
            log.error("",e);
            throw new BusinessException("章节下可能有非NDR资源,不允许重新导入");
        }
    }

    public void updateCatalog(Integer unitId, String cloudToken,Integer catalogId,String title, boolean mqNotice){
        cloudClient.updateCatalog(unitId,cloudToken,mqNotice,catalogId,title);
    }

    public UnitCourse create(UnitCourseCreate unitCourseCreate, String cloudToken,boolean mqNotice) {
        UnitCourse unitCourse = cloudClient.create(cloudToken
                , unitCourseCreate,mqNotice);
        return unitCourse;
    }

    public UnitCourse update(UnitCourseCreate unitCourseCreate, String cloudToken, Integer unitId,boolean mqNotice) {
        UnitCourse unitCourse = cloudClient.update(cloudToken, unitId
                , unitCourseCreate,mqNotice);
        return unitCourse;
    }

    public UnitCourse get(String cloudToken, Integer unitId,boolean mqNotice) {
        UnitCourse unitCourse = cloudClient.get(cloudToken, unitId,mqNotice
        );
        return unitCourse;
    }

    public void appendBatch(Integer unitId, Integer parentId, List<String> titles, String cloudToken,boolean mqNotice) {
        CatalogBatchCreate catalogBatchCreate = new CatalogBatchCreate();
        catalogBatchCreate.setTitles(titles);
        cloudClient.appendBatch(cloudToken, unitId, parentId, catalogBatchCreate,mqNotice);
    }


    public void createBatchNDRResource(String cloud_token,
                                       CloudNDRCreate cloudNDRCreate,boolean mqNotice) {
        cloudClient.createBatchNDRResource(cloud_token, cloudNDRCreate,mqNotice);
    }

}
