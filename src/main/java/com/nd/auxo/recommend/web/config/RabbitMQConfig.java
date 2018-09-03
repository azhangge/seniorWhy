package com.nd.auxo.recommend.web.config;

import com.nd.auxo.recommend.web.rabbitmq.consumer.UserExamConsumer;
import com.nd.auxo.recommend.web.rabbitmq.converter.ExamMessageConverter;
import com.nd.auxo.recommend.web.rabbitmq.converter.KvMessageConverter;
import com.nd.auxo.recommend.web.rabbitmq.converter.RecommendMessageConverter;
import com.nd.auxo.recommend.web.rabbitmq.listener.UserExamChangeListener;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.aop.Advice;
import org.aopalliance.intercept.MethodInterceptor;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.rabbit.config.RetryInterceptorBuilder;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.amqp.rabbit.retry.MissingMessageIdAdvice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.policy.MapRetryContextCache;

/**
 * <p></p>
 *
 * @author zhangchh
 * @version latest
 * @date 2016/7/18
 */
@Slf4j
@Configuration
public class RabbitMQConfig {


    public static final String Queue_Name = "exam_way";
    public static final String Queue_Name_ENV = "${env}";
    public static final String RECOMMEND_EXCHANGE_NAME = "recommend";
    public static final String RECOMMEND_ROUTING_KEY = "recommend_kv.kv_config_change";

    @Autowired
    private UserExamConsumer userExamConsumer;

    @Value("${env}")
    private String env;
    @Value("${rabbitmq.addresses}")
    private String addresses;
    @Value("${rabbitmq.vhost}")
    private String virtualHost;
    @Value("${rabbitmq.username}")
    private String username;
    @Value("${rabbitmq.password}")
    private String password;


    public CachingConnectionFactory rabbitConnectionFactory() {
        CachingConnectionFactory cachingConnectionFactory;
        cachingConnectionFactory = new CachingConnectionFactory();
        cachingConnectionFactory.setAddresses(addresses);
        cachingConnectionFactory.setVirtualHost(virtualHost);
        cachingConnectionFactory.setUsername(username);
        cachingConnectionFactory.setPassword(password);
        try {
            cachingConnectionFactory.afterPropertiesSet();
        } catch (Exception e) {
            log.error("", e);
        }
        return cachingConnectionFactory;
    }

    @Bean(name="recommend")
    public RabbitTemplate standardExamRabbitTemplate() {
        RabbitTemplate standardExamRabbitTemplate = new RabbitTemplate();
        standardExamRabbitTemplate.setExchange(RECOMMEND_EXCHANGE_NAME);
        standardExamRabbitTemplate.setConnectionFactory(rabbitConnectionFactory());
        standardExamRabbitTemplate.setMessageConverter(kvMessageConverter());
        return standardExamRabbitTemplate;
    }

    @Bean(name = "userExamRabbitAdmin")
    public RabbitAdmin userExamRabbitAdmin() {
        RabbitAdmin rabbitAdmin = new RabbitAdmin(rabbitConnectionFactory());
        rabbitAdmin.setAutoStartup(false);
        return rabbitAdmin;
    }

    public MessageListenerAdapter userExamChangeListenerAdapter() {
        MessageListenerAdapter messageListenerAdapter = new MessageListenerAdapter(userExamChangeListener(), new ExamMessageConverter());
        messageListenerAdapter.setDefaultListenerMethod("onMessage");
        return messageListenerAdapter;
    }

    public UserExamChangeListener userExamChangeListener() {
        return new UserExamChangeListener(userExamConsumer);
    }


    @Bean
    public ExamMessageConverter examMessageConverter() {
        return new ExamMessageConverter();
    }

    public KvMessageConverter kvMessageConverter(){
        return new KvMessageConverter();
    }

    @Bean
    public RecommendMessageConverter recommendMessageConverter() {
        return new RecommendMessageConverter();
    }

    @Bean(name = "userExamContainer")
    public SimpleMessageListenerContainer userExamContainer() {
        SimpleMessageListenerContainer uraListenerContainer = new SimpleMessageListenerContainer(rabbitConnectionFactory());
        uraListenerContainer.setMessageConverter(new ExamMessageConverter());
        uraListenerContainer.setAutoStartup(false);
        uraListenerContainer.setAcknowledgeMode(AcknowledgeMode.AUTO);
        uraListenerContainer.setMessageListener(userExamChangeListenerAdapter());
        uraListenerContainer.setAdviceChain(new Advice[]{(new MissingMessageIdAdvice(new MapRetryContextCache())), retryAdvice()});
        return uraListenerContainer;
    }


    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(recommendMessageConverter());
        return factory;
    }

    @Bean
    public MethodInterceptor retryAdvice() {
        //使用stateless（如果使用stateful，会因为重投递的消息体的redelivered标记而在第二次重投递时被MissingMessageIdAdvice抛出异常）
        //初始1秒，最大值限制为10分钟（防止因脏数据导致阻塞过久），每次重试会按指数增长（考虑发布时重启服务器的场景）
        //采用重试10分钟还失败，就抛弃的策略
        return RetryInterceptorBuilder.stateless()
                .backOffOptions(1000L, 2D, 600000L)
                .maxAttempts(10)
                .build();
    }


}
