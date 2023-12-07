package org.talend.esb.sam.agent.ac.flow;

import java.io.Serializable;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 流量规则表
 * key 为服务（webservice接口）的唯一标识
 * value {@link FlowRule}类型对象，为该服务对应的流量控制规则
 */
public class FlowRuleTable<K, V> extends ConcurrentHashMap<K, V> implements Serializable {
	private static final long serialVersionUID = 1L;

	private static FlowRuleTable<String, FlowRule> instance = new FlowRuleTable<String, FlowRule>();

	private FlowRuleTable() {

	}

	public static FlowRuleTable<String, FlowRule> getInstance() {
		return instance;
	}

	/**
	 * 获取指定服务的流量控制规则
	 *
	 * @param serviceIdentification 服务的唯一标识
	 * @return
	 */
	public FlowRule getRule(String serviceIdentification) {
		return instance.get(serviceIdentification);
	}

	/**
	 * 设置指定服务的流量控制规则
	 *
	 * @param serviceIdentification 服务的唯一标识
	 * @return
	 */
	public FlowRule setRule(String serviceIdentification, FlowRule flowRule) {
		return instance.put(serviceIdentification, flowRule);
	}

	/**
	 * 删除指定服务的流量控制规则
	 *
	 * @param serviceIdentification 服务的唯一标识
	 * @return
	 */
	public FlowRule removeRule(String serviceIdentification) {
		return instance.remove(serviceIdentification);
	}

}
