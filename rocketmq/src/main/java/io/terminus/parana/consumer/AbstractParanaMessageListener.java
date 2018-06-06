/**
 * io.terminus
 * Copyright(c) 2012-2018 All Rights Reserved.
 */
package io.terminus.parana.consumer;

import io.terminus.parana.common.DefaultUniformEvent;
import io.terminus.parana.common.UniformEvent;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @author sean
 * @version Id:,v0.1 2018/6/6 下午8:33 sean Exp $
 * @description
 */
public abstract class AbstractParanaMessageListener {

    /**
     * 解析RocketMQ投递过来的消息
     *
     * @param msgs
     * @return
     * @throws
     */
    protected UniformEvent resolveMessage(List<MessageExt> msgs) {
        if (CollectionUtils.isEmpty(msgs)) {
            throw new IllegalStateException("消息消费失败，未能正确获取队列中得消息，List<MessageExt>.size()=0");
        }

        // 因为在生产者端设定了每次只投递一条消息，所以这里只获取第一条消息进行处理
        MessageExt message = msgs.get(0);
        String payload = new String(message.getBody());
        UniformEvent event = new DefaultUniformEvent(message.getTopic(), message.getTags());
        event.setPayload(payload);

        return event;
    }

}
