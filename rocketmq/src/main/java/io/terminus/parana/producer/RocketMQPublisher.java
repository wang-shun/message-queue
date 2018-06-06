/**
 * io.terminus
 * Copyright(c) 2012-2018 All Rights Reserved.
 */
package io.terminus.parana.producer;

import io.terminus.parana.rules.Publisher;
import io.terminus.parana.config.MQProducerProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author sean
 * @version Id:,v0.1 2018/6/6 下午6:13 sean Exp $
 * @description
 */
@Component
@Slf4j
public class RocketMQPublisher implements Publisher {

    @Autowired
    private MQProducerProperties mqProducerProperties;

    private DefaultMQProducer    producer;

    public RocketMQPublisher() {
    }

    private static class ProducerBeanSingleTon {
        private static final RocketMQPublisher INSTANCE = new RocketMQPublisher();
    }

    public static RocketMQPublisher getInstance() {
        return ProducerBeanSingleTon.INSTANCE;
    }

    @Override
    public void init() {
        try {
            producer = new DefaultMQProducer(mqProducerProperties.getProducerGroup());
            producer.setNamesrvAddr(mqProducerProperties.getNameServer());
            producer.setRetryAnotherBrokerWhenNotStoreOK(mqProducerProperties.isRetryAnotherBrokerWhenNotStore());
            producer.setRetryTimesWhenSendFailed(mqProducerProperties.getRetryTimesWhenSendFailed());
            producer.setSendMsgTimeout(mqProducerProperties.getTimeout());
            producer.setClientCallbackExecutorThreads(mqProducerProperties.getMaxMessageSize());
            producer.setMaxMessageSize(mqProducerProperties.getMaxMessageSize());

            producer.start();
        } catch (Exception e) {
            log.error("消息生产者启动失败，消息分组：{}，消息命名服务器地址：{}", mqProducerProperties.getProducerGroup(), mqProducerProperties.getNameServer());
        }
    }

    public DefaultMQProducer getProducer() {
        return producer;
    }
}
