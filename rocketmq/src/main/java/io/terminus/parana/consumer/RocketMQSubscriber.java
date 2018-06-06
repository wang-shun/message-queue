/**
 * io.terminus
 * Copyright(c) 2012-2018 All Rights Reserved.
 */
package io.terminus.parana.consumer;

import io.terminus.parana.client.UniformEventListener;
import io.terminus.parana.config.MQConsumerProperties;
import io.terminus.parana.rules.Subscriber;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author sean
 * @version Id:,v0.1 2018/6/6 下午8:04 sean Exp $
 * @description
 */
@Slf4j
@Component
public class RocketMQSubscriber implements Subscriber {

    private static final int      DEFAULT_CONSUME_THREAD_MAX       = 64;
    private static final int      DEFAULT_CONSUME_THREAD_MIN       = 20;
    private static final int      DEFAULT_PULL_BATCH_SIZE          = 32;
    private static final int      DEFAULT_PULL_INTERVAL            = 0;
    private static final int      DEFAULT_PULL_THRESHOLD_FOR_QUEUE = 1000;
    private static final String   DEFAULT_TAG_EXPRESSION           = "*";

    @Autowired
    private MQConsumerProperties  mqConsumerProperties;

    private DefaultMQPushConsumer consumer;

    @Autowired
    private UniformEventListener  uniformEventListener;

    @Override
    public void subscribe() {
        consumer = new DefaultMQPushConsumer(mqConsumerProperties.getConsumerGroup());
        consumer.setNamesrvAddr(mqConsumerProperties.getNameServer());
        consumer.setConsumeFromWhere(getConsumeFromWhere());
        consumer.setMessageModel(getMessageModel());

        consumer.setConsumeThreadMax(getConsumeThreadMax());
        consumer.setConsumeThreadMin(getConsumeThreadMin());
        consumer.setPullBatchSize(DEFAULT_PULL_BATCH_SIZE);
        consumer.setPullInterval(DEFAULT_PULL_INTERVAL);
        consumer.setPullThresholdForQueue(DEFAULT_PULL_THRESHOLD_FOR_QUEUE);

        if (uniformEventListener.getListenerType() == UniformEventListener.ListenerTypeEnum.CONCURRENTLY) {
            consumer.registerMessageListener(new ParanaMessageListenerConcurrently(uniformEventListener));
        } else if (uniformEventListener.getListenerType() == UniformEventListener.ListenerTypeEnum.ORDERLY) {
            consumer.registerMessageListener(new ParanaMessageListenerOrderly(uniformEventListener));
        } else {
            throw new IllegalArgumentException("统一消息事件监听器类型不支持，类型：" + uniformEventListener.getListenerType());
        }

        // start consumer
        try {
            String tagExpression = getTagExpression(mqConsumerProperties.getTags());
            consumer.subscribe(mqConsumerProperties.getTopic(), tagExpression);
            consumer.start();
            log.info("消费者注册成功 nameSrvAddress={}, group={}", mqConsumerProperties.getNameServer(), mqConsumerProperties.getConsumerGroup());
        } catch (MQClientException e) {
            //todo
        }
    }

    private String getTagExpression(String tags) {
        if (StringUtils.isEmpty(tags)) {
            return DEFAULT_TAG_EXPRESSION;
        }
        return mqConsumerProperties.getTags();
    }

    private int getConsumeThreadMin() {
        if (mqConsumerProperties.getConsumeThreadMin() == 0) {
            return DEFAULT_CONSUME_THREAD_MIN;
        }
        return mqConsumerProperties.getConsumeThreadMin();
    }

    private int getConsumeThreadMax() {
        if (mqConsumerProperties.getConsumeThreadMax() == 0) {
            return DEFAULT_CONSUME_THREAD_MAX;
        }
        return mqConsumerProperties.getConsumeThreadMax();
    }

    private MessageModel getMessageModel() {
        return MessageModel.CLUSTERING;
    }

    private ConsumeFromWhere getConsumeFromWhere() {
        return ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET;
    }

}
