package com.nd.auxo.recommend.web.rabbitmq.converter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.support.converter.SimpleMessageConverter;

import java.io.UnsupportedEncodingException;

/**
 * Created by way on 2016/10/18.
 */
public class RecommendMessageConverter extends SimpleMessageConverter {

    private static final Logger LOGGER = LoggerFactory.getLogger(RecommendMessageConverter.class);


    @Override
    public Object fromMessage(Message message)  {
        try {
            return new String(message.getBody(), DEFAULT_CHARSET);
        } catch (UnsupportedEncodingException e) {
            LOGGER.error("",e);
            return null;
        }
    }
}
