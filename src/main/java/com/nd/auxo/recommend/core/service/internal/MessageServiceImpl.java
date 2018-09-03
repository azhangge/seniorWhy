package com.nd.auxo.recommend.core.service.internal;

import com.nd.auxo.recommend.core.i18n.I18nExceptionMsgConstant;
import com.nd.auxo.recommend.core.repository.Message;
import com.nd.auxo.recommend.core.repository.MessageRepository;
import com.nd.auxo.recommend.core.service.MessageService;
import com.nd.auxo.sdk.interaction.message.api.MessageSendApi;
import com.nd.auxo.sdk.interaction.message.vo.MessageVo;
import com.nd.auxo.sdk.recommend.common.CustomType;
import com.nd.auxo.sdk.recommend.vo.message.*;
import com.nd.gaea.SR;
import com.nd.gaea.context.repository.Repository;
import com.nd.gaea.context.service.AbstractService;
import com.nd.gaea.core.utils.ObjectUtils;
import com.nd.gaea.repository.JpaRepositoryAdapter;
import com.nd.gaea.uranus.common.exception.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import java.util.Date;
import java.util.UUID;

/**
 * @author way
 *         Created on 2016/5/23.
 */
@Service
@Validated
public class MessageServiceImpl extends AbstractService<Message, UUID> implements MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private MessageSendApi messageSendApi;

    @Value("${auxo.webfront.url}")
    private String webFrontUrl;
    @Value("${auxo.course.webfront}")
    private String openCourseFrontUrl;
    @Value("${auxo.train.webfront}")
    private String trainFrontUrl;
    @Value("${auxo.mystudy.webfront}")
    private String myStudyFrontUrl;


    @Override
    public Repository<Message, UUID> getRepository() {
        return new JpaRepositoryAdapter<>(this.messageRepository);
    }

    @Override
    public RecommendMessageVo createMessage(RecommendMessageVoForCreate create, Long appId, Long userId) throws Exception {
        MessageVo messageVo = this.messageSendApi.createMessage(convertByCreate(create, appId));
        Message message = new Message();
        message.setJumpParam(ObjectUtils.toJson(create.getJumpParam()));
        message.setJumpType(create.getJumpType().getValue());
        message.setCreateTime(new Date());
        message.setCreateUserId(userId);
        message.setUpdateUserId(userId);
        message.setUpdateTime(new Date());
        message.setLinkId(messageVo.getId());
        //id 与消息中心的id改为一致
        message.setId(messageVo.getId());
        this.messageRepository.save(message);
        return convertByMessage(message, messageVo);
    }

    @Override
    public RecommendMessageVo getRecommendMessageVo(UUID messageId) throws Exception {
        Message message = this.messageRepository.findOne(messageId);
        if (message == null) {
            throw new ObjectNotFoundException(SR.format(I18nExceptionMsgConstant.COMMON_NOT_FOUND, messageId));
        }
        MessageVo messageVo = this.messageSendApi.getMessage(message.getLinkId());
        if (messageVo == null) {
            throw new ObjectNotFoundException(SR.format(I18nExceptionMsgConstant.COMMON_NOT_FOUND, message.getLinkId()));
        }
        return convertByMessage(message, messageVo);
    }

    @Override
    public RecommendMessageVo updateMessage(UUID messageId, RecommendMessageVoForUpdate update, Long userId) throws Exception {
        Message message = this.messageRepository.findOne(messageId);
        if (message == null) {
            throw new ObjectNotFoundException(SR.format(I18nExceptionMsgConstant.COMMON_NOT_FOUND, messageId));
        }

        MessageVo messageVo = this.messageSendApi.updateMessage(message.getLinkId(), convertByUpdate(update));
        message.setUpdateTime(new Date());
        message.setUpdateUserId(userId);
        message.setJumpType(update.getJumpType().getValue());
        message.setJumpParam(ObjectUtils.toJson(update.getJumpParam()));
        this.messageRepository.save(message);
        return convertByMessage(message, messageVo);
    }

    @Override
    public void deleteMessage(UUID messageId) throws Exception {
        Message message = this.messageRepository.findOne(messageId);
        if (message == null) {
            throw new ObjectNotFoundException(SR.format(I18nExceptionMsgConstant.COMMON_NOT_FOUND, messageId));
        }
        this.messageSendApi.deleteMessage(message.getLinkId());
        this.messageRepository.delete(message);
    }

    private MessageVo convertByUpdate(RecommendMessageVoForUpdate update) {
        MessageVo messageVo = new MessageVo();
        messageVo.setStartTime(update.getStartTime());
        messageVo.setTitle(update.getTitle());
        messageVo.setImageUrl(update.getImageUrl());
        messageVo.setChannels(update.getChannels());
        messageVo.setReceiveObjectType(update.getReceiveObjectType());
        messageVo.setReceiveObject(update.getReceiveObject());
        messageVo.setContent(update.getContent());
        messageVo.setCustomType(update.getCustomType().getValue());

        messageVo.setPcUrl(getPcUrl(update.getJumpType(), update.getJumpParam()));
        messageVo.setMobileUrl(getMobileUrl(update.getJumpType(), update.getJumpParam()));
        return messageVo;
    }

    private RecommendMessageVo convertByMessage(Message message, MessageVo messageVo) {
        RecommendMessageVo recommendMessageVo = new RecommendMessageVo();
        recommendMessageVo.setCustomType(CustomType.getByValue(messageVo.getCustomType()));
        recommendMessageVo.setStatus(messageVo.getStatus());
        recommendMessageVo.setMobileUrl(messageVo.getMobileUrl());
        recommendMessageVo.setCreateTime(message.getCreateTime());
        recommendMessageVo.setProjectId(messageVo.getProjectId());
        recommendMessageVo.setStartTime(messageVo.getStartTime());
        recommendMessageVo.setTitle(messageVo.getTitle());
        recommendMessageVo.setPcUrl(messageVo.getPcUrl());
        recommendMessageVo.setImageUrl(messageVo.getImageUrl());

        recommendMessageVo.setChannels(messageVo.getChannels());
        recommendMessageVo.setReceiveObjectType(messageVo.getReceiveObjectType());
        recommendMessageVo.setReceiveObject(messageVo.getReceiveObject());
        recommendMessageVo.setContent(messageVo.getContent());
        recommendMessageVo.setCustomId(messageVo.getCustomId());

        recommendMessageVo.setJumpType(JumpType.getByValue(message.getJumpType()));
        recommendMessageVo.setJumpParam(ObjectUtils.fromJson(message.getJumpParam(), JumpParam.class));
        recommendMessageVo.setId(message.getId());
        recommendMessageVo.setLinkId(message.getLinkId());
        return recommendMessageVo;
    }

    private MessageVo convertByCreate(RecommendMessageVoForCreate create, Long appId) {
        MessageVo messageVo = new MessageVo();
        messageVo.setProjectId(appId);
        messageVo.setStartTime(create.getStartTime());
        messageVo.setTitle(create.getTitle());
        messageVo.setImageUrl(create.getImageUrl());
        messageVo.setChannels(create.getChannels());
        messageVo.setReceiveObjectType(create.getReceiveObjectType());
        messageVo.setReceiveObject(create.getReceiveObject());
        messageVo.setContent(create.getContent());
        messageVo.setCustomType(create.getCustomType().getValue());

        messageVo.setPcUrl(getPcUrl(create.getJumpType(), create.getJumpParam()));
        messageVo.setMobileUrl(getMobileUrl(create.getJumpType(), create.getJumpParam()));
        return messageVo;
    }

    /**
     * 获取pcUrl
     *
     * @param jumpType
     * @param jumpParam
     * @return
     */
    private String getPcUrl(JumpType jumpType, JumpParam jumpParam) {
        String value = jumpType.getValue();
        if (JumpType.WEB_PAGE.getValue().equals(value)) {
            return jumpParam.getUrl();
        } else if (JumpType.AUXO_EXAM_CENTER.getValue().equals(value)) {
            return String.format("%s/%s/exam/prepare/%s"
                    , webFrontUrl, jumpParam.getProjectCode(), jumpParam.getId());
        } else if (JumpType.AUXO_OPEN_COURSE.getValue().equals(value)) {
            return String.format("%s/%s/course/%s"
                    , openCourseFrontUrl, jumpParam.getProjectCode(), jumpParam.getId());
        } else if (JumpType.AUXO_TRAIN.getValue().equals(value)) {
            return String.format("%s/%s/train/%s"
                    , trainFrontUrl, jumpParam.getProjectCode(), jumpParam.getId());
        } else if (JumpType.MY_LEARN.getValue().equals(value)) {
            return String.format("%s/%s/mystudy/user_center"
                    , myStudyFrontUrl, jumpParam.getProjectCode());
        }
        return null;
    }

    /**
     * 获取移动端Url
     *
     * @param jumpType
     * @param jumpParam
     * @return
     */
    private String getMobileUrl(JumpType jumpType, JumpParam jumpParam) {
        String value = jumpType.getValue();
        if (JumpType.WEB_PAGE.getValue().equals(value)) {
            return jumpParam.getUrl();
        } else if (JumpType.AUXO_EXAM_CENTER.getValue().equals(value)) {
            return String.format("cmp://com.nd.hy.elearning/eexam?egoPageName=ELExamPrepareViewController&examId=%s"
                    , jumpParam.getId());
        } else if (JumpType.AUXO_OPEN_COURSE.getValue().equals(value)) {
            return String.format("cmp://com.nd.hy.elearning/ecourse?egoPageName=HYCLCourseMainViewController&courseId=%s",
                    jumpParam.getId());
        } else if (JumpType.AUXO_TRAIN.getValue().equals(value)) {
            return String.format("cmp://com.nd.hy.elearning/etraincert?egoPageName=ELTrainMainViewController&trainId=%s",
                    jumpParam.getId());
        } else if (JumpType.MY_LEARN.getValue().equals(value)) {
            return "cmp://com.nd.sdp.component.ele-my-study/main";
            //return "cmp://com.nd.hy.elearning/location?channel=emylearn";
        }
        return null;
    }
}
