package com.nd.auxo.recommend.core.service;

import com.nd.auxo.recommend.core.repository.Message;
import com.nd.auxo.sdk.recommend.vo.message.RecommendMessageVo;
import com.nd.auxo.sdk.recommend.vo.message.RecommendMessageVoForCreate;
import com.nd.auxo.sdk.recommend.vo.message.RecommendMessageVoForUpdate;
import com.nd.gaea.context.service.EntityService;

import java.util.UUID;

/**
 * @author way
 *         Created on 2016/6/12.
 */
public interface MessageService extends EntityService<Message,UUID> {
    /**
     * 创建消息
     * @param create
     * @param appId
     * @param userId
     * @return
     */
    RecommendMessageVo createMessage(RecommendMessageVoForCreate create, Long appId, Long userId) throws Exception;

    /**
     *
     * @param messageId
     * @return
     */
    RecommendMessageVo getRecommendMessageVo(UUID messageId) throws Exception;

    /**
     * 更新
     * @param messageId
     * @param update
     * @param userId
     * @return
     */
    RecommendMessageVo updateMessage(UUID messageId, RecommendMessageVoForUpdate update, Long userId) throws Exception;

    void deleteMessage(UUID messageId) throws Exception;
}
