package com.nd.auxo.recommend.core.service;

import com.nd.auxo.recommend.core.repository.KVInfo;
import com.nd.auxo.recommend.core.repository.KVRepository;
import com.nd.auxo.recommend.core.repository.param.KVSearchParam;
import com.nd.auxo.recommend.web.config.AuxoApplication;
import com.nd.auxo.recommend.web.config.WebMvcConfig;
import com.nd.auxo.recommend.web.rabbitmq.producer.RecommendProduce;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

/**
 * Created by Administrator on 2017/2/27 0027.
 */

public class KVServiceTest {

    @InjectMocks
    private KVService kvService;
    @Mock
    private KVRepository kvRepository;
    @Mock
    private RecommendProduce recommendProduce;

    private Long projectId = 1326l;
    private String accessToken = "E379FA2E010AD6629FFBC3147A5F6D6360B1EF1AD3159A99DD6ECC668FBD93CD0C84402523571974";
    private String userId = "2107168459";

    @Before
    public void doBefore() {
        MockitoAnnotations.initMocks(this);
        /*GaeaContext.setAppId(this.projectId);
        GaeaToken gaeaToken = new GaeaToken(this.accessToken, this.projectId);
        UserInfo userInfo = new UserInfo();
        userInfo.setUserId(this.userId);
        UserCenterUserDetails userCenterUserDetails = new UserCenterUserDetails(userInfo, null);
        WafUcCheckToken wafUcCheckToken = new WafUcCheckToken();
        SecurityContextHolder.getContext().setAuthentication(new GaeaUserAuthentication(gaeaToken, new WafUserAuthentication(userCenterUserDetails, wafUcCheckToken, WafAbstractAuthenticationToken.AUTH_TYPE_ALL)));
        */KVInfo info = new KVInfo();
        info.setIsolationStrategy(1);
        info.setIsolationParam("1");
        info.setValue("a");
        info.setGroupKey("a");
        info.setUpdateTime(new Date());
        info.setKey("a");
        info.setRemark("a");
        info.setId(UUID.randomUUID());
        when(this.kvRepository.findByKeyAndIsolationStrategyAndIsolationParam(anyString(),anyInt(),anyString())).thenReturn(null);
        when(this.kvRepository.save(any(KVInfo.class))).thenReturn(info);
        PagerResult<KVInfo> kvInfoPage = new PagerResult<>();
        List<KVInfo> kvInfos = new ArrayList<>();
        kvInfos.add(info);
        kvInfoPage.setItems(kvInfos);
        when(this.kvRepository.listPages(any(KVSearchParam.class))).thenReturn(kvInfoPage);
    }

//    @Test
//    public void testGetValue(){
//        kvService.getValue("a", KVInfo.IsolationStrategy.PROJECT_SINGLE,"a");
//    }

    @Test
    public void testDeleteId(){
        this.kvService.deleteId(UUID.randomUUID(),"a",1,"a");
    }

    @Test
    public void testCreate(){
        KVInfo info = new KVInfo();
        info.setIsolationStrategy(1);
        info.setIsolationParam("1");
        info.setValue("a");
        info.setGroupKey("a");
        info.setUpdateTime(new Date());
        info.setKey("a");
        info.setRemark("a");
        info.setId(UUID.randomUUID());
        this.kvService.create(info);
    }

    @Test
    public void testUpdate(){
        KVInfo info = new KVInfo();
        info.setIsolationStrategy(1);
        info.setIsolationParam("1");
        info.setValue("a");
        info.setGroupKey("a");
        info.setUpdateTime(new Date());
        info.setKey("a");
        info.setRemark("a");
        info.setId(UUID.randomUUID());
        this.kvService.update("a",info,1,"a");
    }
}
