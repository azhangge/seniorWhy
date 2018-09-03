package com.nd.auxo.recommend.core.service;

import com.nd.auxo.recommend.core.repository.RecommendTag;
import com.nd.auxo.recommend.core.repository.RecommendTagRepository;
import com.nd.auxo.recommend.core.service.internal.RecommendTagServiceImpl;
import com.nd.auxo.recommend.web.config.AuxoApplication;
import com.nd.auxo.recommend.web.config.WebMvcConfig;
import com.nd.auxo.sdk.recommend.vo.tag.RecommendTagVoForCreate;
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
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

/**
 * Created by Administrator on 2017/2/27 0027.
 */
public class RecommendTagServiceImplTest {

    @InjectMocks
    private RecommendTagServiceImpl recommendTagService;
    @Mock
    private RecommendTagRepository recommendTagRepository;

    private Long projectId = 1326l;
    private String accessToken = "E379FA2E010AD6629FFBC3147A5F6D6360B1EF1AD3159A99DD6ECC668FBD93CD0C84402523571974";
    private String userId = "2107168459";

    @Before
    public void doBefore() {
        MockitoAnnotations.initMocks(this);
        when(this.recommendTagRepository.findByProjectIdAndCustomTypeAndCustomId(anyLong(),anyString(),anyString())).thenReturn(null);
        when(this.recommendTagRepository.maxSortNumberByProjectId(anyLong())).thenReturn(2l);
        RecommendTag recommendTag = new RecommendTag();
        recommendTag.setCustomType("q");
        recommendTag.setStatus(1);
        recommendTag.setCustomOrderBy(1);
        recommendTag.setCreateTime(new Date());
        recommendTag.setProjectId(1l);
        recommendTag.setCreateUserId(2l);
        recommendTag.setTitle("a");
        recommendTag.setSortNumber(1);
        recommendTag.setCustomId("a");
        recommendTag.setUpdateTime(new Date());
        recommendTag.setId(UUID.randomUUID());
        recommendTag.setAppStoreObjectId(UUID.randomUUID());
        recommendTag.setCustomIdType("1");
        when(this.recommendTagRepository.findOne(any(UUID.class))).thenReturn(recommendTag);
        doNothing().when(this.recommendTagRepository).moveSortNumberByProjectIdAndBannerIdAndGeSortNumberAndLeSortNumber(anyInt(),anyLong(),any(UUID.class),anyInt(),anyInt());
        when(this.recommendTagRepository.save(any(RecommendTag.class))).thenReturn(recommendTag);
    }

    @Test
    public void testCreate(){
        RecommendTagVoForCreate recommendTag = new RecommendTagVoForCreate();
        recommendTag.setCustomType("q");
        recommendTag.setStatus(1);
        recommendTag.setCustomOrderBy(1);
        recommendTag.setTitle("a");
        recommendTag.setCustomId("a");
        recommendTag.setAppStoreObjectId(UUID.randomUUID());
        recommendTag.setCustomIdType("1");
        this.recommendTagService.create(recommendTag,1l,1l);
    }

    @Test
    public void testUpdate(){
        RecommendTag recommendTag = new RecommendTag();
        recommendTag.setCustomType("q");
        recommendTag.setStatus(1);
        recommendTag.setCustomOrderBy(1);
        recommendTag.setTitle("a");
        recommendTag.setCustomId("a");
        recommendTag.setAppStoreObjectId(UUID.randomUUID());
        recommendTag.setCustomIdType("1");
        this.recommendTagService.update(recommendTag,1l);
    }

    @Test
    public void testMove(){
        this.recommendTagService.move(UUID.randomUUID(),2,1l);
    }
}
