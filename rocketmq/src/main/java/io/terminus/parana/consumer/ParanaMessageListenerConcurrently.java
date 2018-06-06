/**
 * io.terminus
 * Copyright(c) 2012-2018 All Rights Reserved.
 */
package io.terminus.parana.consumer;

import io.terminus.parana.client.UniformEventListener;
import io.terminus.parana.common.UniformEvent;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;

import java.util.List;

/**
 * @author sean
 * @version Id:,v0.1 2018/6/6 下午8:22 sean Exp $
 * @description
 */
@Slf4j
public class ParanaMessageListenerConcurrently extends AbstractParanaMessageListener implements MessageListenerConcurrently {

    /** 统一消息事件监听器 */
    private UniformEventListener listener;

    /**
     * @param listener
     */
    public ParanaMessageListenerConcurrently(UniformEventListener listener) {
        super();
        this.listener = listener;
    }

    @Override
    public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
        try {
            UniformEvent event = resolveMessage(msgs);
            boolean success = listener.onUniformEvent(event);
            if (success) {
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            } else {
                return ConsumeConcurrentlyStatus.RECONSUME_LATER;
            }
        } catch (Exception e) {
            return ConsumeConcurrentlyStatus.RECONSUME_LATER;
        } finally {
            log.info("[消费者监听器] <<< Exit ...");
        }
    }

}
