package com.nd.auxo.recommend.core.service.internal;

import com.nd.auxo.recommend.core.repository.Activity;
import com.nd.auxo.recommend.core.repository.ExamActivityLog;
import com.nd.auxo.recommend.core.repository.ExamActivityLogRepository;
import com.nd.auxo.recommend.core.service.ExamActivityLogService;
import com.nd.auxo.recommend.web.rabbitmq.message.ExamMessage;
import com.nd.gaea.context.repository.Repository;
import com.nd.gaea.context.service.AbstractService;
import com.nd.gaea.repository.JpaRepositoryAdapter;
import com.nd.gaea.util.WafJsonMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Date;

/**
 * 考试活动日志
 * <p/>
 * Created by jsc on 2016/7/22.
 */
@Service
public class ExamActivityLogServiceImpl extends AbstractService<ExamActivityLog, Integer> implements ExamActivityLogService {

    @Autowired
    private ExamActivityLogRepository examActivityLogRepository;

    @Override
    public Repository<ExamActivityLog, Integer> getRepository() {
        return new JpaRepositoryAdapter<>(this.examActivityLogRepository);
    }

    @Override
    public ExamActivityLog saveLog(Long userId, ExamMessage examMessage, Activity activity, StringBuilder logContent, ExamActivityLog.Status status) throws IOException {
        ExamActivityLog examActivityLog = new ExamActivityLog();
        examActivityLog.setActivityId(activity.getId().toString());
        examActivityLog.setUserId(userId);
        examActivityLog.setContent(logContent.length() > 500 ? logContent.substring(0, 500) : logContent.toString());
        examActivityLog.setCreateTime(new Date());
        examActivityLog.setMessage(WafJsonMapper.toJson(examMessage));
        examActivityLog.setStatus(status.ordinal());
        examActivityLog.setTaskType(activity.getTaskType());
        return this.examActivityLogRepository.save(examActivityLog);
    }
}
