package com.nd.auxo.recommend.web.rabbitmq.converter;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.nd.gaea.mq.core.bean.MqMessage;
import com.nd.gaea.mq.core.bean.MqUserMessage;
import com.nd.gaea.util.WafJsonMapper;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.support.converter.MessageConversionException;
import org.springframework.amqp.support.converter.SimpleMessageConverter;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * Created by Administrator on 2017/6/2 0002.
 */
public class KvMessageConverter extends SimpleMessageConverter {
    /**
     * createMessage.
     *
     * @param object            Object
     * @param messageProperties MessageProperties
     * @return Message
     * @throws MessageConversionException
     */
    @Override
    protected Message createMessage(Object object, MessageProperties messageProperties) throws MessageConversionException {
        if (object instanceof MqMessage) {
            try {
                return super.createMessage(WafJsonMapper.toJson(object), messageProperties);
            } catch (IOException e) {
                throw new MessageConversionException(
                        "failed to convert text-based Message content", e);
            }
        }
        return super.createMessage(object, messageProperties);
    }

    /**
     * fromMessage.
     *
     * @param message Message
     * @return Object
     * @throws MessageConversionException
     */
    @Override
    public Object fromMessage(Message message) throws MessageConversionException {
        try {
            String content = new String(message.getBody(), DEFAULT_CHARSET);
            //都按带用户的返回，区别只是有无userId
            return WafJsonMapper.parse(content, MqUserMessage.class);
        } catch (UnsupportedEncodingException e) {
            throw new MessageConversionException(
                    "failed to convert text-based Message content", e);
        } catch (JsonMappingException e) {
            throw new MessageConversionException(
                    "failed to convert text-based Message content", e);
        } catch (JsonParseException e) {
            throw new MessageConversionException(
                    "failed to convert text-based Message content", e);
        } catch (IOException e) {
            throw new MessageConversionException(
                    "failed to convert text-based Message content", e);
        }
    }
}
