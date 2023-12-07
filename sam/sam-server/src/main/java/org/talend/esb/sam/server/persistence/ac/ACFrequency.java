package org.talend.esb.sam.server.persistence.ac;

import com.google.gson.annotations.Expose;

import java.util.Date;

/**
 * 数据库表 access_control_frequency 对应的实体类
 *
 */
public class ACFrequency {

    /**
     * 数据库自增主键
     */
    @Expose
    private String id;

    /**
     * 服务（webservice接口）的唯一标识，目前的取值为serviceaddress
     */
    @Expose
    private String serviceKey;

    /**
     * 启用类型 两种情况：
     * 一、值为 {@link org.talend.esb.sam.agent.ac.util.Constants} 中的 FTEQUENCY_TYPE_GLOBAL，
     * 启用全局控制，即频次的控制是针对服务而言。
     * 二、值为 {@link org.talend.esb.sam.agent.ac.util.Constants} 中的 FTEQUENCY_TYPE_CONSUMER，
     * 启用消费方控制，即频次的控制在针对服务的基础上，细分消费者，消费者的差异根据消费方IP区分。
     *
     * 注意：不存在同时启用以上两种类型以上的情况，有且仅有以上情况中的一种
     */
    @Expose
    private String type;

    /**
     * 定义频率规则的时间区间，其秒为单位
     */
    @Expose
    private long timeInterval;

    /**
     * 定义频率规则的阈值，即在规定的时间区间内允许访问的最大次数
     */
    @Expose
    private long threshold;

    /**
     * 状态：0-禁用，1-正常
     */
    @Expose
    private String status;

    /**
     * 创建时间
     */
    @Expose(serialize = true, deserialize = false)
    private Date createTime;

    /**
     * 更新时间
     */
    @Expose(serialize = true, deserialize = false)
    private Date updateTime;

    public ACFrequency() {
    }

    public ACFrequency(String id, String serviceKey, String type, long timeInterval, long threshold, String status, Date createTime, Date updateTime) {
        this.id = id;
        this.serviceKey = serviceKey;
        this.type = type;
        this.timeInterval = timeInterval;
        this.threshold = threshold;
        this.status = status;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getServiceKey() {
        return serviceKey;
    }

    public void setServiceKey(String serviceKey) {
        this.serviceKey = serviceKey;
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

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
