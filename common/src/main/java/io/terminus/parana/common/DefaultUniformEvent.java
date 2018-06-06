/**
 * io.terminus
 * Copyright(c) 2012-2018 All Rights Reserved.
 */
package io.terminus.parana.common;

/**
 * @author sean
 * @version Id:,v0.1 2018/6/6 下午8:55 sean Exp $
 * @description
 */
public class DefaultUniformEvent implements UniformEvent {

    /** 消息ID */
    private String id;

    /** 消息主题 */
    private String topic;

    /** 消息事件码 */
    private String eventCode;

    /** 消息体 */
    private String payload;

    /** 发送超时时间 */
    private long   timeout;

    /** 延时消息等级 */
    private int    delayTimeLevel;

    /**
     * @param topic
     * @param eventCode
     */
    public DefaultUniformEvent(String topic, String eventCode) {
        this.topic = topic;
        this.eventCode = eventCode;
        //        this.id = UUIDGenerator.getInstance().generateTimeBasedUUID().toString();  todo
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    @Override
    public String getEventCode() {
        return eventCode;
    }

    @Override
    public void setPayload(String payload) {
        this.payload = payload;
    }

    public void setEventCode(String eventCode) {
        this.eventCode = eventCode;
    }

    @Override
    public String getPayload() {
        return payload;
    }

    @Override
    public long getTimeout() {
        return timeout;
    }

    @Override
    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }

    @Override
    public int getDelayTimeLevel() {
        return delayTimeLevel;
    }

    @Override
    public void setDelayTimeLevel(int delayTimeLevel) {
        this.delayTimeLevel = delayTimeLevel;
    }
}
