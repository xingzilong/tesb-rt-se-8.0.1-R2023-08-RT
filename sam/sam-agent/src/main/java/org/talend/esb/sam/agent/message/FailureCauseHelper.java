package org.talend.esb.sam.agent.message;

import org.apache.cxf.message.Message;

/**
 * 设置服务请求失败的原因
 * 目前主要描述因访问控制而请求失败的情况
 */
public class FailureCauseHelper {

    public static final String FAILURE_CAUSE_KEY = "FailureCause";

    private FailureCauseHelper() {
    }

    public static String getFailureCause(Message message) {
        return (String) message.get(FAILURE_CAUSE_KEY);
    }

    public static void setFailureCause(Message message, String failureCause) {
        message.put(FAILURE_CAUSE_KEY, failureCause);
    }

}
