/*
package com.nd.auxo.recommend.web.controller.v1;

import com.nd.auxo.recommend.core.util.JsonUtils;
import com.nd.auxo.recommend.web.config.AuxoApplication;
import com.nd.auxo.recommend.web.config.WebMvcConfig;
import com.nd.auxo.sdk.recommend.vo.tag.RecommendTagVo;
import com.nd.auxo.sdk.recommend.vo.tag.RecommendTagVoForCreate;
import com.nd.auxo.sdk.recommend.vo.tag.RecommendTagVoForUpdate;
import com.nd.gaea.core.utils.ObjectUtils;
import com.nd.gaea.rest.security.authens.*;
import com.nd.gaea.uranus.common.exception.UnauthorizedException;
import com.nd.gaea.uranus.gql.PageResult;
import com.nd.gaea.waf.config.WafAutoConfiguration;
import com.nd.gaea.waf.security.gaea.GaeaContext;
import com.nd.gaea.waf.security.gaea.GaeaToken;
import com.nd.gaea.waf.security.gaea.GaeaUserAuthentication;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

*/
/**
 * @author way
 *         Created on 2016/5/23.
 *//*


public class TagControllerTest {

    @InjectMocks
    private RecommendTagController recommendTagController;

    */
/*
     * 不会重复的,便于清除数据
     *//*

    private Long projectId;
    private String customType = "-2";

    private String accessToken = "E379FA2E010AD6629FFBC3147A5F6D6360B1EF1AD3159A99DD6ECC668FBD93CD0C84402523571974";
    private String userId = "2107168459";
    private static final Logger LOGGER = LoggerFactory.getLogger(TagControllerTest.class);

    @Before
    public void doBefore() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCreate() throws IOException{
        List<UUID> uuidList = new ArrayList<>();
        try {
            //正向
            RecommendTagVoForCreate create = new RecommendTagVoForCreate();
            String customId = "1";
            create.setTitle("a");
            create.setCustomType(this.customType);
            create.setCustomId(customId);
            create.setAppStoreObjectId(UUID.randomUUID());
            create.setStatus(1);
            create.setCustomOrderBy(0);
            RecommendTagVo createVo = this.recommendTagController.create(create);
            uuidList.add(createVo.getId());
            Assert.assertEquals(customId, createVo.getCustomId());
            //第一个新建的排序号应该为0
            Assert.assertEquals(new Integer(0), createVo.getCustomOrderBy());
            //反向名称重复
            RecommendTagVo failVo;
            try {
                RecommendTagVoForCreate create2 = new RecommendTagVoForCreate();
                String customId2 = UUID.randomUUID().toString();
                create2.setTitle("a");
                create2.setCustomType(this.customType);
                create2.setCustomId(customId2);
                create2.setAppStoreObjectId(UUID.randomUUID());
                create2.setStatus(1);
                create2.setCustomOrderBy(0);
                failVo = this.recommendTagController.create(create2);
            } catch (UnauthorizedException e) {
//                e.printStackTrace();
                LOGGER.debug(" ", e);
                failVo = null;
            }
            //update
            RecommendTagVoForUpdate update = new RecommendTagVoForUpdate();
            JsonUtils.update(update, ObjectUtils.toJson(createVo));
            update.setStatus(0);//修改为下线
            UUID updateAppStoreObjectId = UUID.randomUUID();
            update.setAppStoreObjectId(updateAppStoreObjectId);//
            update.setCustomOrderBy(1);

            RecommendTagVo updateVo = this.recommendTagController.update(createVo.getId(), update);
            RecommendTagVo compareVo = new RecommendTagVo();
            JsonUtils.update(compareVo, ObjectUtils.toJson(createVo));
            compareVo.setStatus(0);
            compareVo.setAppStoreObjectId(updateAppStoreObjectId);
            compareVo.setCustomOrderBy(1);
            compareVo.setUpdateTime(updateVo.getUpdateTime());

            Assert.assertEquals(ObjectUtils.toJson(updateVo), ObjectUtils.toJson(compareVo));
        } finally {
            PageResult<RecommendTagVo> voPage = this.recommendTagController.listPages(null, 0, 10);

            //delete
            for (UUID temp : uuidList) {
                this.recommendTagController.delete(temp);
            }
        }
    }

}
*/
