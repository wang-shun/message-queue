/**
 * io.terminus
 * Copyright(c) 2012-2018 All Rights Reserved.
 */
package io.terminus.mq.ons.producer;

import io.terminus.mq.config.MQProducerProperties;
import io.terminus.mq.exception.MQException;
import lombok.Data;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author sean
 * @version Id:,v0.1 2018/6/8 下午2:04 sean Exp $
 * @description
 */
@Component
@Data
public class OnsPublisherHolder implements DisposableBean {

    @Autowired
    private MQProducerProperties producerProperties;

    private OnsPublisher         onsPublisher;

    public void init() {
        try {
            String producerId = producerProperties.getProducerId();
            int timeout = producerProperties.getTimeout();
            String nameServerAddr = producerProperties.getNameServer();
            String accessKey = producerProperties.getAccessKey();
            String secretKey = producerProperties.getSecretKey();

            onsPublisher = new OnsPublisher(nameServerAddr, producerId, accessKey, secretKey, timeout);
            onsPublisher.start();
        } catch (MQException e) {
            throw new RuntimeException("message producer init fail");
        }
    }

    @Override
    public void destroy() throws Exception {
        onsPublisher.shutdown();
    }
}
