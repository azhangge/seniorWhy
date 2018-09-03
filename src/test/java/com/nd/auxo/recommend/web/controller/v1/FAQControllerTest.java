package com.nd.auxo.recommend.web.controller.v1;

import com.nd.auxo.recommend.core.repository.FAQManage;
import com.nd.auxo.recommend.core.repository.FAQManageRepository;
import com.nd.auxo.recommend.core.service.internal.FAQManageServiceImpl;
import com.nd.auxo.recommend.web.config.AuxoApplication;
import com.nd.auxo.recommend.web.config.WebMvcConfig;
import com.nd.auxo.recommend.web.controller.v1.faqmanage.FAQManageVo;
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

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.when;

/**
 * Created by Administrator on 2016/11/28 0028.
 */
public class FAQControllerTest {

    @InjectMocks
    private FAQManageServiceImpl faqManageService;
    @Mock
    private FAQManageRepository faqManageRepository;

    private String accessToken = "E379FA2E010AD6629FFBC3147A5F6D6360B1EF1AD3159A99DD6ECC668FBD93CD0C84402523571974";
    private String userId = "2107168459";
    private Long projectId = 1326l;

    @Before
    public void doBefore() {
        MockitoAnnotations.initMocks(this);
        FAQManage faqManage=new FAQManage();
        faqManage.setId(UUID.randomUUID());
        faqManage.setQuestionType(0);
        faqManage.setQuestionName("a");
        faqManage.setCreateTime(new Date());
        faqManage.setCustomType("a");
        faqManage.setSortNumber(1);
        faqManage.setQuestionAnswer("a");
        when(this.faqManageRepository.save(any(FAQManage.class))).thenReturn(faqManage);
        when(this.faqManageRepository.findOne(any(UUID.class))).thenReturn(faqManage);
        PagerResult<FAQManage> faqManageResult=new PagerResult<>();
        List list=new ArrayList();
        list.add(faqManage);
        faqManageResult.setItems(list);
        when(this.faqManageRepository.listPages(anyLong(),anyString(), anyString(), anyInt(), anyString(), anyInt(), anyInt())).thenReturn(faqManageResult);
    }

   // @Test
    public void testCreate(){
        FAQManageVo createVo = new FAQManageVo();
        createVo.setQuestionName("a");
        createVo.setCustomType("a");
        createVo.setQuestionAnswer("a");
        createVo.setQuestionType(0);
        this.faqManageService.create(createVo);
    }

    @Test
    public void testUpdate(){
        FAQManageVo updateVo =new FAQManageVo();
        updateVo.setQuestionName("a");
        updateVo.setCustomType("a");
        updateVo.setQuestionAnswer("a");
        updateVo.setQuestionType(0);
        this.faqManageService.update(UUID.randomUUID(), updateVo);
    }

    @Test
    public void testDelete(){
        this.faqManageService.delete(UUID.randomUUID());
    }

    //@Test
    public void testSearch(){
        this.faqManageService.search("a", "a", 0, "a", 0, 1);
    }

    @Test
    public void testMove(){
        this.faqManageService.move(UUID.randomUUID(), 1);
    }
}
