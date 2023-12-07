package org.talend.esb.sam.agent.ac.ip;

import java.util.HashMap;

/**
 * IP黑白名单规则定义对象
 */
public class IPRule {

    /**
     * 启用类型 两种情况：
     * 启用黑名单控制时，值为 {@link org.talend.esb.sam.agent.ac.util.Constants} 中的 IP_TYPE_BLACK
     * 启用白名单控制时，值为 {@link org.talend.esb.sam.agent.ac.util.Constants} 中的 IP_TYPE_WHITE
     * 注意：不存在同时启动黑名单和白名单的情况
     */
    private String type;

    /**
     * IP黑名单 key和value的值是一样的，都是IPV4的地址，之所以使用HashMap数据结构而不使用List，
     * 是因为（在大量服务接口都配置了IP控制规则的情况下）在进行取值对比筛选的时候HahsMap的时间复杂度要远远小于List，
     * 即使hash冲突导致链表过长，查询效率任然优于List。
     * 选择HashMap也有相应的缺点，对内存的占用会略高于List，但是我认为可以忽略
     *
     * @author xingzilong
     */
    private HashMap<String, String> blackList;

    /**
     * IP白名单 key和value的值是一样的，都是IPV4的地址，之所以使用HashMap数据结构而不使用List，
     * 是因为（在大量服务接口都配置了IP控制规则的情况下）在进行取值对比筛选的时候HahsMap的时间复杂度要远远小于List，
     * 即使hash冲突导致链表过长，查询效率任然优于List。
     * 选择HashMap也有相应的缺点，对内存的占用会略高于List，但是我认为可以忽略
     *
     * @author xingzilong
     */
    private HashMap<String, String> whiteList;

    /**
     * 状态：0-禁用，1-正常
     */
    private String status;

    public IPRule() {
    }

    public IPRule(String type, HashMap<String, String> blackList, HashMap<String, String> whiteList, String status) {
        this.type = type;
        this.blackList = blackList;
        this.whiteList = whiteList;
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public HashMap<String, String> getBlackList() {
        return blackList;
    }

    public void setBlackList(HashMap<String, String> blackList) {
        this.blackList = blackList;
    }

    public HashMap<String, String> getWhiteList() {
        return whiteList;
    }

    public void setWhiteList(HashMap<String, String> whiteList) {
        this.whiteList = whiteList;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
