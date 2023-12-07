package org.talend.esb.sam.agent.ac.ip;

import java.io.Serializable;
import java.util.concurrent.ConcurrentHashMap;

/**
 * IP黑白名单规则表
 * key 为服务（webservice接口）的唯一标识
 * value {@link IPRule}类型对象，为该服务对应的IP黑白名单控制规则
 */
public class IPRuleTable<K, V> extends ConcurrentHashMap<K, V> implements Serializable {
    private static final long serialVersionUID = 1L;

    private static IPRuleTable<String, IPRule> instance = new IPRuleTable<String, IPRule>();

    private IPRuleTable() {

    }

    public static IPRuleTable<String, IPRule> getInstance() {
        return instance;
    }

    /**
     * 获取指定服务的IP黑白名单
     *
     * @param serviceIdentification 服务的唯一标识
     * @return
     */
    public IPRule getRule(String serviceIdentification) {
        return instance.get(serviceIdentification);
    }

    /**
     * 设置指定服务的IP黑白名单控制规则
     *
     * @param serviceIdentification 服务的唯一标识
     * @return
     */
    public IPRule setRule(String serviceIdentification, IPRule ipRule) {
        return instance.put(serviceIdentification, ipRule);
    }

    /**
     * 删除指定服务的IP黑白名单控制规则
     *
     * @param serviceIdentification 服务的唯一标识
     * @return
     */
    public IPRule removeRule(String serviceIdentification) {
        return instance.remove(serviceIdentification);
    }

}