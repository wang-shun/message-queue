/**
 * io.terminus
 * Copyright(c) 2012-2018 All Rights Reserved.
 */
package io.terminus.parana.impl;

import io.terminus.parana.client.MessagePublisher;
import io.terminus.parana.common.UniformEvent;
import io.terminus.parana.exception.MQException;
import io.terminus.parana.producer.UniformEventPublisher;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

/**
 * @author sean
 * @version Id:,v0.1 2018/6/6 下午9:54 sean Exp $
 * @description
 */
public class DefaultMessagePublisher implements MessagePublisher {

    /** 统一消息事件发布器 */
    private UniformEventPublisher uniformEventPublisher;

    @Override
    public boolean sendOneway(String topic, String eventCode, String payload) throws MQException {
        Assert.notNull(StringUtils.isNotBlank(topic), "Topic is required");
        Assert.notNull(StringUtils.isNotBlank(eventCode), "Event code is required");
        Assert.notNull(payload, "Payload cannot be null");
        UniformEvent event = uniformEventPublisher.createUniformEvent(topic, eventCode, payload);
        return uniformEventPublisher.publishUniformEventOneway(event);
    }

    @Override
    public boolean send(String topic, String eventCode, String payload) throws MQException {
        return send(topic, eventCode, payload, -1);
    }

    @Override
    public boolean send(String topic, String eventCode, String payload, long timeout) throws MQException {
        Assert.notNull(StringUtils.isNotBlank(topic), "Topic is required");
        Assert.notNull(StringUtils.isNotBlank(eventCode), "Event code is required");
        Assert.notNull(payload, "Payload cannot be null");
        UniformEvent event = uniformEventPublisher.createUniformEvent(topic, eventCode, payload);
        event.setTimeout(timeout);
        return uniformEventPublisher.publishUniformEvent(event);
    }
}
