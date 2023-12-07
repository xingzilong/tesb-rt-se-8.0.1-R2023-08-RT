package org.talend.esb.sam.agent.ac;

import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;

import java.util.logging.Logger;

/**
 * 访问控制拦截器
 */
public class AccessControlInterceptor extends AbstractPhaseInterceptor<Message> {

	private static final Logger LOG = Logger.getLogger(AccessControlInterceptor.class.getName());

	public AccessControlInterceptor() {
		super(Phase.PRE_INVOKE);
	}

	@Override
	public void handleMessage(Message message) throws Fault {
//        //指定CXF获取客户端的HttpServletRequest : http-request；
//        HttpServletRequest request = (HttpServletRequest) message.get(AbstractHTTPDestination.HTTP_REQUEST);
//        if (null != request) {
//        	//获取服务地址
//        	String serviceAddress = message.getExchange().getEndpoint().getEndpointInfo().getAddress();
//        	// 消费方IP地址
//        	String consumerIP = ConsumerIP.getConsumerIP(request);
//			// 获取用户的数据包大小
//			long length = request.getContentLength();
//			long timestamp = System.currentTimeMillis();
//
//			//IP黑白名单控制
//            if (blackControl(serviceAddress, consumerIP)) {
//            	FailureCauseHelper.setFailureCause(message, "IP");
//            	LOG.warning("IP [" + consumerIP + "] failed to call service ["+serviceAddress+"].Reason: IP black-and-white list control." );
//                throw new Fault(new IllegalAccessException("The call failed, reason: IP [" + consumerIP + "] is in the blacklist and access is not allowed!"));
//            }
//            if(whiteControl(serviceAddress, consumerIP)){
//            	FailureCauseHelper.setFailureCause(message, "IP");
//            	LOG.warning("IP [" + consumerIP + "] failed to call service ["+serviceAddress+"].Reason: IP black-and-white list control." );
//            	throw new Fault(new IllegalAccessException("The call failed, reason: Illegal IP [" + consumerIP + "] access not allowed!"));
//            }
//
//            //流量控制
//			if (cycleControl(serviceAddress, consumerIP, length, timestamp)) {
//				FailureCauseHelper.setFailureCause(message, "FLOW");
//				LOG.warning("IP [" + consumerIP + "] failed to call service ["+serviceAddress+"].Reason: Flow control." );
//				throw new Fault(new IllegalAccessException("The call failed, reason: The requested packet size exceeds the traffic threshold!"));
//			}
//			if (singleControl(serviceAddress, consumerIP, length)) {
//				FailureCauseHelper.setFailureCause(message, "FLOW");
//				LOG.warning("IP [" + consumerIP + "] failed to call service ["+serviceAddress+"].Reason: Flow control." );
//				throw new Fault(new IllegalAccessException("The call failed, reason: The requested packet size exceeds the traffic threshold!"));
//			}
//
//			//频度控制
//    		if (frequencycontrol(timestamp, serviceAddress, consumerIP)) {
//    			long count = FrequencyRecordTable.getFrequency(serviceAddress, consumerIP).getCount();
//    			FailureCauseHelper.setFailureCause(message, "FREQUENCY");
//    			LOG.warning("IP [" + consumerIP + "] failed to call service ["+serviceAddress+"].Reason: Frequency  control." );
//    			throw new Fault(new IllegalAccessException("The number of calls to ["+serviceAddress+"] service exceeds the frequency threshold. The service allows"+FrequencyDefinitionTable.getFrequencyDefinition(serviceAddress).getThreshold()+" visits every "+FrequencyDefinitionTable.getFrequencyDefinition(serviceAddress).getPeriod()+" seconds. This is the "+count+"th access!"));
//    		}
//        }
	}

//	/**
//	 * 黑名单控制
//	 *
//	 * @param serviceIdentification
//	 * @param consumerIdentification
//	 * @return
//	 */
//	public static boolean blackControl(String serviceIdentification, String consumerIdentification){
//		HashMap<String, String> ipBlackList = IPBlackListDefinitionTable.getIPBlackList(serviceIdentification);
//		//若ipBlackList为空或者集合大小为“0”，都视为该服务未开启黑名单控制
//		if (ipBlackList == null || ipBlackList.size() == 0) {
//			return false;
//		}
//		//黑名单中存在消费方IP
//		if (ipBlackList.get(consumerIdentification) != null) {
//			return true;
//		}
//		//启用了黑名单控制且消费方IP不在黑名单中
//		return false;
//	}
//
//	/**
//	 * 白名单控制
//	 * @param serviceIdentification
//	 * @param consumerIdentification
//	 * @return
//	 */
//	public static boolean whiteControl(String serviceIdentification, String consumerIdentification){
//		HashMap<String, String> ipWhiteList = IPWhiteListDefinitionTable.getIPWhiteList(serviceIdentification);
//		//若ipWhiteList为空或者集合大小为“0”，都视为该服务未开启黑名单控制
//		if (ipWhiteList == null || ipWhiteList.size() == 0) {
//			return false;
//		}
//		//白名单中存在消费方IP
//		if (ipWhiteList.get(consumerIdentification) != null) {
//			return false;
//		}
//		//启用了白名单控制且消费方IP不在白名单中
//		return true;
//	}
//
//	/**
//	 * 单次访问流量控制
//	 *
//	 * @param serviceIdentification
//	 * @param consumerIdentification
//	 * @param length
//	 * @return
//	 */
//	public static boolean singleControl(String serviceIdentification, String consumerIdentification,long length){
//		//此处为真正从map中获取实际的流量控制规则对象，并获取其相应的配置信息
//		FlowDefinition flowDefinition = FlowDefinitionTable.getFlowDefinition(serviceIdentification);
//		//若flowDefinition为空或者SingleThreshold的值小于“0”，都视为该服务未开启流量控制
//		if (flowDefinition == null || flowDefinition.getSingleThreshold() < 0) {
//			return false;
//		}
//		return length >= flowDefinition.getSingleThreshold();
//
//	}
//
//	/**
//	 * 周期访问流量控制
//	 * @param serviceIdentification
//	 * @param consumerIdentification
//	 * @param length
//	 * @param timestamp
//	 * @return
//	 */
//	public static boolean cycleControl(String serviceIdentification,String consumerIdentification,long length,long timestamp){
//		//此处FlowDefinition（流量控制的规则）对象的创建仅用于对两种拦截模式的判断区分
//		FlowDefinition flowDefinition = FlowDefinitionTable.getFlowDefinition(serviceIdentification);
//		//若flowDefinition为空或者Period的值小于“0”，都视为该服务未开启流量控制
//		if (flowDefinition == null || flowDefinition.getPeriod() < 0) {
//			return false;
//		}
//
//		long period = flowDefinition.getPeriod();
//		long threshold = flowDefinition.getThreshold();
//		Flow flow = FlowRecordTable.getFlow(timestamp, serviceIdentification, consumerIdentification);
//		long firstTimestamp = flow.getTimestamp();
//		long ttl = period * 1000L + firstTimestamp;
//		//此次请求距离上次请求的间隔时间超过设置的请求区间，重置记录表中的对象数据
//		if (ttl <System.currentTimeMillis()) {
//			//此次请求距离上次请求的间隔时间超过设置的请求区间，重置对象池中的对象数据
//			FlowRecordTable.reset(timestamp, serviceIdentification, consumerIdentification);
//		}
//		//此次请求和上次请求仍然在同一个时间区间中，对访问流量进行累加，并判断此次请求是否合法
//		long flowTotal = FlowRecordTable.incr(timestamp, serviceIdentification, consumerIdentification, length);
//		if (flowTotal > threshold) {
//			return true;
//		}
//		return false;
//
//	}
//
//	/**
//	 * 频度访问控制
//	 *
//	 * @param timestamp
//	 * @param serviceIdentification
//	 * @param consumerIdentification
//	 * @return
//	 */
//	public static boolean frequencycontrol(long timestamp, String serviceIdentification, String consumerIdentification){
//		FrequencyDefinition frequencyDefinition = FrequencyDefinitionTable.getFrequencyDefinition(serviceIdentification);
//		//若frequencyDefinition为空或者Period的值小于“0”，都视为该服务未开启频度控制
//		if (frequencyDefinition == null || frequencyDefinition.getPeriod() < 0) {
//			return false;
//		}
//		long period = frequencyDefinition.getPeriod();
//		long threshold = frequencyDefinition.getThreshold();
//		//timestam当前系统时间
//		Frequency frequency = FrequencyRecordTable.getFrequency(timestamp, serviceIdentification, consumerIdentification);
//		long ttl = period *1000L + frequency.getTimestamp();
//		//此次请求距离上次请求的间隔时间超过设置的请求区间，重置记录表中的对象数据
//		if (ttl < System.currentTimeMillis()) {
//			FrequencyRecordTable.reset(timestamp, serviceIdentification, consumerIdentification);
//		}
//		//此次请求和上次请求仍然在同一个时间区间中，对访问次数进行累加，并判断此次请求是否合法
//		long count = FrequencyRecordTable.incr(timestamp, serviceIdentification, consumerIdentification);
//		if (count > threshold) {
//			return true;
//		}
//		return false;
//	}


}
