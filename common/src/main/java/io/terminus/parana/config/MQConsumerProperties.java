/**
 * io.terminus
 * Copyright(c) 2012-2018 All Rights Reserved.
 */
package io.terminus.parana.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author sean
 * @version Id:,v0.1 2018/6/6 下午4:44 sean Exp $
 * @description
 */
@Data
@ConfigurationProperties("mq.consumer")
public class MQConsumerProperties extends MQProperties {

    /** 消费者ID （ons用）*/
    private String consumerId;

    /** 消费消息的topic */
    private String topic;

    /** 消费组名 */
    private String consumerGroup;

    /** 消费消息的最小线程数 */
    private int    consumeThreadMin = 20;

    /** 消费消息的最大线程数 */
    private int    consumeThreadMax = 64;

    /** 指定消息的Tag TagA||TagB 以双竖杠区分 */
    private String tags;
}
