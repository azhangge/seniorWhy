package com.nd.auxo.recommend.core.api.cloud;

import com.nd.auxo.recommend.core.api.cloud.model.*;
import com.nd.auxo.recommend.core.api.erp.repository.ErpResponse;
import com.nd.gaea.client.http.WafHttpClient;
import com.nd.gaea.client.http.WafSecurityHttpClient;
import com.nd.gaea.client.support.WafClientContextHolder;
import com.nd.gaea.waf.security.gaea.GaeaContext;
import com.nd.gaea.waf.security.gaea.GaeaToken;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by way on 2016/10/31.
 */
@Service
public class RecommendSdkCloudClient {

    @Value("${auxo.cloud.url}")
    private String cloudUrl;

    private WafHttpClient wafHttpClient = new WafHttpClient(30000,30000);

    private static final String HEADER_MQ = "_cloud_mqstop_";

    private static final String URL_MQ = "&_cloud_mqstop_=1";


    /**
     * 获取元课程树
     *
     * @param unitId
     * @param cloudToken
     * @return
     */
    public CloudCatalogTree getCatalogTree(Integer unitId, String cloudToken, boolean mqNotice) {
        String url = cloudUrl + "/v1/unitcloud/catalog/tree?unitId=" + unitId + "&cloud_token=" + cloudToken;
        url = addMq(url,mqNotice);
        ResponseEntity<CloudCatalogTree> responseEntity = wafHttpClient.executeForEntity(url, HttpMethod.GET, handleMqHeader(mqNotice), CloudCatalogTree.class);
        return responseEntity.getBody();
    }




    /**
     * 重置元课程树
     *
     * @param unitId
     * @param cloudToken
     * @param mqNotice
     */
    public void restCloudCatalogTree(Integer unitId, String cloudToken, boolean mqNotice) {
        String url = cloudUrl + "/v1/unitcloud/unitcourse/reset?unitId=" + unitId + "&cloud_token=" + cloudToken;
        url = addMq(url,mqNotice);
        wafHttpClient.execute(url, HttpMethod.GET, handleMqHeader(mqNotice));
    }

    /**
     * 修改元课程树目录名称
     */
    public void updateCatalog(Integer unitId, String cloudToken, boolean mqNotice,Integer catalogId,String title){
        String url = cloudUrl + "/v1/unitcloud/catalog/update?unitId=" + unitId + "&cloud_token=" + cloudToken
                +"&catalogId="+catalogId+"&title="+title;
        url = addMq(url,mqNotice);
        wafHttpClient.execute(url, HttpMethod.GET, handleMqHeader(mqNotice));
    }



    /**
     * 创建元课程
     *
     * @param cloudToken
     * @param unitCourseCreate
     * @param mqNotice
     * @return
     */
    public UnitCourse create(String cloudToken,
                             UnitCourseCreate unitCourseCreate, boolean mqNotice) {
        String url = cloudUrl + "/v1/unitcloud/unitcourse/create?cloud_token=" + cloudToken;
        url = addMq(url,mqNotice);
        ResponseEntity<UnitCourse> responseEntity = wafHttpClient.executeForEntity(url, HttpMethod.POST, handleMqHeader(mqNotice, unitCourseCreate), UnitCourse.class);
        return responseEntity.getBody();
    }


    /**
     * 修改元课程
     *
     * @param cloudToken
     * @param unitId
     * @param mqNotice
     * @param unitCourseUpdate
     * @return
     */
    public UnitCourse update(String cloudToken,
                             Integer unitId,
                             UnitCourseCreate unitCourseUpdate,
                             boolean mqNotice) {
        String url = cloudUrl + "/v1/unitcloud/unitcourse/update?unitId=" + unitId + "&cloud_token=" + cloudToken;
        url = addMq(url,mqNotice);
        ResponseEntity<UnitCourse> responseEntity = wafHttpClient.executeForEntity(url, HttpMethod.POST, handleMqHeader(mqNotice, unitCourseUpdate), UnitCourse.class);
        return responseEntity.getBody();
    }

    /**
     * 获取元课程
     *
     * @param cloudToken
     * @param unitId
     * @return
     */
    @RequestMapping(value = "/v1/unitcloud/unitcourse/get", method = RequestMethod.GET)
    public UnitCourse get(@RequestParam("cloud_token") String cloudToken,
                          @RequestParam("unitId") Integer unitId, boolean mqNotice
    ) {
        String url = cloudUrl + "/v1/unitcloud/unitcourse/get?unitId=" + unitId + "&cloud_token=" + cloudToken;
        url = addMq(url,mqNotice);
        ResponseEntity<UnitCourse> responseEntity = wafHttpClient.executeForEntity(url, HttpMethod.GET, handleMqHeader(mqNotice), UnitCourse.class);
        return responseEntity.getBody();
    }


    /**
     * 创建章节 批量
     *
     * @param cloudToken
     * @param unitId
     * @param parentId
     * @param catalogBatchCreate
     * @param mqNotice
     */
    public void appendBatch(String cloudToken,
                            Integer unitId,
                            Integer parentId,
                            CatalogBatchCreate catalogBatchCreate
            , boolean mqNotice
    ) {
        String url = cloudUrl + "/v1/unitcloud/catalog/appendbatch?unitId=" + unitId + "&cloud_token=" + cloudToken + "&parentId=" + parentId;
        url = addMq(url,mqNotice);
        wafHttpClient.execute(url, HttpMethod.POST, handleMqHeader(mqNotice, catalogBatchCreate));
    }


    /**
     * 添加NDR资源到基础平台
     *
     * @param cloudToken
     * @param cloudNDRCreate
     */
    public void createBatchNDRResource(String cloudToken,
                                       CloudNDRCreate cloudNDRCreate
            , boolean mqNotice) {
        String url = cloudUrl + "/unitndr/createbatch?cloud_token=" + cloudToken;
        url = addMq(url,mqNotice);
        wafHttpClient.execute(url, HttpMethod.POST, handleMqHeader(mqNotice, cloudNDRCreate));
    }


    /**
     * 设置mq头
     *
     * @param mqNotice
     */
    private HttpEntity handleMqHeader(boolean mqNotice) {
        MultiValueMap<String, String> headers = new HttpHeaders();
        headers.set("Accept", "application/sdp+json");
        if (!mqNotice) {
            headers.set(HEADER_MQ, "true");
        }
        HttpEntity requestEntity = new HttpEntity<>(headers);
        return requestEntity;
    }

    /**
     * 设置mq头
     *
     * @param mqNotice
     */
    private HttpEntity handleMqHeader(boolean mqNotice, Object body) {
        MultiValueMap<String, String> headers = new HttpHeaders();
        headers.set("Accept", "application/sdp+json");
        if (!mqNotice) {
            headers.set(HEADER_MQ, "true");
        }
        HttpEntity requestEntity = new HttpEntity<>(body, headers);
        return requestEntity;
    }

    private String addMq(String url, boolean mqNotice) {
        if (mqNotice) {
            return url;
        }
        return url+URL_MQ;
    }
}
