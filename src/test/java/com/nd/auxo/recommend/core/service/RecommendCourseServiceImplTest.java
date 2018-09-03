package com.nd.auxo.recommend.core.service;

import com.nd.auxo.recommend.core.repository.RecommendCourse;
import com.nd.auxo.recommend.core.repository.RecommendCourseRepository;
import com.nd.auxo.recommend.core.repository.course.RecommendCourseDetail;
import com.nd.auxo.recommend.core.repository.course.RecommendCourseDetailRepository;
import com.nd.auxo.recommend.core.service.internal.RecommendCourseServiceImpl;
import com.nd.auxo.recommend.web.config.AuxoApplication;
import com.nd.auxo.recommend.web.config.WebMvcConfig;
import com.nd.auxo.sdk.recommend.vo.course.RecommendCourseVoForCreate;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

/**
 * Created by Administrator on 2017/2/27 0027.
 */

public class RecommendCourseServiceImplTest {

    @InjectMocks
    private RecommendCourseServiceImpl recommendCourseService;
    @Mock
    private RecommendCourseRepository recommendCourseRepository;
    @Mock
    private RecommendCourseDetailRepository recommendCourseDetailRepository;

    private Long projectId = 1326l;
    private String accessToken = "E379FA2E010AD6629FFBC3147A5F6D6360B1EF1AD3159A99DD6ECC668FBD93CD0C84402523571974";
    private String userId = "2107168459";

    @Before
    public void doBefore() {
        MockitoAnnotations.initMocks(this);
        when(this.recommendCourseRepository.maxSortNumberByProjectId(anyLong())).thenReturn(2l);
        RecommendCourse recommendCourse = new RecommendCourse();
        recommendCourse.setCustomType("a");
        recommendCourse.setStatus(1);
        recommendCourse.setCustomOrderBy(1);
        recommendCourse.setCreateTime(new Date());
        recommendCourse.setProjectId(1l);
        recommendCourse.setCreateUserId(1l);
        recommendCourse.setTitle("a");
        recommendCourse.setSortNumber(1);
        recommendCourse.setCustomId("1");
        recommendCourse.setUpdateTime(new Date());
        recommendCourse.setUpdateUserId(1l);
        recommendCourse.setId(UUID.randomUUID());
        recommendCourse.setCustomType("a");
        recommendCourse.setRecommendMode(1);
        when(this.recommendCourseRepository.findOne(any(UUID.class))).thenReturn(recommendCourse);
        when(this.recommendCourseRepository.save(any(RecommendCourse.class))).thenReturn(recommendCourse);
        List<RecommendCourse> list = new ArrayList<>();
        list.add(recommendCourse);
        Page<RecommendCourse> voPage = new PageImpl<RecommendCourse>(list);
        when(this.recommendCourseRepository.findByProjectId(anyLong(),any(Pageable.class))).thenReturn(voPage);
        when(this.recommendCourseDetailRepository.findByRecommendIdIn(anyList())).thenReturn(new ArrayList<RecommendCourseDetail>());
        doNothing().when(this.recommendCourseRepository).moveSortNumberByProjectIdAndBannerIdAndGeSortNumberAndLeSortNumber(anyInt(),anyLong(),any(UUID.class),anyInt(),anyInt());
    }

    @Test
    public void testCreate(){
        RecommendCourseVoForCreate recommendCourse = new RecommendCourseVoForCreate();
        recommendCourse.setCustomType("a");
        recommendCourse.setStatus(1);
        recommendCourse.setCustomOrderBy(1);
        recommendCourse.setTitle("a");
        recommendCourse.setCustomId("1");
        recommendCourse.setCustomType("a");
        recommendCourse.setRecommendMode(1);
        this.recommendCourseService.create(recommendCourse,1l,1l);
    }

    @Test
    public void testUpdate(){
        RecommendCourse recommendCourse = new RecommendCourse();
        recommendCourse.setCustomType("a");
        recommendCourse.setStatus(1);
        recommendCourse.setCustomOrderBy(1);
        recommendCourse.setTitle("a");
        recommendCourse.setCustomId("1");
        recommendCourse.setCustomType("a");
        recommendCourse.setRecommendMode(1);
        this.recommendCourseService.update(recommendCourse,1l,null);
    }

//    @Test
//    public void testListPages(){
//        recommendCourseService.listPages(1l,1,1,2);
//    }

    @Test
    public void testMove(){
        this.recommendCourseService.move(UUID.randomUUID(),1,2l);
    }
}
