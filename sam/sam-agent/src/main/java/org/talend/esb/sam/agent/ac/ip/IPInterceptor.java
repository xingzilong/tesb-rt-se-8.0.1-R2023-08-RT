package org.talend.esb.sam.agent.ac.ip;

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
import java.util.HashMap;

/**
 * IP黑白名单拦截器
 */
public class IPInterceptor extends AbstractPhaseInterceptor<Message> {

    public IPInterceptor() {
        super(Phase.PRE_INVOKE);
    }

    public void handleMessage(Message message) throws Fault {
        // 指定CXF获取客户端的HttpServletRequest : http-request；
        HttpServletRequest request = (HttpServletRequest) message.get(AbstractHTTPDestination.HTTP_REQUEST);
        if (null != request) {
            // 获取服务名称
            String serviceAddress = message.getExchange().getEndpoint().getEndpointInfo().getAddress();
            // 消费方IP地址
            String consumerIP = IPUtil.getConsumerIP(request);
            if (!controlHandler(serviceAddress, consumerIP)) {
                FailureCauseHelper.setFailureCause(message, Constants.REQUEST_FAILURE_CAUSE_IP);
                throw new Fault(new ACExcetion("【" + serviceAddress + "】服务IP黑白名单限制，无效访问。"));
            }
        }
    }

    /**
     * IP控制处理程序
     *
     * @param serviceIdentification
     * @param consumerIdentification
     * @return
     */
    public boolean controlHandler(String serviceIdentification, String consumerIdentification) {
        Boolean isRelease = false;
        IPRule ipRule = IPRuleTable.getInstance().getRule(serviceIdentification);
        if (ipRule == null || ipRule.getStatus().equals("0")) {
            return true;
        }
        String type = ipRule.getType();
        HashMap<String, String> blackList = ipRule.getBlackList();
        HashMap<String, String> whiteList = ipRule.getWhiteList();

        if (Constants.IP_TYPE_BLACK.equals(type)) {
            // 若blackList为空或者集合大小为“0”，都视为该服务未开启黑名单控制
            if (blackList == null || blackList.size() == 0) {
                return true;
            }
            // 黑名单中存在消费方IP
            if (blackList.get(consumerIdentification) != null) {
                return false;
            }
            isRelease = true;
        }
        if (Constants.IP_TYPE_WHITE.equals(type)) {
            // 若whiteList为空或者集合大小为“0”，都视为该服务未开启白名单控制
            if (whiteList == null || whiteList.size() == 0) {
                return false;
            }
            // 白名单中存在消费方IP
            if (whiteList.get(consumerIdentification) != null) {
                return true;
            }
            isRelease = false;
        }
        return isRelease;
    }

}
