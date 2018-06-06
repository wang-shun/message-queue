/**
 * io.terminus
 * Copyright(c) 2012-2018 All Rights Reserved.
 */
package io.terminus.parana.common;

import java.io.Serializable;

/**
 * @author sean
 * @version Id:,v0.1 2018/6/4 下午10:44 sean Exp $
 * @description
 */
public interface UniformEvent extends Serializable {

    /**
     *
     * @return
     */
    String getId();

    void setId(String id);

    /**
     * 获取主题
     *
     * @return
     */
    String getTopic();

    /**
     * 获取事件码
     *
     * @return
     */
    String getEventCode();

    /**
     * 设置消息载体
     *
     * @param payload
     */
    void setPayload(String payload);

    /**
     * 获取消息载体
     *
     * @return
     */
    String getPayload();

    /**
     * 设置发送超时时间
     *
     * @param timeout
     */
    void setTimeout(long timeout);

    /**
     * 获取发送超时时间
     *
     * @return
     */
    long getTimeout();

    /**
     * 设置延时消息等级
     *
     * @param level
     */
    void setDelayTimeLevel(int level);

    /**
     * 获取延时消息等级
     *
     * @return
     */
    int getDelayTimeLevel();
}
