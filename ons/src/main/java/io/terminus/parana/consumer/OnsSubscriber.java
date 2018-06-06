/**
 * io.terminus
 * Copyright(c) 2012-2018 All Rights Reserved.
 */
package io.terminus.parana.consumer;

import com.aliyun.openservices.ons.api.Action;
import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.MessageListener;
import com.aliyun.openservices.ons.api.PropertyKeyConst;
import com.aliyun.openservices.ons.api.bean.ConsumerBean;
import com.aliyun.openservices.ons.api.bean.OrderConsumerBean;
import com.aliyun.openservices.ons.api.bean.Subscription;
import com.aliyun.openservices.ons.api.order.MessageOrderListener;
import com.aliyun.openservices.ons.api.order.OrderAction;
import io.terminus.parana.client.UniformEventListener;
import io.terminus.parana.common.DefaultUniformEvent;
import io.terminus.parana.common.UniformEvent;
import io.terminus.parana.config.MQConsumerProperties;
import io.terminus.parana.rules.Subscriber;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;

/**
 * @author sean
 * @version Id:,v0.1 2018/6/6 下午10:18 sean Exp $
 * @description
 */
@Component
@Slf4j
public class OnsSubscriber implements Subscriber {

    @Autowired
    private MQConsumerProperties consumerProperties;

    private static final int     DEFAULT_CONSUME_THREAD_MAX = 64;
    private static final String  DEFAULT_TAG_EXPRESSION     = "*";

    @Autowired
    private UniformEventListener listener;

    @Override
    public void subscribe() {

        Properties properties = new Properties();
        properties.put(PropertyKeyConst.ConsumerId, consumerProperties.getConsumerId());
        properties.put(PropertyKeyConst.AccessKey, consumerProperties.getAccessKey());
        properties.put(PropertyKeyConst.SecretKey, consumerProperties.getSecretKey());
        properties.put(PropertyKeyConst.ConsumeThreadNums, getConsumeThreadMax());

        try {
            // start consumer
            if (listener.getListenerType() == UniformEventListener.ListenerTypeEnum.CONCURRENTLY) {

                ConsumerBean consumer = registerCurrentlyConsumer(properties);

                consumer.start();
            } else if (listener.getListenerType() == UniformEventListener.ListenerTypeEnum.ORDERLY) {
                OrderConsumerBean orderConsumer = registerOrderlyConsumer(properties);
                orderConsumer.start();

            } else {
                throw new IllegalArgumentException("统一消息事件监听器类型不支持，类型：{}" + listener.getListenerType());
            }

        } catch (Exception e) {
            //todo
        }
    }

    /**
     * 顺序消费消息
     * @param properties
     * @return
     */
    private OrderConsumerBean registerOrderlyConsumer(Properties properties) {

        OrderConsumerBean orderConsumer = new OrderConsumerBean();
        Map<Subscription, MessageOrderListener> subscriptionTable = new HashMap<>();
        Subscription subscription = new Subscription();
        subscription.setTopic(consumerProperties.getTopic());
        subscription.setExpression(getTagExpression(consumerProperties.getTags()));

        MessageOrderListener messageOrderListener = (message, ordercontext) -> {

            log.info("<<< 开始消费，消息topic:{},消息ID:{}", consumerProperties.getTopic(), message.getMsgID());
            try {
                UniformEvent event = createUniformEvent(message);
                listener.onUniformEvent(event);
                return OrderAction.Success;
            } catch (Exception e) {
                return OrderAction.Suspend;
            }
        };
        subscriptionTable.put(subscription, messageOrderListener);
        orderConsumer.setProperties(properties);
        orderConsumer.setSubscriptionTable(subscriptionTable);
        return orderConsumer;

    }

    /**
     * 并发消费
     * @param properties
     * @return
     */
    private ConsumerBean registerCurrentlyConsumer(Properties properties) {
        ConsumerBean consumer = new ConsumerBean();
        Map<Subscription, MessageListener> subscriptionTable = new HashMap<>();
        Subscription subscription = new Subscription();
        subscription.setTopic(consumerProperties.getTopic());
        subscription.setExpression(getTagExpression(consumerProperties.getTags()));
        MessageListener messageListener = (message, context) -> {

            log.info("<<< 开始消费，消息topic:{},消息ID:{}", consumerProperties.getTopic(), message.getMsgID());
            try {
                UniformEvent event = createUniformEvent(message);
                listener.onUniformEvent(event);
                return Action.CommitMessage;
            } catch (Exception e) {
                return Action.ReconsumeLater;
            }
        };
        subscriptionTable.put(subscription, messageListener);
        consumer.setProperties(properties);
        consumer.setSubscriptionTable(subscriptionTable);
        return consumer;
    }

    private UniformEvent createUniformEvent(Message message) {
        if (Objects.isNull(message)) {
            throw new IllegalStateException("消息消费失败，未能获取到消息实体");
        }

        // 因为在生产者端设定了每次只投递一条消息，所以这里只获取第一条消息进行处理
        String payload = new String(message.getBody());
        UniformEvent event = new DefaultUniformEvent(message.getTopic(), message.getUserProperties("__TAG"));
        event.setPayload(payload);

        return event;
    }

    private int getConsumeThreadMax() {
        if (consumerProperties.getConsumeThreadMax() == 0) {
            return DEFAULT_CONSUME_THREAD_MAX;
        }
        return consumerProperties.getConsumeThreadMax();
    }

    private String getTagExpression(String tags) {
        if (StringUtils.isEmpty(tags)) {
            return DEFAULT_TAG_EXPRESSION;
        }
        return consumerProperties.getTags();
    }
}
