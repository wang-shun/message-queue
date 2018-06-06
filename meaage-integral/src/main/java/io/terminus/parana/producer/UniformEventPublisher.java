/**
 * io.terminus
 * Copyright(c) 2012-2018 All Rights Reserved.
 */
package io.terminus.parana.producer;

import io.terminus.parana.common.UniformEvent;

/**
 * @author sean
 * @version Id:,v0.1 2018/6/6 上午11:45 sean Exp $
 * @description
 */
public interface UniformEventPublisher {

    /**
     * 发布统一消息事件
     *
     * @param event
     * @return
     */
    boolean publishUniformEvent(UniformEvent event);

    /**
     * 发布统一消息事件(oneway方式)
     *
     * @param event
     * @return
     */
    boolean publishUniformEventOneway(UniformEvent event);

    /**
     * 
     * @param topic
     * @param eventCode
     * @param payload
     * @return
     */
    UniformEvent createUniformEvent(String topic, String eventCode, String payload);
}
