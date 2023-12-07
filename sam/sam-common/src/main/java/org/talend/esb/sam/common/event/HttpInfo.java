package org.talend.esb.sam.common.event;

import java.io.Serializable;

/**
 * http详细信息，主要包含http请求行和请求头等信息
 *
 */
public class HttpInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 服务接口的唯一标识，目前使用的时serviceaddress作为其值
     */
    private String serviceKey;

    /**
     * http请求方法，http请求行中的内容信息之一
     */
    private String httpMethod;

    /**
     * URI，http请求行中的内容信息之一
     */
    private String uri;

    /**
     * 参数字符串，http请求行中的内容信息之一
     */
    private String queryString;

    /**
     * 协议，http请求行中的内容信息之一
     */
    private String protocol;

    /**
     * http的请求和响应头部信息（header），是HashMap的JSON字符串
     */
    private String httpHeaders;

    /**
     * 消费方IP，即请求该服务接口的客户端IP地址，目前只能是IPV4的地址
     */
    private String consumerIP;

    /**
     * http状态码
     */
    private int httpStatus;

    /**
     * 响应时间，一个服务接口从请求至相应之间的耗时，以毫秒为单位
     */
    private long responseTime;

    /**
     * 失败原因，若此次请求为非正常（失败）响应，则记录错误相应信息（失败响应的原因）
     */
    private String failureCause;

    /**
     * 报文的类型，用于标志此条报文日志是request（REQ）还是response（RESP）。
     */
    private String messageType;

    public HttpInfo() {
    }

    public HttpInfo(String serviceKey, String httpMethod, String uri, String queryString, String protocol,
                    String httpHeaders, String consumerIP, int httpStatus, long responseTime,
                    String failureCause, String messageType) {
        this.serviceKey = serviceKey;
        this.httpMethod = httpMethod;
        this.uri = uri;
        this.queryString = queryString;
        this.protocol = protocol;
        this.httpHeaders = httpHeaders;
        this.consumerIP = consumerIP;
        this.httpStatus = httpStatus;
        this.responseTime = responseTime;
        this.failureCause = failureCause;
        this.messageType = messageType;
    }

    public String getServiceKey() {
        return serviceKey;
    }

    public void setServiceKey(String serviceKey) {
        this.serviceKey = serviceKey;
    }

    public String getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(String httpMethod) {
        this.httpMethod = httpMethod;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getQueryString() {
        return queryString;
    }

    public void setQueryString(String queryString) {
        this.queryString = queryString;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getHttpHeaders() {
        return httpHeaders;
    }

    public void setHttpHeaders(String httpHeaders) {
        this.httpHeaders = httpHeaders;
    }

    public String getConsumerIP() {
        return consumerIP;
    }

    public void setConsumerIP(String consumerIP) {
        this.consumerIP = consumerIP;
    }

    public int getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(int httpStatus) {
        this.httpStatus = httpStatus;
    }

    public long getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(long responseTime) {
        this.responseTime = responseTime;
    }

    public String getFailureCause() {
        return failureCause;
    }

    public void setFailureCause(String failureCause) {
        this.failureCause = failureCause;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }
}
