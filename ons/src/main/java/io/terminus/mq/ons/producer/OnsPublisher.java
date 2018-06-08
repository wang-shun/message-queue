/**
 * io.terminus
 * Copyright(c) 2012-2018 All Rights Reserved.
 */
package io.terminus.mq.ons.producer;

import com.aliyun.openservices.ons.api.*;
import io.terminus.mq.common.UniformEventPublisher;
import io.terminus.mq.exception.MQException;
import io.terminus.mq.model.DefaultUniformEvent;
import io.terminus.mq.model.UniformEvent;
import io.terminus.mq.utils.JacksonUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.Properties;

/**
 * @author sean
 * @version Id:,v0.1 2018/6/8 下午12:12 sean Exp $
 * @description
 */
@Slf4j
public class OnsPublisher implements UniformEventPublisher {

    /** 注册中心地址 */
    private String   nameServerAddr;

    /** 生产者Id */
    private String   producerId;

    /** accessKey */
    private String   accessKey;

    /** secretKey */
    private String   secretKey;

    /** 发送的超时时间 */
    private int      sendTimeOut;

    /** 生产者Id */
    private Producer producer;

    public OnsPublisher(String nameServerAddr, String producerId, String accessKey, String secretKey, int sendTimeOut) {
        this.nameServerAddr = nameServerAddr;
        this.producerId = producerId;
        this.accessKey = accessKey;
        this.secretKey = secretKey;
        this.sendTimeOut = sendTimeOut;
    }

    @Override
    public void start() throws MQException {

        // container 实例配置初始化
        Properties properties = new Properties();
        //在控制台创建的Producer ID
        properties.setProperty(PropertyKeyConst.ProducerId, producerId);
        // AccessKey 阿里云身份验证，在阿里云服务器管理控制台创建
        properties.setProperty(PropertyKeyConst.AccessKey, accessKey);
        // SecretKey 阿里云身份验证，在阿里云服务器管理控制台创建
        properties.setProperty(PropertyKeyConst.SecretKey, secretKey);
        //设置发送超时时间，单位毫秒
        properties.setProperty(PropertyKeyConst.SendMsgTimeoutMillis, Integer.toString(sendTimeOut));
        // 设置 TCP 接入域名
        properties.setProperty(PropertyKeyConst.ONSAddr, nameServerAddr);
        producer = ONSFactory.createProducer(properties);
        // 在发送消息前，必须调用start方法来启动Producer，只需调用一次即可
        producer.start();
    }

    @Override
    public boolean publishUniformEvent(UniformEvent event) throws MQException {
        if (event == null) {
            throw new IllegalArgumentException("UniformEvent is null");
        }

        Message message = createOnsMessage(event);

        try {
            SendResult sendResult = producer.send(message);
            log.info("消息发送，消息Id:{}，RocketMQ消息：{}", sendResult.getMessageId(), JacksonUtils.toJson(event.getPayload()));
            return true;
        } catch (RuntimeException e) {
            throw new MQException(e);
        }
    }

    /**
     *
     * @param event
     * @return
     * @throws MQException
     */
    private Message createOnsMessage(UniformEvent event) throws MQException {
        try {
            // 序列化数据
            byte[] data = JacksonUtils.toJson(event.getPayload()).getBytes();

            Message message = new Message(event.getTopic(), event.getEventCode(), data);

            return message;
        } catch (Exception e) {
            throw new MQException("消息实体构建失败");
        }
    }

    @Override
    public boolean publishUniformEventOneway(UniformEvent event) throws MQException {
        if (event == null) {
            throw new IllegalArgumentException("UniformEvent is null");
        }
        Message message = createOnsMessage(event);

        try {
            producer.sendOneway(message);
            log.info("Oneway消息发送成功，RocketMQ消息：{}", JacksonUtils.toJson(event.getPayload()));
            return true;
        } catch (RuntimeException e) {
            throw new MQException(e);
        }
    }

    @Override
    public UniformEvent createUniformEvent(String topic, String eventCode) {
        return new DefaultUniformEvent(topic, eventCode);
    }

    @Override
    public UniformEvent createUniformEvent(String topic, String eventCode, boolean transactional) {
        UniformEvent e = new DefaultUniformEvent(topic, eventCode);
        e.setTransactional(transactional);
        return e;
    }

    @Override
    public UniformEvent createUniformEvent(String topic, String eventCode, boolean transactional, Object payload) {
        UniformEvent e = new DefaultUniformEvent(topic, eventCode);
        e.setTransactional(transactional);
        e.setPayload(payload);
        return e;
    }

    @Override
    public void shutdown() throws MQException {
        producer.shutdown();
    }

    @Override
    public String getGroup() {
        return producerId;
    }

    @Override
    public String getNameSrvAddress() {
        return nameServerAddr;
    }
}
