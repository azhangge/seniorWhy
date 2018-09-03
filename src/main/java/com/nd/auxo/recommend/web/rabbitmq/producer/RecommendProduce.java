package com.nd.auxo.recommend.web.rabbitmq.producer;

import com.nd.auxo.recommend.web.config.RabbitMQConfig;
import com.nd.auxo.recommend.web.rabbitmq.message.RecommendKVMessage;
import com.nd.gaea.mq.core.bean.MqMessage;
import com.nd.gaea.mq.core.service.MqSendService;
import com.nd.gaea.uranus.common.exception.BusinessException;
import com.nd.gaea.waf.security.gaea.GaeaContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

/**
 * Created by Administrator on 2016/12/23 0023.
 */
@Slf4j
@Service
public class RecommendProduce {

    @Resource(name = "recommend")
    private RabbitTemplate recommendRabbitTemplate;

    public void send(RecommendKVMessage message){

        //发送消息到exchange：recommend
        MqMessage mqMessage = new MqMessage();
        //事件
        mqMessage.setEventType("kv_config_change");
        //上下文
        mqMessage.putBizObject("recommend_kv",message.getKVId().toString());
        //自定义数据
        mqMessage.setProjectId(GaeaContext.getAppId());
        mqMessage.putData("kv_id",message.getKVId().toString());
        mqMessage.putData("kv_key",message.getKVKey());
        mqMessage.putData("operate_type",message.getOperateType());
        mqMessage.putData("operate_time",new Date().toString());
        mqMessage.putData("isolation_strategy",message.getIsolationStrategy().toString());
        mqMessage.putData("user_id", message.getUserId().toString());
        try {

            recommendRabbitTemplate.convertAndSend(RabbitMQConfig.RECOMMEND_ROUTING_KEY, mqMessage);
        }catch (AmqpException e) {
            if (log.isErrorEnabled()) {
                log.error("Send mq error", e);
            }
            throw new BusinessException("Send mq error", e);
        }
    }
}
