package org.talend.esb.sam.agent.ac.frequency;

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
 * 频度控制拦截器
 * 两种策略:
 * 一、全局频次控制，控制的最小粒度为每个服务。
 * 二、消费方频次控制，控制的最小粒度在针对每个服务的基础之上再区分不同的消费方
 */
public class FrequencyInterceptor extends AbstractPhaseInterceptor<Message> {

    public FrequencyInterceptor() {
        super(Phase.PRE_INVOKE);
    }

    @Override
    public void handleMessage(Message message) throws Fault {
        // 指定CXF获取客户端的HttpServletRequest : http-request；
        HttpServletRequest request = (HttpServletRequest) message.get(AbstractHTTPDestination.HTTP_REQUEST);
        if (null != request) {
            // 获取 serviceaddress
            String serviceAddress = message.getExchange().getEndpoint().getEndpointInfo().getAddress();
            // 先检测此服务是否具备访问控制策略，没有则直接放行
            FrequencyRule frequencyRule = FrequencyRuleTable.getInstance().getRule(serviceAddress);
            // 若frequencyRule为 null 或者 status 为“0”，都视为该服务未开启频度控制
            if (frequencyRule == null || frequencyRule.getStatus().equals("0")) {
                return;
            }
            // 消费方IP地址
            String consumerIP = IPUtil.getConsumerIP(request);
            long currentTime = System.currentTimeMillis();
            boolean result = controlHandler(frequencyRule, currentTime, serviceAddress, consumerIP);
            if (!result) {
                FailureCauseHelper.setFailureCause(message, Constants.REQUEST_FAILURE_CAUSE_FREQUENCY);
                throw new Fault(new ACExcetion("【" + serviceAddress + "】服务超出频次控制阈值，无效访问。"));
            }
        }
    }

    /**
     * 频次控制处理程序
     *
     * @param frequencyRule
     * @param currentTime
     * @param serviceIdentification
     * @param consumerIdentification
     * @return
     */
    public static boolean controlHandler(FrequencyRule frequencyRule, long currentTime, String serviceIdentification, String consumerIdentification) {
        String type = frequencyRule.getType();
        long timeInterval = frequencyRule.getTimeInterval();
        long threshold = frequencyRule.getThreshold();
        // 获取调用记录
        FrequencyRecord frequencyRecord = null;
        if (type.equals(Constants.FTEQUENCY_TYPE_GLOBAL)) {
            // 获取调用记录
            frequencyRecord = FrequencyRecordTable.getInstance().getFrequencyRecord(currentTime, serviceIdentification);
        }
        if (type.equals(Constants.FTEQUENCY_TYPE_CONSUMER)) {
            // 获取调用记录
            frequencyRecord = FrequencyRecordTable.getInstance().getFrequencyRecord(currentTime, serviceIdentification, consumerIdentification);
        }
        long ati = timeInterval * 1000L + frequencyRecord.getFirstTimestamp();
        // 此次请求距离上次请求的间隔时间超过设置的请求区间，重置记录表中的对象数据
        if (ati < currentTime) {
            FrequencyRecordTable.getInstance().reset(currentTime, frequencyRecord);
        }
        // 如果已经超过阈值，直接返回，不在进行累加
        if (frequencyRecord.getCount() > threshold) {
            return false;
        }
        // 此次请求和上次请求仍然在同一个时间区间中，对访问次数进行累加，并判断此次请求是否合法
        long count = frequencyRecord.incrCount();
        if (count > threshold) {
            return false;
        }
        return true;
    }
}
