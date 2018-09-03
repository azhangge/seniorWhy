package com.nd.auxo.recommend.web.rabbitmq.manager;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Created by Administrator on 15-6-23.
 */
@Component
public class MqManager {
    private static final Log MQLOG = LogFactory.getLog(MqManager.class);
    @Value("${env}")
    String env;
    @Autowired
    @Qualifier("userExamRabbitAdmin")
    private RabbitAdmin userExamRabbitAdmin;
    @Autowired
    @Qualifier("userExamContainer")
    private SimpleMessageListenerContainer userExamChangeListenerContainer;
    public static final String EXAM_CONFIG_EXCHANGE_NAME = "exam";
    public static final String Exam_Queue_Name = "exam_way_";

    /**
     */
    @PostConstruct
    public void startListener() {
        Thread t = new Thread() {
            @Override
            public void run() {
                try {
                    if (MQLOG.isDebugEnabled()) {
                        MQLOG.debug("MQ listener starting...");
                    }
                    //绑定项目配置组件队列
                    Queue examMessageQueue = new Queue(Exam_Queue_Name + env, true);

                    userExamRabbitAdmin.declareQueue(examMessageQueue);
                    userExamRabbitAdmin.declareBinding(new Binding(examMessageQueue.getName(), Binding.DestinationType.QUEUE, EXAM_CONFIG_EXCHANGE_NAME, "#", null));
                    // 启动监听
                    userExamChangeListenerContainer.setQueues(examMessageQueue);
                    userExamChangeListenerContainer.start();
                } catch (Exception e) {
                    MQLOG.error("MQ listener start error!", e);
                }
            }
        };
        t.start();
    }
}