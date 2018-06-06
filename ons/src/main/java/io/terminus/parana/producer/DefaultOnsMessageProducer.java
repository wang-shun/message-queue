/**
 * io.terminus
 * Copyright(c) 2012-2018 All Rights Reserved.
 */
package io.terminus.parana.producer;

import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.Producer;
import com.aliyun.openservices.ons.api.SendResult;
import com.google.common.base.Throwables;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author sean
 * @version Id:,v0.1 2018/6/4 下午9:46 sean Exp $
 * @description
 */
@Component
@Slf4j
public class DefaultOnsMessageProducer implements OnsMessageProducer {

    @Override
    public boolean send(String topic, String tag, String payload) {
        try {
            Producer producer = OnsPublisher.getInstance().getProducer();
            SendResult sendResult = producer.send(new Message(topic, tag, payload.getBytes()));
            log.info("message send success,topic:{},msgId:{}", sendResult.getTopic(), sendResult.getMessageId());
            return true;
        } catch (Exception e) {
            log.error("message send failure,topic:{},cause:{}", topic, Throwables.getStackTraceAsString(e));
            return false;
        }
    }

    @Override
    public boolean sendOneway(String topic, String tag, String payload) {
        try {
            Producer producer = OnsPublisher.getInstance().getProducer();
            producer.sendOneway(new Message(topic, tag, payload.getBytes()));
            log.info("message send success,topic:{}", topic);
            return true;
        } catch (Exception e) {
            log.error("message send failure,topic:{},cause:{}", topic, Throwables.getStackTraceAsString(e));
            return false;
        }
    }
}
