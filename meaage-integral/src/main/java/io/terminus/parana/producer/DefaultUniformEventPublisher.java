/**
 * io.terminus
 * Copyright(c) 2012-2018 All Rights Reserved.
 */
package io.terminus.parana.producer;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.terminus.parana.common.DefaultUniformEvent;
import io.terminus.parana.common.UniformEvent;
import io.terminus.parana.enums.ClientTypeEnum;

/**
 * @author sean
 * @version Id:,v0.1 2018/6/6 下午12:25 sean Exp $
 * @description
 */
@Component
public class DefaultUniformEventPublisher implements UniformEventPublisher {

    @Value("${mq.clientType}")
    private String             clientType;

    @Autowired
    private OnsMessageProducer onsMessageProducer;

    @Autowired
    private RocketMQProducer   rocketMQProducer;

    @Override
    public boolean publishUniformEvent(UniformEvent event) {
        if (event == null) {
            throw new IllegalArgumentException("UniformEvent is null");
        }
        if (StringUtils.equalsIgnoreCase(clientType, ClientTypeEnum.ons.name())) {
            onsMessageProducer.send(event.getTopic(), event.getEventCode(), event.getPayload());

        } else {
            rocketMQProducer.send(event.getTopic(), event.getEventCode(), event.getPayload());
        }
        return false;
    }

    @Override
    public boolean publishUniformEventOneway(UniformEvent event) {
        return false;
    }

    @Override
    public UniformEvent createUniformEvent(String topic, String eventCode, String payload) {
        UniformEvent e = new DefaultUniformEvent(topic, eventCode);
        e.setPayload(payload);
        return e;
    }
}
