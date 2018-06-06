/**
 * io.terminus
 * Copyright(c) 2012-2018 All Rights Reserved.
 */
package io.terminus.parana.client;

import io.terminus.parana.exception.MQException;

/**
 * @author sean
 * @version Id:,v0.1 2018/6/6 下午9:50 sean Exp $
 * @description
 */
public interface MessagePublisher {

    /**
     * 消息投递（Oneway方式）
     *
     * @param topic 主题
     * @param eventCode 事件码
     * @param payload 业务数据
     * @return
     * @throws MQException
     */
    boolean sendOneway(String topic, String eventCode, String payload) throws MQException;

    /**
     * 消息投递（同步方式）
     *
     * @param topic 主题
     * @param eventCode 事件码
     * @param payload 业务数据
     * @return
     * @throws MQException
     */
    boolean send(String topic, String eventCode, String payload) throws MQException;

    /**
     * 消息投递（同步方式）
     *
     * @param topic 主题
     * @param eventCode 事件码
     * @param payload 业务数据
     * @param timeout 投递超时时间
     * @return
     * @throws MQException
     */
    boolean send(String topic, String eventCode, String payload, long timeout) throws MQException;

}
