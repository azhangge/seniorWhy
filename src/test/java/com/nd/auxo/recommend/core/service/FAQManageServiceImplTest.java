package com.nd.auxo.recommend.core.service;

import com.nd.auxo.recommend.core.repository.FAQManage;
import com.nd.auxo.recommend.core.repository.FAQManageRepository;
import com.nd.auxo.recommend.core.service.internal.FAQManageServiceImpl;
import com.nd.auxo.recommend.web.controller.v1.faqmanage.FAQManageVo;
import com.nd.gaea.uranus.gql.PagerResult;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.UUID;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.when;

/**
 * Created by Administrator on 2017/2/16 0016.
 */
public class FAQManageServiceImplTest {

    @InjectMocks
    private FAQManageServiceImpl faqManageService;
    @Mock
    private FAQManageRepository faqManageRepository;

    @Before
    public void doBefore() {
        MockitoAnnotations.initMocks(this);
        FAQManage faqManage = new FAQManage();
        faqManage.setSortNumber(2);
        when(this.faqManageRepository.findOne(any(UUID.class))).thenReturn(faqManage);
        when(this.faqManageRepository.save(any(FAQManage.class))).thenReturn(new FAQManage());
        when(this.faqManageRepository.listPages(anyLong(), anyString(), anyString(), any(Integer.class), anyString(), anyInt(), anyInt())).thenReturn(new PagerResult<FAQManage>());
    }

    @Test
    public void testUpdate() {
        FAQManageVo updateVo = new FAQManageVo();
        updateVo.setQuestionType(1);
        updateVo.setCustomType("a");
        updateVo.setQuestionName("a");
        updateVo.setQuestionAnswer("a");
        this.faqManageService.update(UUID.randomUUID(), updateVo);
    }

    @Test
    public void testDelete() {
        this.faqManageService.delete(UUID.randomUUID());
    }

    @Test
    public void testMove(){
        this.faqManageService.move(UUID.randomUUID(), 21);
    }
}
