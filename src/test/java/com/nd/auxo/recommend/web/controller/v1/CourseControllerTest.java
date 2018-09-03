/*
package com.nd.auxo.recommend.web.controller.v1;

import com.nd.auxo.recommend.core.service.RecommendCourseService;
import com.nd.auxo.recommend.core.util.JsonUtils;
import com.nd.auxo.recommend.web.config.AuxoApplication;
import com.nd.auxo.recommend.web.config.WebMvcConfig;
import com.nd.auxo.sdk.recommend.vo.course.RecommendCourseVo;
import com.nd.auxo.sdk.recommend.vo.course.RecommendCourseVoForCreate;
import com.nd.auxo.sdk.recommend.vo.course.RecommendCourseVoForUpdate;
import com.nd.gaea.core.exception.BaseRuntimeException;
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
import org.mockito.Mock;
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

import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.when;

*/
/**
 * @author way
 *         Created on 2016/5/23.
 *//*

public class CourseControllerTest {

    @InjectMocks
    private RecommendCourseController recommendCourseController;

    @Mock
    private RecommendCourseService recommendCourseService;

    */
/*
     * 不会重复的,便于清除数据
     *//*

    private Long projectId;
    private String customType = "-2";

    private String accessToken = "E379FA2E010AD6629FFBC3147A5F6D6360B1EF1AD3159A99DD6ECC668FBD93CD0C84402523571974";
    private String userId = "2107168459";

    private static final Logger LOGGER = LoggerFactory.getLogger(CourseControllerTest.class);

    @Before
    public void doBefore() {
        MockitoAnnotations.initMocks(this);

        PageResult<RecommendCourseVo> pageResult = new PageResult<>();
        when(recommendCourseService.listPages(anyLong(),anyInt(),anyInt(),anyInt())).thenReturn(pageResult);
    }

    @Test
    public void testCreate() throws BaseRuntimeException, IOException {
        List<UUID> uuidList = new ArrayList<>();
        try {
            //正向
            RecommendCourseVoForCreate create = new RecommendCourseVoForCreate();
            String customId = "1";
            create.setTitle("a");
            create.setCustomType(this.customType);
            create.setCustomId(customId);
            create.setRecommendMode(0);
            RecommendCourseVo createVo = this.recommendCourseController.create(create);
            uuidList.add(createVo.getId());
            Assert.assertEquals(customId, createVo.getCustomId());
            //反向名称重复
            RecommendCourseVo failVo;
            try {
                RecommendCourseVoForCreate create2 = new RecommendCourseVoForCreate();
                String customId2 = UUID.randomUUID().toString();
                create2.setTitle("a");
                create2.setCustomType(this.customType);
                create2.setCustomId(customId2);
                create2.setRecommendMode(0);
                failVo = this.recommendCourseController.create(create2);
            } catch (UnauthorizedException e) {
//                e.printStackTrace();
                LOGGER.debug(" ", e);
                failVo = null;

            }
            //update
            RecommendCourseVoForUpdate update = new RecommendCourseVoForUpdate();
            JsonUtils.update(update, ObjectUtils.toJson(createVo));
            update.setCustomOrderBy(1);
            update.setStatus(1);
            update.setTitle("b");
            update.setRecommendMode(0);



            RecommendCourseVo updateVo = this.recommendCourseController.update(createVo.getId(), update);

            RecommendCourseVo compareVo = new RecommendCourseVo();
            JsonUtils.update(compareVo, ObjectUtils.toJson(createVo));
            compareVo.setCustomOrderBy(1);
            compareVo.setStatus(1);
            compareVo.setTitle("b");
            compareVo.setUpdateTime(updateVo.getUpdateTime());

            Assert.assertEquals(ObjectUtils.toJson(compareVo), ObjectUtils.toJson(updateVo));


        } finally {
            //delete
            PageResult<RecommendCourseVo> voPage = recommendCourseController.listPages(1326, 0, 10);
            for (UUID temp : uuidList) {
                this.recommendCourseController.delete(temp);
            }
        }
    }

}
*/
