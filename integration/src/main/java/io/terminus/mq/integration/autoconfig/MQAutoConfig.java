/**
 * io.terminus
 * Copyright(c) 2012-2018 All Rights Reserved.
 */
package io.terminus.mq.integration.autoconfig;

import io.terminus.mq.config.MQConsumerProperties;
import io.terminus.mq.config.MQProducerProperties;
import io.terminus.mq.config.MQProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author sean
 * @version Id:,v0.1 2018/6/12 上午10:15 sean Exp $
 * @description
 */
@ComponentScan
@Configuration
@EnableConfigurationProperties({ MQProperties.class, MQProducerProperties.class, MQConsumerProperties.class })
public class MQAutoConfig {

}
