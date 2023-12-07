package org.talend.esb.sam.agent.ac.frequency;

import java.io.Serializable;

/**
 * 频次规则定义对象
 */
public class FrequencyRule implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 启用类型 两种情况：
     * 一、值为 {@link org.talend.esb.sam.agent.ac.util.Constants} 中的 FTEQUENCY_TYPE_GLOBAL，
     * 启用全局控制，即频次的控制是针对服务而言。
     * 二、值为 {@link org.talend.esb.sam.agent.ac.util.Constants} 中的 FTEQUENCY_TYPE_CONSUMER，
     * 启用消费方控制，即频次的控制在针对服务的基础上，细分消费者，消费者的差异根据消费方IP区分。
     * <p>
     * 注意：不存在同时启用以上两种类型以上的情况，有且仅有以上情况中的一种
     */
    private String type;

    /**
     * 定义频率规则的时间区间，其秒为单位
     */
    private long timeInterval;

    /**
     * 定义频率规则的阈值，即在规定的时间区间内允许访问的最大次数
     */
    private long threshold;

    /**
     * 状态：0-禁用，1-正常
     */
    private String status;

    public FrequencyRule() {
    }

    public FrequencyRule(String type, long timeInterval, long threshold, String status) {
        this.type = type;
        this.timeInterval = timeInterval;
        this.threshold = threshold;
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

    public long getThreshold() {
        return threshold;
    }

    public void setThreshold(long threshold) {
        this.threshold = threshold;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
