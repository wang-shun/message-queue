/**
 * io.terminus
 * Copyright(c) 2012-2018 All Rights Reserved.
 */
package io.terminus.parana.producer;

import com.aliyun.openservices.ons.api.ONSFactory;
import com.aliyun.openservices.ons.api.Producer;
import com.aliyun.openservices.ons.api.PropertyKeyConst;
import io.terminus.parana.rules.Publisher;
import io.terminus.parana.config.MQProducerProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Properties;

/**
 * @author sean
 * @version Id:,v0.1 2018/6/4 下午8:29 sean Exp $
 * @description
 */
@Component
public class OnsPublisher implements Publisher {

    @Autowired
    private MQProducerProperties producerProperties;

    private static Producer      producer;

    public OnsPublisher() {
    }

    private static class ProducerBeanSingleTon {
        private static final OnsPublisher INSTANCE = new OnsPublisher();
    }

    public static final OnsPublisher getInstance() {
        return ProducerBeanSingleTon.INSTANCE;
    }

    /** 
     * 启动ONS 生产者客户端 
     */
    @Override
    public void init() {
        // producer 实例配置初始化
        Properties properties = new Properties();
        //在控制台创建的Producer ID
        properties.setProperty(PropertyKeyConst.ProducerId, producerProperties.getProducerId());
        // AccessKey 阿里云身份验证，在阿里云服务器管理控制台创建
        properties.setProperty(PropertyKeyConst.AccessKey, producerProperties.getAccessKey());
        // SecretKey 阿里云身份验证，在阿里云服务器管理控制台创建
        properties.setProperty(PropertyKeyConst.SecretKey, producerProperties.getSecretKey());
        //设置发送超时时间，单位毫秒
        properties.setProperty(PropertyKeyConst.SendMsgTimeoutMillis, Integer.toString(producerProperties.getTimeout()));
        // 设置 TCP 接入域名
        properties.setProperty(PropertyKeyConst.ONSAddr, producerProperties.getNameServer());
        producer = ONSFactory.createProducer(properties);
        // 在发送消息前，必须调用start方法来启动Producer，只需调用一次即可
        producer.start();
    }

    public Producer getProducer() {
        return producer;
    }
}
