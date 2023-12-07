package org.talend.esb.sam.agent.ac.flow;

import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;
import org.apache.cxf.transport.http.AbstractHTTPDestination;
import org.talend.esb.sam.agent.ac.ACExcetion;
import org.talend.esb.sam.agent.ac.util.Constants;
import org.talend.esb.sam.agent.ac.util.IPUtil;
import org.talend.esb.sam.agent.message.FailureCauseHelper;

import javax.servlet.http.HttpServletRequest;

/**
 * 流量控制拦截器
 */
public class FlowInterceptor extends AbstractPhaseInterceptor<Message> {

    public FlowInterceptor() {
        super(Phase.PRE_INVOKE);
    }

    public void handleMessage(Message message) throws Fault {
        // 指定CXF获取客户端的HttpServletRequest : http-request；
        HttpServletRequest request = (HttpServletRequest) message.get(AbstractHTTPDestination.HTTP_REQUEST);
        if (null != request) {
            // 获取服务名称
            String serviceAddress = message.getExchange().getEndpoint().getEndpointInfo().getAddress();
            // 先检测此服务是否具备访问控制策略，没有则直接放行
            FlowRule flowRule = FlowRuleTable.getInstance().getRule(serviceAddress);
            // 若flowRule为 null 或者staus 为 “0”，都视为该服务未开启流量控制
            if (flowRule == null || flowRule.getStatus().equals("0")) {
                return;
            }
            // 消费方IP地址
            String consumerIP = IPUtil.getConsumerIP(request);
            long currentTime = System.currentTimeMillis();
            // 获取用户的数据包大小
            long currentFlowSize = request.getContentLengthLong();
            if (currentFlowSize < 0) {
                String queryString = (String) message.get(Message.QUERY_STRING);
                if (null == queryString && null != message.getExchange().getInMessage()) {
                    queryString = (String) message.getExchange().getInMessage().get(Message.QUERY_STRING);
                }
                currentFlowSize = queryString == null ? request.getRequestURI().length() : queryString.length();
            }
            if (!controlHandler(flowRule, currentTime, serviceAddress, consumerIP, currentFlowSize)) {
                FailureCauseHelper.setFailureCause(message, Constants.REQUEST_FAILURE_CAUSE_FLOW);
                throw new Fault(new ACExcetion("【" + serviceAddress + "】服务超出流量控制阈值，无效访问。"));
            }
        }
    }

    /**
     * 流量控制处理程序
     * <p>
     * param flowRule
     *
     * @param currentTime
     * @param serviceIdentification
     * @param consumerIdentification
     * @return
     */
    public static boolean controlHandler(FlowRule flowRule, long currentTime, String serviceIdentification, String consumerIdentification, Long currentFlowSize) {
        String type = flowRule.getType();
        long timeInterval = flowRule.getTimeInterval();
        long intervalThreshold = flowRule.getIntervalThreshold();
        long singleThreshold = flowRule.getSingleThreshold();
        if (type.equals(Constants.FLOW_TYPE_SINGLE) && singleThreshold < currentFlowSize) {
            return false;
        }
        if (type.equals(Constants.FLOW_TYPE_SINGLE) && singleThreshold >= currentFlowSize) {
            return true;
        }
        // 获取调用记录
        FlowRecord flowRecord = null;
        if (type.equals(Constants.FLOW_TYPE_INTERVAL_GLOBAL)) {
            // 获取调用记录
            flowRecord = FlowRecordTable.getInstance().getFlowRecord(currentTime, serviceIdentification);
        }
        if (type.equals(Constants.FLOW_TYPE_INTERVAL_CONSUMER)) {
            // 获取调用记录
            flowRecord = FlowRecordTable.getInstance().getFlowRecord(currentTime, serviceIdentification, consumerIdentification);
        }
        long ati = timeInterval * 1000L + flowRecord.getFirstTimestamp();
        // 此次请求距离上次请求的间隔时间超过设置的请求区间，重置记录表中的对象数据
        if (ati < currentTime) {
            FlowRecordTable.getInstance().reset(currentTime, flowRecord);
        }
        // 如果已经超过阈值，直接返回，不在进行累加
        if (flowRecord.getFlowSize() > intervalThreshold) {
            return false;
        }
        // 此次请求和上次请求仍然在同一个时间区间中，对流量进行累加，并判断此次请求是否合法
        long count = flowRecord.incrFlowSize(currentFlowSize);
        if (count > intervalThreshold) {
            return false;
        }
        return true;
    }
}
