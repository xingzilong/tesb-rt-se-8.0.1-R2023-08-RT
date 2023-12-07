package org.talend.esb.sam.agent.ac.util;

/**
 * 访问控制通用常量信息
 */
public class Constants {
    /**
     * IP黑白名单访问控制中使用黑名单控制的类型标志符
     */
    public static final String IP_TYPE_BLACK = "black";

    /**
     * IP黑白名单访问控制中使用白名单控制的类型标志符
     */
    public static final String IP_TYPE_WHITE = "white";

    /**
     * 频次访问控制中使用全局控制的类型标志符
     */
    public static final String FTEQUENCY_TYPE_GLOBAL = "global";

    /**
     * 频次访问控制中使用消费方控制的类型标志符
     */
    public static final String FTEQUENCY_TYPE_CONSUMER = "consumer";

    /**
     * 流量访问控制中使用单次流量控制的类型标志符
     */
    public static final String FLOW_TYPE_SINGLE = "single";

    /**
     * 流量访问控制中使用区间-全局控制的类型标志符
     */
    public static final String FLOW_TYPE_INTERVAL_GLOBAL = "global";

    /**
     * 流量访问控制中使用区间-消费方控制的类型标志符
     */
    public static final String FLOW_TYPE_INTERVAL_CONSUMER = "consumer";

    /**
     * 受IP控制访问控制规则而请求失败的原因，字符串描述
     */
    public static final String REQUEST_FAILURE_CAUSE_IP = "IP";

    /**
     * 受流量控制访问控制规则而请求失败的原因，字符串描述
     */
    public static final String REQUEST_FAILURE_CAUSE_FLOW = "FLOW";

    /**
     * 受频次控制访问控制规则而请求失败的原因，字符串描述
     */
    public static final String REQUEST_FAILURE_CAUSE_FREQUENCY = "FREQUENCY";

    /**
     * 报文的类型，用于标志此条报文日志是request（REQ）。
     */
    public static final String MESSAGE_TYPE_REQ = "REQ";

    /**
     * 报文的类型，用于标志此条报文日志是response（RESP）。
     */
    public static final String MESSAGE_TYPE_RESP = "RESP";

}
