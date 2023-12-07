package org.talend.esb.sam.agent.ac.flow;

import java.io.Serializable;

/**
 * 流量控制规则
 */
public class FlowRule implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 启用类型 三种情况：
     * 一、值为 {@link org.talend.esb.sam.agent.ac.util.Constants} 中的 FLOW_TYPE_SINGLE，
     * 启动单次流量控制，即对单次请求的流量大小进行控制。
     * 二、值为 {@link org.talend.esb.sam.agent.ac.util.Constants} 中的 FLOW_TYPE_INTERVAL_GLOBAL，
     * 启用区间-全局流量控制，且是全局，即流量的控制是针对服务而言。
     * 三、值为 {@link org.talend.esb.sam.agent.ac.util.Constants} 中的 FLOW_TYPE_INTERVAL_CONSUMER，
     * 启用区间-消费方流量控制，即流量的控制在针对服务的基础上，细分消费者，消费者的差异根据消费方IP区分。
     * <p>
     * 注意：不存在同时启用以上两种类型以上的情况，有且仅有以上情况中的一种
     */
    private String type;

    /**
     * 定义流量控制规则的时间区间，其秒为单位
     */
    private long timeInterval;

    /**
     * 定义流量控制规则的阈值，即在规定的时间区间内允许访问的最大流量阈值
     */
    private long intervalThreshold;

    /**
     * 定义流量控制规则的单次流量阈值
     */
    private long singleThreshold;

    /**
     * 状态：0-禁用，1-正常
     */
    private String status;

    public FlowRule() {
    }

    public FlowRule(String type, long timeInterval, long intervalThreshold, long singleThreshold, String status) {
        this.type = type;
        this.timeInterval = timeInterval;
        this.intervalThreshold = intervalThreshold;
        this.singleThreshold = singleThreshold;
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getTimeInterval() {
        return timeInterval;
    }

    public void setTimeInterval(long timeInterval) {
        this.timeInterval = timeInterval;
    }

    public long getIntervalThreshold() {
        return intervalThreshold;
    }

    public void setIntervalThreshold(long intervalThreshold) {
        this.intervalThreshold = intervalThreshold;
    }

    public long getSingleThreshold() {
        return singleThreshold;
    }

    public void setSingleThreshold(long singleThreshold) {
        this.singleThreshold = singleThreshold;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
