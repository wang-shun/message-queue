/**
 * io.terminus
 * Copyright(c) 2012-2018 All Rights Reserved.
 */
package io.terminus.parana.producer;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;

import com.google.common.base.Throwables;
import org.springframework.stereotype.Component;

/**
 * @author sean
 * @version Id:,v0.1 2018/6/6 下午9:40 sean Exp $
 * @description
 */
@Component
@Slf4j
public class DefaultRocketMQProducer implements RocketMQProducer {

    @Override
    public boolean send(String topic, String tag, String payload) {
        try {
            DefaultMQProducer producer = RocketMQPublisher.getInstance().getProducer();
            SendResult sendResult = producer.send(new Message(topic, tag, payload.getBytes()));
            log.info("message send success,topic:{},msgId:{}", topic, sendResult.getMsgId());
            return true;
        } catch (Exception e) {
            log.error("message send failure,topic:{},cause:{}", topic, Throwables.getStackTraceAsString(e));
            return false;
        }
    }

    @Override
    public boolean sendOneway(String topic, String tag, String payload) {

        try {
            DefaultMQProducer producer = RocketMQPublisher.getInstance().getProducer();
            producer.sendOneway(new Message(topic, tag, payload.getBytes()));
            log.info("message sendOneway success,topic:{}", topic);
            return true;
        } catch (Exception e) {
            log.error("message sendOneway failure,topic:{},cause:{}", topic, Throwables.getStackTraceAsString(e));
            return false;
        }
    }
}
