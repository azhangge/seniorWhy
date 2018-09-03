package com.nd.auxo.recommend.web.controller.v1;

import com.nd.auxo.recommend.core.api.cloud.CloudCatalog;
import com.nd.auxo.recommend.core.api.cloud.CloudCatalogTree;
import com.nd.auxo.recommend.core.api.cloud.RecommendSdkCloudApi;
import com.nd.auxo.recommend.core.api.cloud.model.UnitCourse;
import com.nd.auxo.recommend.core.api.cloud.model.UnitCourseCreate;
import com.nd.auxo.recommend.web.config.AuxoApplication;
import com.nd.auxo.recommend.web.config.WebMvcConfig;
import com.nd.gaea.core.utils.CollectionUtils;
import com.nd.gaea.core.utils.DateUtils;
import com.nd.gaea.waf.config.WafAutoConfiguration;
import junit.framework.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by way on 2016/10/31.
 */

public class CloudApiTest {

    @InjectMocks
    private RecommendSdkCloudApi cloudApi;

    @Test
    public void test1(){
        Assert.assertTrue(true);
    }

    /**
     * 将树 按深度优先 转化为 list,保持与NDR的章节list的结构一致
     *
     * @param cloudCatalogTree
     * @param catalogList
     * @return
     */
    private void convertCatalogTreeToCatalogList(CloudCatalogTree cloudCatalogTree, List<CloudCatalog> catalogList) {
        catalogList.add(CloudCatalog.convert(cloudCatalogTree));
        List<CloudCatalogTree> children = cloudCatalogTree.getChildren();
        if (CollectionUtils.isEmpty(children)) {
            return;
        }
        for (CloudCatalogTree child : children) {
            convertCatalogTreeToCatalogList(child, catalogList);
        }
    }
}
