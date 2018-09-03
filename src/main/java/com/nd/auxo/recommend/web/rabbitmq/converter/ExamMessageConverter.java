package com.nd.auxo.recommend.web.rabbitmq.converter;

import com.nd.auxo.recommend.web.rabbitmq.message.ExamMessage;
import com.nd.gaea.util.WafJsonMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.support.converter.MessageConversionException;
import org.springframework.amqp.support.converter.SimpleMessageConverter;

import java.io.IOException;

/**
 * <p></p>
 *
 * @author  zhangchh
 * @date    2016/7/18
 * @version latest
 */
public class ExamMessageConverter extends SimpleMessageConverter {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExamMessageConverter.class);

    @Override
    protected Message createMessage(Object object, MessageProperties messageProperties) throws MessageConversionException {
        if (object instanceof ExamMessage) {
            try {
                return super.createMessage(WafJsonMapper.toJson(object), messageProperties);
            } catch (IOException e) {
                throw new MessageConversionException(
                        "failed to convert text-based Message content", e);
            }
        }
        return super.createMessage(object, messageProperties);
    }

    @Override
    public Object fromMessage(Message message) throws MessageConversionException {
        try {
            //
            LOGGER.error(" message record ," + message.toString());
            String content = new String(message.getBody(), DEFAULT_CHARSET);
            return WafJsonMapper.parse(content, ExamMessage.class);
        } catch (Exception e) {
            throw new MessageConversionException(
                    "failed to convert text-based Message content", e);
        }
    }
}
