/*
package com.nd.auxo.recommend.web.controller.v1;

import com.nd.auxo.recommend.web.config.AuxoApplication;
import com.nd.auxo.recommend.web.config.WebMvcConfig;
import com.nd.auxo.sdk.recommend.vo.banner.BannerVo;
import com.nd.auxo.sdk.recommend.vo.banner.BannerVoForCreate;
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
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

*/
/**
 * @author way
 *         Created on 2016/5/23.
 *//*

public class BannerControllerTest {

    @InjectMocks
    private BannerController bannerController;

    */
/*
     * 不会重复的,便于清除数据
     *//*

    private Long projectId;
    private String customType = "-2";

    private String accessToken = "E379FA2E010AD6629FFBC3147A5F6D6360B1EF1AD3159A99DD6ECC668FBD93CD0C84402523571974";
    private String userId = "2107168459";

    private static final Logger LOGGER = LoggerFactory.getLogger(BannerControllerTest.class);

    @Before
    public void doBefore() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCreate() {
        List<UUID> uuidList = new ArrayList<>();
        try {
            //正向
            BannerVoForCreate create = new BannerVoForCreate();
            String customId = UUID.randomUUID().toString();
            create.setTitle("a");
            create.setCustomType(this.customType);
            create.setCustomId(customId);
            BannerVo bannerVo = this.bannerController.create(create);
            uuidList.add(bannerVo.getId());
            Assert.assertEquals(customId, bannerVo.getCustomId());
            //反向名称重复
            try {
                BannerVoForCreate create2 = new BannerVoForCreate();
                String customId2 = UUID.randomUUID().toString();
                create2.setTitle("b");
                create2.setCustomType(this.customType);
                create2.setCustomId(customId2);
                bannerVo = this.bannerController.create(create2);
                uuidList.add(bannerVo.getId());
            } catch (UnauthorizedException e) {
//                e.printStackTrace();
                LOGGER.debug(" ", e);
                bannerVo = null;
            }
            //update
        } finally {
            //delete
            PageResult<BannerVo> bannerPage = this.bannerController.listPages(0, 10);
            for (UUID vo : uuidList) {
                this.bannerController.delete(vo);
            }
        }
    }

    //@Test
    public void testMove() {
        List<UUID> uuidList = new ArrayList<>();
        try {
            //正向
            BannerVoForCreate create = new BannerVoForCreate();
            String customId = "11";
            create.setTitle("aa");
            create.setCustomType(this.customType);
            create.setCustomId(customId);
            BannerVo bannerVo = this.bannerController.create(create);
            uuidList.add(bannerVo.getId());
            //move
            BannerVoForCreate move = new BannerVoForCreate();
            move.setTitle("move");
            move.setCustomType(this.customType);
            move.setCustomId("move");
            BannerVo moveVo = this.bannerController.create(move);
            uuidList.add(moveVo.getId());


            create = new BannerVoForCreate();
            create.setTitle("b");
            create.setCustomType(this.customType);
            create.setCustomId("b");
            BannerVo vo1 = this.bannerController.create(create);
            uuidList.add(vo1.getId());


            create = new BannerVoForCreate();
            create.setTitle("c");
            create.setCustomType(this.customType);
            create.setCustomId("c");
            BannerVo vo2 =this.bannerController.create(create);
            uuidList.add(vo2.getId());


            this.bannerController.move(moveVo.getId(), 3);

        } finally {
            //delete
            PageResult<BannerVo> bannerPage = this.bannerController.listPages(0, 10);
            for (UUID vo :uuidList) {
                this.bannerController.delete(vo);
            }
        }


    }
}
*/
