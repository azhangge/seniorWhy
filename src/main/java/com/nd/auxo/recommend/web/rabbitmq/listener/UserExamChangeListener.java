package com.nd.auxo.recommend.web.rabbitmq.listener;

import com.nd.auxo.recommend.web.rabbitmq.consumer.UserExamConsumer;
import com.nd.auxo.recommend.web.rabbitmq.message.ExamMessage;
import org.springframework.util.Assert;

import java.io.UnsupportedEncodingException;

/**
 * 消费监听
 * <p/>
 * Created by jsc on15-1-26.
 */

public class UserExamChangeListener {

    private UserExamConsumer userExamConsumer;

    public UserExamChangeListener(UserExamConsumer userExamConsumer) {
        Assert.notNull(userExamConsumer, "UserExamConsumer must not be null");
        this.userExamConsumer = userExamConsumer;
    }

    public void onMessage(ExamMessage message) {
        try {
            this.userExamConsumer.onMessage(message);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
