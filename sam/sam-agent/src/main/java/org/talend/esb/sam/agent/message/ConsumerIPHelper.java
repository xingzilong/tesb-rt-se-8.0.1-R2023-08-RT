package org.talend.esb.sam.agent.message;

import org.apache.cxf.message.Message;

/**
 * 设置消费方IP
 */
public class ConsumerIPHelper {

    public static final String CONSUMER_IP_KEY = "ConsumerIP";

    private ConsumerIPHelper() {
    }

    public static String getConsumerIP(Message message) {
        return (String) message.get(CONSUMER_IP_KEY);
    }

    public static void setConsumerIP(Message message, String consumerIP) {
        message.put(CONSUMER_IP_KEY, consumerIP);
    }
}
