/**
 * io.terminus
 * Copyright(c) 2012-2018 All Rights Reserved.
 */
package io.terminus.parana.producer;

/**
 * @author sean
 * @version Id:,v0.1 2018/6/4 下午9:41 sean Exp $
 * @description
 */
public interface OnsMessageProducer {

    boolean send(String topic, String tag, String payload);

    boolean sendOneway(String topic, String tag, String payload);

}
