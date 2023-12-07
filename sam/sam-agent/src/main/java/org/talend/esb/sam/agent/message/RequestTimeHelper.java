package org.talend.esb.sam.agent.message;

import org.apache.cxf.message.Message;

import java.util.Date;

/**
 * 设置请求开始时间
 */
public class RequestTimeHelper {

    public static final String REQUEST_TIME_KEY = "RequestTime";

    private RequestTimeHelper() {
    }

    public static Date getRequestTime(Message message) {
        return (Date) message.get(REQUEST_TIME_KEY);
    }

    public static void setRequestTime(Message message, Date requestTime) {
        message.put(REQUEST_TIME_KEY, requestTime);
    }
}
