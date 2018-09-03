package com.nd.auxo.recommend.core.service;

import com.nd.auxo.recommend.core.api.erp.ErpCustomCampaignApi;
import com.nd.auxo.recommend.core.api.erp.repository.CustomCampaign;
import com.nd.auxo.recommend.core.api.oldelearning.ElearningProjectApi;
import com.nd.auxo.recommend.core.api.oldelearning.repository.org.UcOrg;
import com.nd.auxo.recommend.core.api.oldelearning.repository.project.Project;
import com.nd.auxo.recommend.core.api.uc.UcApi;
import com.nd.auxo.recommend.core.repository.Activity;
import com.nd.auxo.recommend.core.repository.ActivityRepository;
import com.nd.auxo.recommend.core.service.internal.ActivityServiceImpl;
import com.nd.auxo.recommend.web.config.AuxoApplication;
import com.nd.auxo.recommend.web.config.WebMvcConfig;
import com.nd.auxo.sdk.recommend.vo.activity.ActivityVoForCreate;
import com.nd.auxo.sdk.recommend.vo.activity.ActivityVoForUpdate;
import com.nd.gaea.rest.security.authens.*;
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
import java.util.Date;
import java.util.UUID;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

/**
 * Created by Administrator on 2017/2/16 0016.
 */
public class ActivityServiceImplTest {

    @InjectMocks
    private ActivityServiceImpl activityService;
    @Mock
    private ActivityRepository activityRepository;
    @Mock
    private ElearningProjectApi projectApi;
    @Mock
    private ErpCustomCampaignApi erpCustomCampaignApi;
    @Mock
    private UcApi ucApi;

    private Long projectId = 1326l;
    private String accessToken = "E379FA2E010AD6629FFBC3147A5F6D6360B1EF1AD3159A99DD6ECC668FBD93CD0C84402523571974";
    private String userId = "2107168459";

    @Before
    public void doBefore() {
        MockitoAnnotations.initMocks(this);
        CustomCampaign customCampaign = new CustomCampaign();
        customCampaign.setId(1l);
        when(this.erpCustomCampaignApi.create(any(CustomCampaign.class))).thenReturn(customCampaign);
        Activity activity = new Activity();
        activity.setTaskType(1);
        activity.setTaskId("1");
        activity.setStartTime(new Date());
        activity.setEndTime(new Date());
        when(this.activityRepository.findOne(any(UUID.class))).thenReturn(activity);
        when(this.erpCustomCampaignApi.update(anyString(),any(CustomCampaign.class))).thenReturn(customCampaign);
        UcOrg ucOrg = new UcOrg();
        ucOrg.setOrgId(1l);
        when(this.projectApi.getUcOrgByName(anyString())).thenReturn(ucOrg);
        Project project = new Project();
        project.setOrgId("1");
        project.setUcOrgName("a");
        project.setOrgType(1);
        project.setDomain("a");
        when(this.projectApi.get(anyLong())).thenReturn(project);
    }

    @Test
    public void testCreate(){
        ActivityVoForCreate vo = new ActivityVoForCreate();
        vo.setJoinObject("1@nd,2@nd");
        vo.setJoinObjectType(1);
        vo.setTaskType(1);
        vo.setStartTime(new Date());
        vo.setEndTime(new Date());
        this.activityService.create(vo,1l,1l);
    }

    @Test
    public void testUpdate(){
        ActivityVoForUpdate vo = new ActivityVoForUpdate();
        vo.setJoinObject("1@nd,2@nd");
        vo.setJoinObjectType(1);
        vo.setStartTime(new Date());
        vo.setEndTime(new Date());
        this.activityService.update(UUID.randomUUID(),vo,1l,1l);
    }

    @Test
    public void testDelete() throws Exception {
        this.activityService.delete(UUID.randomUUID());
    }

    @Test
    public void testConvertActivity(){
        ActivityVoForCreate create = new ActivityVoForCreate();
        create.setCustomType("a");
        create.isEnabled();
        create.setDescription("a");
        create.setEndTime(new Date());
        create.setStartTime(new Date());
        create.setRewardExperience(1);
        create.setTitle("a");
        create.setRewardPoints(1);
        create.setJoinObjectType(1);
        create.setCustomId(UUID.randomUUID().toString());
        create.setTargetCmpUrl("a");
        create.setJoinObject("a");
        create.setActivityType(1);
        create.setActivityFinishType(1);
        this.activityService.convertActivity(create);
    }


}
