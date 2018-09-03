package com.nd.auxo.recommend.core.service;

import com.nd.auxo.recommend.core.repository.Activity;
import com.nd.auxo.recommend.core.repository.ExamActivityLog;
import com.nd.auxo.recommend.web.rabbitmq.message.ExamMessage;
import com.nd.gaea.context.service.EntityService;

import java.io.IOException;

/**
 * 考试活动日志
 *
 * Created by jsc on 2016/7/22.
 */
public interface ExamActivityLogService extends EntityService<ExamActivityLog, Integer> {

    /**
     * 保存日志
     *
     * @param userId
     * @param examMessage
     * @param activity
     * @param logContent
     * @param status
     */
    public ExamActivityLog saveLog(Long userId, ExamMessage examMessage, Activity activity, StringBuilder logContent, ExamActivityLog.Status status) throws IOException;
}
