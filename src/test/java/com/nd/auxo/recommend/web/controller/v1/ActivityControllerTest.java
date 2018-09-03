/*
package com.nd.auxo.recommend.web.controller.v1;

import com.nd.auxo.recommend.core.api.erp.ErpCustomCampaignApi;
import com.nd.auxo.recommend.core.api.erp.repository.CustomCampaign;
import com.nd.auxo.recommend.core.constant.ObjectType;
import com.nd.auxo.recommend.web.config.AuxoApplication;
import com.nd.auxo.recommend.web.config.WebMvcConfig;
import com.nd.auxo.recommend.web.controller.v1.ActivityController;
import com.nd.auxo.sdk.recommend.vo.activity.ActivityVo;
import com.nd.auxo.sdk.recommend.vo.activity.ActivityVoForCreate;
import com.nd.gaea.core.utils.ObjectUtils;
import com.nd.gaea.rest.security.authens.*;
import com.nd.gaea.waf.config.WafAutoConfiguration;
import com.nd.gaea.waf.security.gaea.GaeaContext;
import com.nd.gaea.waf.security.gaea.GaeaToken;
import com.nd.gaea.waf.security.gaea.GaeaUserAuthentication;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

*/
/**
 * Created by way on 2016/7/20.
 *//*

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {AuxoApplication.class, WafAutoConfiguration.class, WebMvcConfig.class})
@WebAppConfiguration
public class ActivityControllerTest {

    @Resource
    private ActivityController activityController;

    @Autowired
    private ErpCustomCampaignApi erpCustomCampaignApi;

    */
/*
     * 不会重复的,便于清除数据
     *//*

    private Long projectId = 1326l;
    private String customType = "-2";

    private String accessToken = "E379FA2E010AD6629FFBC3147A5F6D6360B1EF1AD3159A99DD6ECC668FBD93CD0C84402523571974";
    private String userId = "2107168459";

    private List<UUID> list = new ArrayList<>();

    @Before
    public void doBefore() {
        //ToDO 待修改为mock
        GaeaContext.setAppId(projectId);
        GaeaToken gaeaToken = new GaeaToken(accessToken, projectId);
        UserInfo userInfo = new UserInfo();
        userInfo.setUserId(userId);
        UserCenterUserDetails userCenterUserDetails = new UserCenterUserDetails(userInfo, null);
        WafUcCheckToken wafUcCheckToken = new WafUcCheckToken();
        SecurityContextHolder.getContext().setAuthentication(new GaeaUserAuthentication(gaeaToken, new WafUserAuthentication(userCenterUserDetails, wafUcCheckToken, WafAbstractAuthenticationToken.AUTH_TYPE_ALL)));

    }

    @Test
    public void testCreate() {
        Assert.assertTrue(true);
      */
/*  try {
            //正向
            ActivityVoForCreate create = new ActivityVoForCreate();
            create.setEnabled(true);
            create.setCustomId("1");
            create.setCustomType(customType);
            create.setJoinObject("113216@nd");
            create.setJoinObjectType(ObjectType.USER.getValue());//对象类型为用户
            create.setStartTime(new Date());
            create.setEndTime(new Date());
            create.setTitle("test");
            create.setTargetCmpUrl("cmp://xxx");

            ActivityVo activityVo = activityController.create(create);
            list.add(activityVo.getId());
            //update


        } finally {
//            for (UUID id : list) {
//                activityController.delete(id);
//            }

        }*//*

    }

    @Test
    public void test2() {
       */
/* CustomCampaign param = new CustomCampaign();
        param.setOrgId(491036501742l);
        param.setAppKey("26396AA4-6A6A-4693-881A-53673479060E");
        param.setName("test2");
        param.setBeginTime(new Date());
        param.setEndTime(new Date(System.currentTimeMillis() + 100000l));
        param.setWebUri("http://www.test.com");
        param.setMobileUri("cmp://www.test.com");
        param.setEnabled(true);
        param.setAll(false);
        param.setUcUids("113216@ep_003920");

        CustomCampaign result = erpCustomCampaignApi.create(param);
        System.out.println(ObjectUtils.toJson(result));*//*

    }
}
*/
