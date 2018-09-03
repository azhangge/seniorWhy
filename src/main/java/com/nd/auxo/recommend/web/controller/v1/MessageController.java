package com.nd.auxo.recommend.web.controller.v1;

import com.nd.auxo.recommend.core.constant.SwitchKeyConsts;
import com.nd.auxo.recommend.core.i18n.I18nExceptionMsgConstant;
import com.nd.auxo.recommend.core.service.MessageService;
import com.nd.auxo.sdk.recommend.RecommendMessageApi;
import com.nd.auxo.sdk.recommend.vo.message.RecommendMessageVo;
import com.nd.auxo.sdk.recommend.vo.message.RecommendMessageVoForCreate;
import com.nd.auxo.sdk.recommend.vo.message.RecommendMessageVoForUpdate;
import com.nd.gaea.SR;
import com.nd.gaea.uranus.common.exception.ObjectNotFoundException;
import com.nd.gaea.uranus.common.exception.UnauthorizedException;
import com.nd.gaea.waf.security.gaea.GaeaContext;
import com.nd.gaea.waf.servicemanager.ServiceManagerDo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * @author way
 *         Created on 2016/6/12.
 */
@Api("消息")
@RestController
@RequestMapping("")
public class MessageController implements RecommendMessageApi {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageController.class);


    @Autowired
    private MessageService messageService;

    @ApiOperation("创建推送消息")
    @RequestMapping(value = "/v1/recommends/messages", method = RequestMethod.POST)
    public RecommendMessageVo createMessage(@ApiParam("消息信息") @RequestBody RecommendMessageVoForCreate create) throws Exception {
        if (!ServiceManagerDo.isServiceEnable(SwitchKeyConsts.RECOMMEND_MESSAGE)) {
            throw new ObjectNotFoundException("Not Found");
        }
        if (GaeaContext.getUserId() == null) {
            throw new UnauthorizedException(SR.getString(I18nExceptionMsgConstant.COMMON_NEED_AUTH));
        }
        RecommendMessageVo vo = this.messageService.createMessage(create, GaeaContext.getAppId(), GaeaContext.getUserId());
        LOGGER.info("新建消息\"" + vo.getTitle() + "(" + vo.getId() + ")\"");
        return vo;

    }

    @ApiOperation("编辑推送消息")
    @RequestMapping(value = "/v1/recommends/messages/{message_id}", method = RequestMethod.PUT)
    public RecommendMessageVo updateMessage(@ApiParam("推送消息标识") @PathVariable("message_id") UUID messageId
            , @ApiParam("推送消息") @RequestBody RecommendMessageVoForUpdate update) throws Exception {
        if (!ServiceManagerDo.isServiceEnable(SwitchKeyConsts.RECOMMEND_MESSAGE)) {
            throw new ObjectNotFoundException("Not Found");
        }
        if (GaeaContext.getUserId() == null) {
            throw new UnauthorizedException(SR.getString(I18nExceptionMsgConstant.COMMON_NEED_AUTH));
        }
        RecommendMessageVo vo = this.messageService.updateMessage(messageId, update, GaeaContext.getUserId());
        LOGGER.info("编辑消息\"" + vo.getTitle() + "(" + vo.getId() + ")\"");
        return vo;
    }

    @ApiOperation("删除推送消息")
    @RequestMapping(value = "/v1/recommends/messages/{message_id}", method = RequestMethod.DELETE)
    public void deleteMessage(@ApiParam("推送消息标识") @PathVariable("message_id") UUID messageId) throws Exception {
        if (!ServiceManagerDo.isServiceEnable(SwitchKeyConsts.RECOMMEND_MESSAGE)) {
            throw new ObjectNotFoundException("Not Found");
        }
        this.messageService.deleteMessage(messageId);
        LOGGER.info("删除消息\"(" + messageId + ")\"");
    }

    @ApiOperation("根据Id获取推送消息")
    @RequestMapping(value = "/v1/recommends/messages/{message_id}", method = RequestMethod.GET)
    public RecommendMessageVo getMessage(@ApiParam("推送消息标识") @PathVariable("message_id") UUID messageId) throws Exception {
        if (!ServiceManagerDo.isServiceEnable(SwitchKeyConsts.RECOMMEND_MESSAGE)) {
            throw new ObjectNotFoundException("Not Found");
        }
        return this.messageService.getRecommendMessageVo(messageId);
    }
}
