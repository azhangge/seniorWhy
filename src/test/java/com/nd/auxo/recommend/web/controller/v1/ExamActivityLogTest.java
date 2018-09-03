package com.nd.auxo.recommend.web.controller.v1;

import com.nd.auxo.recommend.core.repository.Activity;
import com.nd.auxo.recommend.core.repository.ActivityRepository;
import com.nd.auxo.recommend.core.repository.ExamActivityLog;
import com.nd.auxo.recommend.core.repository.ExamActivityLogRepository;
import com.nd.auxo.recommend.core.service.internal.ActivityServiceImpl;
import com.nd.auxo.recommend.core.service.internal.ExamActivityLogServiceImpl;
import com.nd.auxo.recommend.core.util.RedisTemplateUtil;
import com.nd.auxo.recommend.web.rabbitmq.message.ExamMessage;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

/**
 * Created by jsc on 2016/7/22.
 */
//@RunWith(SpringJUnit4ClassRunner.class)
//@SpringApplicationConfiguration(classes = {AuxoApplication.class, WafAutoConfiguration.class, WebMvcConfig.class})
//@WebAppConfiguration
public class ExamActivityLogTest {

    @Mock
    private RedisTemplateUtil<Activity> redisTemplateUtil;
    @Mock
    private ActivityRepository activityRepository;
    @Mock
    private ExamActivityLogRepository examActivityLogRepository;

    @InjectMocks
    private ActivityServiceImpl activityService;
    @InjectMocks
    private ExamActivityLogServiceImpl examActivityLogService;


    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        when(this.activityRepository.findOne(any(UUID.class))).thenReturn(new Activity());
        when(this.examActivityLogRepository.save(any(ExamActivityLog.class))).thenReturn(new ExamActivityLog());
    }


    @Test
    public void testName() {
        Activity activity = this.activityService.get(UUID.nameUUIDFromBytes(new String("37ecee5b-9f9b-4794-8d64-7729ad373ea8").getBytes()));
        this.redisTemplateUtil.set("a", activity, 10, TimeUnit.SECONDS);
    }

    @Test
    public void testSave() throws IOException {
        UUID uuid = UUID.fromString("37ecee5b-9f9b-4794-8d64-7729ad373ea8");
        System.out.println(uuid.toString());
        Activity activity = this.activityService.get(uuid);
        this.examActivityLogService.saveLog(1L, new ExamMessage(), activity, new StringBuilder("1"), ExamActivityLog.Status.SUCCESS);
    }
}
