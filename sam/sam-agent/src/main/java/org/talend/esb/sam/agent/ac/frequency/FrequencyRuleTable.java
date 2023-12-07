package org.talend.esb.sam.agent.ac.frequency;

import java.io.Serializable;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 频次规则表
 * key 为服务（webservice接口）的唯一标识
 * value {@link FrequencyRule}类型对象，为该服务对应的频次控制规则
 */
public class FrequencyRuleTable<K, V> extends ConcurrentHashMap<K, V> implements Serializable {
    private static final long serialVersionUID = 1L;

    private static FrequencyRuleTable<String, FrequencyRule> instance = new FrequencyRuleTable<String, FrequencyRule>();

    private FrequencyRuleTable() {

    }

    public static FrequencyRuleTable<String, FrequencyRule> getInstance() {
        return instance;
    }

    /**
     * 获取指定服务的频次控制规则
     *
     * @param serviceIdentification 服务的唯一标识
     * @return
     */
    public FrequencyRule getRule(String serviceIdentification) {
        return instance.get(serviceIdentification);
    }

    /**
     * 设置指定服务的频次控制规则
     *
     * @param serviceIdentification 服务的唯一标识
     * @return
     */
    public FrequencyRule setRule(String serviceIdentification, FrequencyRule frequencyRule) {
        return instance.put(serviceIdentification, frequencyRule);
    }

    /**
     * 删除指定服务的频次控制规则
     *
     * @param serviceIdentification 服务的唯一标识
     * @return
     */
    public FrequencyRule removeRule(String serviceIdentification) {
        return instance.remove(serviceIdentification);
    }

}
