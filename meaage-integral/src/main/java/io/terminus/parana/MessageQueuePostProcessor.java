/**
 * io.terminus
 * Copyright(c) 2012-2018 All Rights Reserved.
 */
package io.terminus.parana;

import io.terminus.parana.enums.ClientTypeEnum;
import io.terminus.parana.producer.OnsPublisher;
import io.terminus.parana.producer.RocketMQPublisher;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * @author sean
 * @version Id:,v0.1 2018/6/5 上午10:42 sean Exp $
 * @description
 */
@Component
public class MessageQueuePostProcessor implements ApplicationListener<ContextRefreshedEvent> {

    @Value("${mq.clientType}")
    private String clientType;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        if (StringUtils.equalsIgnoreCase(clientType, ClientTypeEnum.ons.name())) {
            OnsPublisher.getInstance().init();
        } else {
            RocketMQPublisher.getInstance().init();
        }

    }
}
