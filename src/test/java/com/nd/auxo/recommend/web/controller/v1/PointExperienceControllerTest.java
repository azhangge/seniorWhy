package com.nd.auxo.recommend.web.controller.v1;

import com.nd.auxo.recommend.core.api.erp.ErpCustomCampaignApi;
import com.nd.auxo.recommend.core.api.erp.constant.ErpVirtualType;
import com.nd.auxo.recommend.core.api.erp.repository.ErpResponse;
import com.nd.auxo.recommend.core.api.uc.UcApi;
import com.nd.auxo.recommend.core.repository.PointExperience;
import com.nd.auxo.recommend.core.repository.PointExperienceRepository;
import com.nd.auxo.recommend.core.service.internal.PointExperienceServiceImpl;
import com.nd.auxo.recommend.core.util.MD5Util;
import com.nd.auxo.recommend.web.config.AuxoApplication;
import com.nd.auxo.recommend.web.config.WebMvcConfig;
import com.nd.auxo.recommend.web.controller.v1.point.PointExperienceVo;
import com.nd.elearning.sdk.uc.bean.UcOrgExInfo;
import com.nd.elearning.sdk.uc.bean.UcUser;
import com.nd.elearning.sdk.uc.bean.UserInfoVo;
import com.nd.elearning.sdk.uc.repository.UcUserRepository;
import com.nd.gaea.rest.security.authens.*;
import com.nd.gaea.uranus.gql.PagerResult;
import com.nd.gaea.waf.config.WafAutoConfiguration;
import com.nd.gaea.waf.security.gaea.GaeaContext;
import com.nd.gaea.waf.security.gaea.GaeaToken;
import com.nd.gaea.waf.security.gaea.GaeaUserAuthentication;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.when;

/**
 * Created by Administrator on 2016/11/21 0021.
 */

public class PointExperienceControllerTest {

    @InjectMocks
    private PointExperienceServiceImpl pointExperienceService;
    @Mock
    private UcUserRepository ucUserRepository;
    @Mock
    private UcApi ucApi;
    @Mock
    private PointExperienceRepository pointExperienceRepository;
    @Mock
    private ErpCustomCampaignApi erpCustomCampaignApi;

    private String accessToken = "E379FA2E010AD6629FFBC3147A5F6D6360B1EF1AD3159A99DD6ECC668FBD93CD0C84402523571974";
    private String userId = "2107168459";
    private Long projectId = 1326l;

    @Before
    public void doBefore() {
        MockitoAnnotations.initMocks(this);

        UcUser operateUser=new UcUser();
        operateUser.setUserId(322198L);
        UcOrgExInfo ucOrgExInfo=new UcOrgExInfo();
        ucOrgExInfo.setRealName("a");
        ucOrgExInfo.setOrgName("a");
        operateUser.setOrgExinfo(ucOrgExInfo);
        when(this.ucUserRepository.getUser(anyLong())).thenReturn(operateUser);
        when(this.ucApi.thirdQueryUid(anyString())).thenReturn(new String("123"));
        UserInfoVo user=new UserInfoVo();
        user.setUserId(1L);
        user.setOrgId(1L);
        List<UserInfoVo> list=new ArrayList<>();
        list.add(user);
        when(this.ucUserRepository.list(anyList())).thenReturn(list);
        ErpResponse erpPoint=new ErpResponse();
        erpPoint.setResult(1);
        when(this.erpCustomCampaignApi.addPointAndExp(any(ErpVirtualType.class), anyInt(), anyLong(), anyLong())).thenReturn(erpPoint);



        PagerResult<PointExperience> pagerResult=new PagerResult<>();
        PointExperience pointExperience = new PointExperience();
        List<PointExperience> list1 = new ArrayList<>();
        list1.add(pointExperience);
        pagerResult.setItems(list1);
        when(pointExperienceRepository.listPages(anyString(), anyString(),any(Date.class), any(Date.class), anyInt(), anyInt())).thenReturn(pagerResult);
    }

    //@Test
    public void testReward() throws ParseException {

        PointExperienceVo pointExperienceVo=new PointExperienceVo();
        pointExperienceVo.setPoints(1);
        pointExperienceVo.setExperiences(1);
        pointExperienceVo.setAuth(MD5Util.encryptMD5_ND(GaeaContext.getUserId().toString())+","+System.currentTimeMillis());
        pointExperienceVo.setRewardObject("322@nd");
        this.pointExperienceService.reward(pointExperienceVo);
    }

    @Test
    public void testGetPage() throws ParseException {

        this.pointExperienceService.getPage("a","a","2016-11-10 20:56:48","2016-11-10 21:56:48",0,1);
    }
}
