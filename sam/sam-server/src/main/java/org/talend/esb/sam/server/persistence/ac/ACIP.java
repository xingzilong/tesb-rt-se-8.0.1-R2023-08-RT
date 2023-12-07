package org.talend.esb.sam.server.persistence.ac;

import com.google.gson.annotations.Expose;

import java.util.Date;

/**
 * 数据库表 access_control_ip 对应的实体类
 *
 */
public class ACIP {

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
     * 启用黑名单控制时，值为 {@link org.talend.esb.sam.agent.ac.util.Constants} 中的 IP_TYPE_BLACK
     * 启用白名单控制时，值为 {@link org.talend.esb.sam.agent.ac.util.Constants} 中的 IP_TYPE_WHITE
     * 注意：不存在同时启动黑名单和白名单的情况
     */
    @Expose
    private String type;

    /**
     * IP黑名单
     */
    @Expose
    private String blackList;

    /**
     * IP白名单
     */
    @Expose
    private String whiteList;

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

    public ACIP() {
    }

    public ACIP(String id, String serviceKey, String type, String blackList, String whiteList, String status, Date createTime, Date updateTime) {
        this.id = id;
        this.serviceKey = serviceKey;
        this.type = type;
        this.blackList = blackList;
        this.whiteList = whiteList;
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

    public String getBlackList() {
        return blackList;
    }

    public void setBlackList(String blackList) {
        this.blackList = blackList;
    }

    public String getWhiteList() {
        return whiteList;
    }

    public void setWhiteList(String whiteList) {
        this.whiteList = whiteList;
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
