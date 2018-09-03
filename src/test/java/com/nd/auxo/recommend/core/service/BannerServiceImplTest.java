package com.nd.auxo.recommend.core.service;

import com.nd.auxo.recommend.core.repository.Banner;
import com.nd.auxo.recommend.core.repository.BannerRepository;
import com.nd.auxo.recommend.core.service.internal.BannerServiceImpl;
import com.nd.auxo.recommend.web.config.AuxoApplication;
import com.nd.auxo.recommend.web.config.WebMvcConfig;
import com.nd.auxo.sdk.recommend.vo.banner.BannerVoForCreate;
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

import java.util.UUID;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

/**
 * Created by Administrator on 2017/2/27 0027.
 */
public class BannerServiceImplTest {

    @InjectMocks
    private BannerServiceImpl bannerService;
    @Mock
    private BannerRepository bannerRepository;

    private Long projectId = 1326l;
    private String accessToken = "E379FA2E010AD6629FFBC3147A5F6D6360B1EF1AD3159A99DD6ECC668FBD93CD0C84402523571974";
    private String userId = "2107168459";

    @Before
    public void doBefore() {
        MockitoAnnotations.initMocks(this);
       /* GaeaContext.setAppId(this.projectId);
        GaeaToken gaeaToken = new GaeaToken(this.accessToken, this.projectId);
        UserInfo userInfo = new UserInfo();
        userInfo.setUserId(this.userId);
        UserCenterUserDetails userCenterUserDetails = new UserCenterUserDetails(userInfo, null);
        WafUcCheckToken wafUcCheckToken = new WafUcCheckToken();
        SecurityContextHolder.getContext().setAuthentication(new GaeaUserAuthentication(gaeaToken, new WafUserAuthentication(userCenterUserDetails, wafUcCheckToken, WafAbstractAuthenticationToken.AUTH_TYPE_ALL)));*/
        when(this.bannerRepository.findByProjectIdAndCustomTypeAndCustomId(anyLong(),anyString(),anyString())).thenReturn(null);
        when(this.bannerRepository.maxSortNumberByProjectId(anyLong())).thenReturn(2l);
        Banner vo = new Banner();
        vo.setCustomId("1");
        vo.setCustomType("1");
        vo.setTitle("a");
        vo.setWebStoreObjectId(UUID.randomUUID());
        vo.setAppStoreObjectId(UUID.randomUUID());
        vo.setWebUrl("a");
        vo.setAppUrl("b");
        vo.setSortNumber(1);
        when(this.bannerRepository.findOne(any(UUID.class))).thenReturn(vo);
        doNothing().when(this.bannerRepository).moveSortNumberByProjectIdAndBannerIdAndGeSortNumberAndLeSortNumber(anyInt(),anyLong(),any(UUID.class),anyInt(),anyInt());
    }

    @Test
    public void testCreate(){
        BannerVoForCreate vo = new BannerVoForCreate();
        vo.setCustomId("1");
        vo.setCustomType("1");
        vo.setTitle("a");
        vo.setWebStoreObjectId(UUID.randomUUID());
        vo.setAppStoreObjectId(UUID.randomUUID());
        vo.setWebUrl("a");
        vo.setAppUrl("b");
        this.bannerService.create(vo,1l,2l);
    }

    @Test
    public void testUpdate(){
        Banner vo = new Banner();
        vo.setCustomId("1");
        vo.setCustomType("1");
        vo.setTitle("a");
        vo.setWebStoreObjectId(UUID.randomUUID());
        vo.setAppStoreObjectId(UUID.randomUUID());
        vo.setWebUrl("a");
        vo.setAppUrl("b");
        this.bannerService.update(vo,1l);
    }

    @Test
    public void testMove(){
        this.bannerService.move(UUID.randomUUID(),2,1l);
    }
}
