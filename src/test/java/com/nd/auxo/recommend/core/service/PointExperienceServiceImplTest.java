package com.nd.auxo.recommend.core.service;

import com.nd.auxo.recommend.core.api.erp.ErpCustomCampaignApi;
import com.nd.auxo.recommend.core.api.uc.UcApi;
import com.nd.auxo.recommend.core.repository.PointExperience;
import com.nd.auxo.recommend.core.repository.PointExperienceRepository;
import com.nd.auxo.recommend.core.service.internal.PointExperienceServiceImpl;
import com.nd.elearning.sdk.uc.bean.UserInfoVo;
import com.nd.elearning.sdk.uc.repository.UcUserRepository;
import com.nd.gaea.uranus.gql.PagerResult;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.when;

/**
 * Created by Administrator on 2017/2/16 0016.
 */
public class PointExperienceServiceImplTest {

    @InjectMocks
    private PointExperienceServiceImpl pointExperienceService;
    @Mock
    private PointExperienceRepository pointExperienceRepository;
    @Mock
    private UcApi ucApi;
    @Mock
    private ErpCustomCampaignApi erpCustomCampaignApi;
    @Mock
    private UcUserRepository ucUserRepository;

    @Before
    public void doBefore() {
        MockitoAnnotations.initMocks(this);
        when(this.ucApi.thirdQueryUid(anyString())).thenReturn(new String("100516"));
        List<UserInfoVo> userInfoVos = new ArrayList<>();
        UserInfoVo userInfoVo = new UserInfoVo();
        userInfoVo.setUserId(100516L);
        userInfoVo.setOrgId(123L);
        userInfoVos.add(userInfoVo);
        when(ucUserRepository.list(anyList())).thenReturn(userInfoVos);
        PagerResult<PointExperience> pagerResult=new PagerResult<>();
        PointExperience pointExperience = new PointExperience();
        List<PointExperience> list1 = new ArrayList<>();
        list1.add(pointExperience);
        pagerResult.setItems(list1);
        when(pointExperienceRepository.listPages(anyString(), anyString(), any(Date.class), any(Date.class), anyInt(), anyInt())).thenReturn(pagerResult);
    }

    @Test
    public void testCovert(){
        PointExperience param = new PointExperience();
        param.setExperiences(1);
        param.setOperateUserName("a");
        param.setPoints(1);
        param.setRewardDescription("a");
        param.setRewardDescription("a");
        param.setRewardTime(new Date());
        param.setRewardUserName("a");
        param.setType(1);
        param.setStatus(1);
        this.pointExperienceService.convert(param);
    }

    @Test
    public void testGetPage() throws ParseException {
        this.pointExperienceService.getPage("a", "a", null, null, 1, 2);
    }
}
