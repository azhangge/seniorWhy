package com.nd.auxo.recommend.web.rabbitmq.consumer;

import com.nd.auxo.recommend.core.api.erp.ErpCustomCampaignApi;
import com.nd.auxo.recommend.core.api.erp.constant.ErpVirtualType;
import com.nd.auxo.recommend.core.api.erp.repository.ErpFinishResponse;
import com.nd.auxo.recommend.core.api.erp.repository.ErpResponse;
import com.nd.auxo.recommend.core.api.pbl.PblApi;
import com.nd.auxo.recommend.core.api.pbl.repository.EventParam;
import com.nd.auxo.recommend.core.api.pbl.repository.TriggerEvent;
import com.nd.auxo.recommend.core.constant.ActivityFinishType;
import com.nd.auxo.recommend.core.constant.TaskType;
import com.nd.auxo.recommend.core.repository.Activity;
import com.nd.auxo.recommend.core.repository.ExamActivityLog;
import com.nd.auxo.recommend.core.service.ActivityService;
import com.nd.auxo.recommend.core.service.ExamActivityLogService;
import com.nd.auxo.recommend.core.util.DateUtils;
import com.nd.auxo.recommend.core.util.RedisTemplateUtil;
import com.nd.auxo.recommend.web.config.RabbitMQConfig;
import com.nd.auxo.recommend.web.rabbitmq.constant.ExamOperateType;
import com.nd.auxo.recommend.web.rabbitmq.constant.ExamResourceType;
import com.nd.auxo.recommend.web.rabbitmq.converter.ExamMessageConverter;
import com.nd.auxo.recommend.web.rabbitmq.message.ExamMessage;
import com.nd.auxo.sdk.recommend.util.ActivityUtil;
import com.nd.auxo.sdk.recommend.util.CustomDataUtil;
import com.nd.auxo.sdk.recommend.vo.activity.CustomData;
import com.nd.gaea.core.exception.BaseRuntimeException;
import com.nd.gaea.core.utils.ObjectUtils;
import com.nd.gaea.core.utils.StringUtils;
import com.nd.gaea.util.WafJsonMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * <p></p>
 *
 * @author zhangchh
 * @version latest
 * @date 2016/7/18
 */
@Service
public class UserExamConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserExamConsumer.class);

    @Resource(name = "redisTemplate")
    private RedisTemplateUtil<Activity> redisTemplateUtil;
    @Autowired
    private ActivityService activityService;
    @Autowired
    private ErpCustomCampaignApi erpCustomCampaignApi;
    @Autowired
    private PblApi pblApi;
    @Autowired
    private ExamActivityLogService examActivityLogService;

    @Value("${erp.app.key}")
    private String erpAppKey;

//    @RabbitListener(queues = RabbitMQConfig.Queue_Name + "_" + RabbitMQConfig.Queue_Name_ENV)
    public void onMessage(ExamMessage examMessage) throws UnsupportedEncodingException {

        try {
            String data = examMessage.getCustomData();
            if (StringUtils.isEmpty(data)) {
                return;
            }
            //customData
            data = URLEncoder.encode(data, "UTF-8");
            //记录日志
            StringBuilder notSuccessMessageLog = new StringBuilder();
            if (ExamResourceType.USER_EXAM.getValue() == examMessage.getResourceType().getValue()) {
                this.handleUserExam(data, examMessage, notSuccessMessageLog);
            }
            if (notSuccessMessageLog.length() > 0) {
                notSuccessMessageLog.append(",message=【" + ObjectUtils.toJson(examMessage) + "】");
                LOGGER.error("handle message ," + notSuccessMessageLog.toString());
            }
        } catch (Exception e) {
            LOGGER.error("handle message error,message【" + ObjectUtils.toJson(examMessage) + "】", e);
        }

    }


    /**
     * 处理用户考试
     *
     * @param data
     * @param examMessage
     * @param notSuccessMessageLog
     */
    private void handleUserExam(String data, ExamMessage examMessage, StringBuilder notSuccessMessageLog) {
        Integer userExamActivityType;
        if (ExamOperateType.START.getValue() == examMessage.getOperateType().getValue()) {
            userExamActivityType = ActivityFinishType.JOIN.getValue();
        } else if (ExamOperateType.FINISH.getValue() == examMessage.getOperateType().getValue()) {
            userExamActivityType = ActivityFinishType.PASS.getValue();
        } else {
            notSuccessMessageLog.append(" ,ExamOperateType not support,end");
            return;
        }
        Long userId = examMessage.getUserId();
        if (userId == null) {
            notSuccessMessageLog.append(" ,userId is null ,end");
            return;
        }
        CustomData customData = CustomDataUtil.parseCustomData(data, CustomData.class);
        if (customData == null || customData.getActivityId() == null || customData.getUserId() == null) {
            notSuccessMessageLog.append(" ,customData is null ,end");
            return;
        }
        if (!userId.equals(customData.getUserId())) {
            // 用户id不一致
            notSuccessMessageLog.append(" ,userId is " + userId + ",customUserId is +" + customData.getUserId() + " ,end");
            return;
        }
        UUID activityId = customData.getActivityId();
        Activity activity = activityService.get(activityId);
        if (!ActivityUtil.checkActivityEnable(activityService.convertActivityVo(activity))) {
            //活动无效
            notSuccessMessageLog.append(" ,activity not enable " + ObjectUtils.toJson(activity) + " ,end");
            return;
        }
        Integer finishType = activity.getActivityFinishType();
        if (!userExamActivityType.equals(finishType)) {
            // 完成标准不匹配
            notSuccessMessageLog.append(" ,finishType not match ,end");
            return;
        }

        //用户id，活动id 是否有存在 redis
        String redisKey = "activity:user:" + activityId.toString() + ":" + userId;
        Activity redisValue = redisTemplateUtil.get(redisKey, Activity.class);
        if (redisValue != null) {
            notSuccessMessageLog.append(" ,redis has value ,end");
            return;
        }

        // 日志内容
        StringBuilder logContent = new StringBuilder();
        // 发送状态：0失败，1成功
        ExamActivityLog.Status status = ExamActivityLog.Status.SUCCESS;

        //通过  积分、经验
        if (TaskType.PBL.getValue().equals(activity.getTaskType())) {
            logContent.append("调用PBL接口");
            try {
                //pbl
                String uniqueId = UUID.randomUUID().toString();

                TriggerEvent triggerEvent = new TriggerEvent();
                triggerEvent.setEventSource("" + activity.getJoinObjectOrgId());
                triggerEvent.setUid("" + userId);
                triggerEvent.setUniqueNo(uniqueId);
                EventParam eventParam = new EventParam();
                eventParam.setDesc("一日三题");
                triggerEvent.setParams(eventParam);
                pblApi.doOneDay3Questions("" + activity.getJoinObjectOrgId(), activity.getJoinObjectOrgType(), triggerEvent);
                // 记录日志
                logContent.append("，上报事件成功,uniqueId=" + uniqueId);
            } catch (BaseRuntimeException e) {
                LOGGER.error("Send PBL error!", e);
                logContent.append("失败:" + e.getMessage());
                status = ExamActivityLog.Status.FAILURE;
            }
        } else if (TaskType.ERP.getValue().equals(activity.getTaskType())) {
            logContent.append("调用ERP接口");
            try {
                //完成任务
                status = this.finishCampaign(activity, logContent, userId);
                //经验
                if (activity.getRewardExperience() != null && activity.getRewardExperience() > 0) {
                    ExamActivityLog.Status tempStatus = this.erpRetry(ErpVirtualType.EXP, activity.getRewardExperience(), userId, activity.getJoinObjectOrgId(), examMessage, activity, logContent);
                    if (tempStatus.equals(ExamActivityLog.Status.SUCCESS)) {
                    } else {
                        status = ExamActivityLog.Status.FAILURE;
                    }

                }
                //积分
                if (activity.getRewardPoints() != null && activity.getRewardPoints() > 0) {
                    ExamActivityLog.Status tempStatus = this.erpRetry(ErpVirtualType.POINT, activity.getRewardPoints(), userId, activity.getJoinObjectOrgId(), examMessage, activity, logContent);
                    if (tempStatus.equals(ExamActivityLog.Status.SUCCESS)) {
                    } else {
                        status = ExamActivityLog.Status.FAILURE;
                    }

                }
            } catch (IOException e) {
                LOGGER.error("Send ERP error!", e);
                logContent.append("失败:" + e.getMessage());
                status = ExamActivityLog.Status.FAILURE;
            }
        } else {
            return;
        }
        // 缓存，过期时间到当天最晚。
        long timeout = DateUtils.timeDiff(new Date(com.nd.gaea.core.utils.DateUtils.truncate(new Date(), Calendar.DAY_OF_MONTH).getTime() + 86399999l), new Date());
        redisTemplateUtil.set(redisKey, activity, timeout, TimeUnit.MILLISECONDS);
        // 保存日志，记录送积分、送经验
        try {
            examActivityLogService.saveLog(userId, examMessage, activity, logContent, status);
        } catch (IOException e) {
            LOGGER.error("Save log error!", e);
        }
    }


    /**
     * 获取缓存结束时间
     *
     * @return 时间
     */
    public Date getEndTime() {
        return DateUtils.getTodayEndTime();
    }

    /**
     * 完成erp任务
     *
     * @param activity
     * @param logContent
     */
    private ExamActivityLog.Status finishCampaign(Activity activity, StringBuilder logContent, Long userId) {
        try {
            ErpFinishResponse erpFinishResponse = erpCustomCampaignApi.finishCampaign(activity.getJoinObjectOrgId(), Long.parseLong(activity.getTaskId()), userId, new Date(), erpAppKey);
            if (erpFinishResponse.getIsSuccess()) {
                logContent.append("，完成任务成功 ");
                return ExamActivityLog.Status.SUCCESS;
            } else {
                logContent.append("，完成任务失败 " + WafJsonMapper.toJson(erpFinishResponse));
                return ExamActivityLog.Status.FAILURE;
            }
        } catch (Exception e) {
            LOGGER.error("Send ERP error!", e);
            logContent.append("，完成erp任务失败:" + e.getMessage());
            return ExamActivityLog.Status.FAILURE;
        }
    }

    private ExamActivityLog.Status erpRetry(ErpVirtualType type, Integer virtualAmount, Long userId, Long orgId,
                                            ExamMessage examMessage, Activity activity, StringBuilder logContent) throws IOException {
        ErpResponse erpResponseRetry = new ErpResponse();
        int retryNum = 3;
        String rawId = "elearning-" + UUID.randomUUID();

        for (int i = 0; i < retryNum; i++) {
            StringBuilder logContext = new StringBuilder();
            logContext.append("重试ERP接口," + "rawId为：" + rawId);
            try {
                erpResponseRetry = erpCustomCampaignApi.addPointAndExpRetry(type, virtualAmount, userId, orgId, rawId);
                if (erpResponseRetry.getResult() == 1) {

                    buildLogContentByType(type, ExamActivityLog.Status.SUCCESS, activity, null, logContent, rawId);
                    return ExamActivityLog.Status.SUCCESS;
                }
                if (i < retryNum - 1) {
                    this.buildLogContextByErpType(erpResponseRetry, type, logContext);
                    this.saveLog(userId, examMessage, activity, logContext);
                    continue;
                }
                if (i == retryNum - 1) {
                    buildLogContentByType(type, ExamActivityLog.Status.FAILURE, activity, WafJsonMapper.toJson(erpResponseRetry), logContent, rawId);
                }
            } catch (BaseRuntimeException e) {
                LOGGER.debug("", e);
                if (i < retryNum - 1) {
                    this.buildLogContextByErpType(erpResponseRetry, type, logContext);
                    this.saveLog(userId, examMessage, activity, logContext);
                    continue;
                }
                if (i == retryNum - 1) {
                    buildLogContentByType(type, ExamActivityLog.Status.FAILURE, activity, e.getMessage(), logContent, rawId);
                }
            }
        }
        return ExamActivityLog.Status.FAILURE;
    }

    private void buildLogContentByType(ErpVirtualType type, ExamActivityLog.Status status, Activity activity, String errorMessage, StringBuilder logContent, String rawId) {
        if (ErpVirtualType.POINT.equals(type)) {
            if (ExamActivityLog.Status.SUCCESS.equals(status)) {
                logContent.append(",积分rawId=" + rawId + "，送积分：" + activity.getRewardPoints());
            } else {
                logContent.append(",积分rawId=" + rawId + "，送积分失败:" + errorMessage);
            }
        } else {
            if (ExamActivityLog.Status.SUCCESS.equals(status)) {
                logContent.append(",经验rawId=" + rawId + "，送经验：" + activity.getRewardExperience());

            } else {
                logContent.append(",经验rawId=" + rawId + "，送经验失败：" + errorMessage);

            }
        }
    }

    private void saveLog(Long userId, ExamMessage examMessage, Activity activity, StringBuilder logContent) {
        ExamActivityLog.Status status = ExamActivityLog.Status.FAILURE;
        try {
            examActivityLogService.saveLog(userId, examMessage, activity, logContent, status);
        } catch (IOException log) {
            LOGGER.error("Save log error!", log);
        }
    }

    private void buildLogContextByErpType(ErpResponse erpResponse, ErpVirtualType type, StringBuilder logContent) throws IOException {

        if (type.getValue() == 1) {
            logContent.append("，送积分失败" + WafJsonMapper.toJson(erpResponse));
        } else {
            logContent.append("，送经验失败" + WafJsonMapper.toJson(erpResponse));
        }
    }

}
