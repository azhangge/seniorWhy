package com.nd.auxo.recommend.web.rabbitmq.producer;

import com.nd.auxo.recommend.web.rabbitmq.constant.ExamOperateType;
import com.nd.auxo.recommend.web.rabbitmq.constant.ExamResourceType;
import com.nd.auxo.recommend.web.rabbitmq.message.ExamMessage;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * <p></p>
 *
 * @author  zhangchh
 * @date    2016/7/18
 * @version latest
 */
@Service
public class UserExamProducer {

    @Autowired
    private RabbitTemplate rabbitTemplate;



    public void sendStart() {
        Map<String, Object> data = new HashMap<>();

        ExamMessage message = new ExamMessage();
        message.setOperateType(ExamOperateType.START);
        message.setResourceType(ExamResourceType.USER_EXAM);
        message.setAppId(1l);
        message.setCustomId("1");
        message.setCustomType("1");
        message.setExamId(UUID.randomUUID());
        message.setUserId(1l);
        message.setCustomData("1");
        message.setData((HashMap<String, Object>) data);
        rabbitTemplate.convertAndSend("userExam.start", message);

    }

}
