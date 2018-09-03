package com.nd.auxo.recommend.core.service;

import com.nd.auxo.recommend.core.repository.Activity;
import com.nd.auxo.recommend.core.repository.ExamActivityLog;
import com.nd.auxo.recommend.core.repository.ExamActivityLogRepository;
import com.nd.auxo.recommend.core.service.internal.ExamActivityLogServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.io.IOException;
import java.util.UUID;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

/**
 * Created by Administrator on 2017/2/16 0016.
 */
public class ExamActivityLogServiceImplTest {

    @InjectMocks
    private ExamActivityLogServiceImpl examActivityLogService;
    @Mock
    private ExamActivityLogRepository examActivityLogRepository;

    @Before
    public void doBefore() {
        MockitoAnnotations.initMocks(this);
        when(this.examActivityLogRepository.save(any(ExamActivityLog.class))).thenReturn(new ExamActivityLog());
    }

    @Test
    public void test() throws IOException {
        Activity activity = new Activity();
        activity.setId(UUID.randomUUID());
        StringBuilder logContent = new StringBuilder("abc");
        this.examActivityLogService.saveLog(null,null,activity,logContent, ExamActivityLog.Status.SUCCESS);
    }
}
