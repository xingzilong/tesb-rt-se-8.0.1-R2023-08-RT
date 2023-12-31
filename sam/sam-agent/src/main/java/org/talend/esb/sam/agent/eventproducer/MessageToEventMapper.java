/*
 * #%L
 * Service Activity Monitoring :: Agent
 * %%
 * Copyright (c) 2006-2021 Talend Inc. - www.talend.com
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package org.talend.esb.sam.agent.eventproducer;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamReader;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.cxf.binding.soap.SoapBinding;
import org.apache.cxf.binding.soap.model.SoapBindingInfo;
import org.apache.cxf.configuration.security.AuthorizationPolicy;
import org.apache.cxf.io.CachedOutputStream;
import org.apache.cxf.message.Exchange;
import org.apache.cxf.message.Message;
import org.apache.cxf.message.MessageUtils;
import org.apache.cxf.security.SecurityContext;
import org.apache.cxf.service.model.BindingOperationInfo;
import org.apache.cxf.service.model.ServiceInfo;
import org.apache.cxf.transport.http.AbstractHTTPDestination;
import org.apache.cxf.ws.addressing.AddressingProperties;
import org.apache.cxf.ws.addressing.ContextUtils;
//import org.apache.cxf.ws.addressing.impl.AddressingPropertiesImpl;
import org.apache.cxf.service.model.ServiceModelUtil;
import org.talend.esb.sam.agent.ac.util.Constants;
import org.talend.esb.sam.agent.ac.util.IPUtil;
import org.talend.esb.sam.agent.feature.EventFeature;
import org.talend.esb.sam.agent.message.ConsumerIPHelper;
import org.talend.esb.sam.agent.message.CorrelationIdHelper;
import org.talend.esb.sam.agent.message.CustomInfo;
import org.talend.esb.sam.agent.message.FailureCauseHelper;
import org.talend.esb.sam.agent.message.FlowIdHelper;
import org.talend.esb.sam.agent.message.RequestTimeHelper;
import org.talend.esb.sam.agent.util.Converter;
import org.talend.esb.sam.common.event.Event;
import org.talend.esb.sam.common.event.EventTypeEnum;
import org.talend.esb.sam.common.event.HttpInfo;
import org.talend.esb.sam.common.event.MessageInfo;
import org.talend.esb.sam.common.event.Originator;

/**
 * The Class MessageToEventMapper.
 */
public class MessageToEventMapper {

    private static final Logger LOG = Logger.getLogger(MessageToEventMapper.class.getName());

    private static final String CUT_START_TAG = "<cut><![CDATA[";
    private static final String CUT_END_TAG = "]]></cut>";

    private int maxContentLength = -1;

    private Gson gson = new Gson();

    /**
     * Map to event.
     *
     * @param message
     *            the message
     * @return the event
     */
    public Event mapToEvent(Message message) {
        Event event = new Event();
        MessageInfo messageInfo = new MessageInfo();
        Originator originator = new Originator();
        boolean isRestMessage = isRestMessage(message);

        event.setMessageInfo(messageInfo);
        event.setOriginator(originator);
        String content = getPayload(message);
        event.setContent(content);
        handleContentLength(event);
        event.setEventType(null);
        Date date = new Date();
        event.setTimestamp(date);
        HttpInfo httpInfo = new HttpInfo();
        event.setHttpInfo(httpInfo);

//        if (isRestMessage) {
//            String queryString = (String) message.get(Message.QUERY_STRING);
//            if (queryString == null && message.getExchange().getInMessage() != null) {
//                queryString = (String) message.getExchange().getInMessage().get(Message.QUERY_STRING);
//            }
//            if (queryString != null && queryString.contains("_wadl")) {
//                return null;
//            }
//        }

        messageInfo.setFlowId(FlowIdHelper.getFlowId(message));
        if (!isRestMessage) {
            messageInfo.setMessageId(getMessageId(message));
            ServiceInfo serviceInfo = message.getExchange().getBinding().getBindingInfo().getService();
            if (null != serviceInfo) {
                String portTypeName = serviceInfo.getInterface().getName().toString();
                messageInfo.setPortType(portTypeName);
                messageInfo.setOperationName(getOperationName(message));
            }
            SoapBinding soapBinding = (SoapBinding) message.getExchange().getBinding();
            if (soapBinding.getBindingInfo() instanceof SoapBindingInfo) {
                SoapBindingInfo soapBindingInfo = (SoapBindingInfo) soapBinding.getBindingInfo();
                messageInfo.setTransportType(soapBindingInfo.getTransportURI());
            }
        } else {
            messageInfo.setTransportType("http://cxf.apache.org/transports/http");
            messageInfo.setPortType(message.getExchange().getEndpoint().getEndpointInfo().getName()
                    .toString());
            String opName = getRestOperationName(message);
            messageInfo.setOperationName(opName);
        }

        if (messageInfo.getTransportType() == null) {
            messageInfo.setTransportType("Unknown transport type");
        }

        // add custom properties from CXF properties
        if (null != message.getExchange().getEndpoint().get(EventFeature.SAM_PROPERTIES)) {
            Map<String, String> customProp =
                    (Map<String, String>) message.getExchange().getEndpoint().get(EventFeature.SAM_PROPERTIES);
            event.getCustomInfo().putAll(customProp);
        }

//        String addr = message.getExchange().getEndpoint().getEndpointInfo().getAddress();
//        if (null != addr) {
//            event.getCustomInfo().put("address", addr);
//        }

        String correlationId = CorrelationIdHelper.getCorrelationId(message);
        if (null != correlationId) {
            event.getCustomInfo().put("CorrelationID", correlationId);
        }

        try {
            InetAddress inetAddress = InetAddress.getLocalHost();
            originator.setIp(inetAddress.getHostAddress());
            originator.setHostname(inetAddress.getHostName());
        } catch (UnknownHostException e) {
            originator.setHostname("Unknown hostname");
            originator.setIp("Unknown ip address");
        }
        originator.setProcessId(Converter.getPID());

//        if (isRestMessage) {
//            //String queryString = (String) message.get(Message.QUERY_STRING);
//            //if (null == queryString && null != message.getExchange().getInMessage()) {
//            //    queryString = (String) message.getExchange().getInMessage().get(Message.QUERY_STRING);
//            //}
//            //if (null != queryString) {
//            //    event.getCustomInfo().put("Query String", queryString);
//            //}
//
//            String accept = (String) message.get(Message.ACCEPT_CONTENT_TYPE);
//            if (null != accept) {
//                event.getCustomInfo().put("Accept Type", accept);
//            }
//
//            //String httpMethod = (String) message.get(Message.HTTP_REQUEST_METHOD);
//            //if (null != httpMethod) {
//            //    event.getCustomInfo().put("HTTP Method", httpMethod);
//            //}
//
//            String contentType = (String) message.get(Message.CONTENT_TYPE);
//            if (null != contentType) {
//                event.getCustomInfo().put("Content Type", contentType);
//            }
//
//            Integer responseCode = (Integer) message.get(Message.RESPONSE_CODE);
//            if (null != responseCode) {
//                event.getCustomInfo().put("Response Code", responseCode.toString());
//            }
//        }

        SecurityContext sc = message.get(SecurityContext.class);
        if (sc != null && sc.getUserPrincipal() != null) {
            originator.setPrincipal(sc.getUserPrincipal().getName());
        }

        if (originator.getPrincipal() == null) {
            AuthorizationPolicy authPolicy = message.get(AuthorizationPolicy.class);
            if (authPolicy != null) {
                originator.setPrincipal(authPolicy.getUserName());
            }
        }

        EventTypeEnum eventType = getEventType(message);
        event.setEventType(eventType);

        CustomInfo customInfo = CustomInfo.getOrCreateCustomInfo(message);
        // System.out.println("custom props: " + customInfo);
        event.getCustomInfo().putAll(customInfo);

        // 访问控制新增，完善http请求响应的记录信息 2023-06-16 xingzilong
        // 判断是请求还是响应
        Exchange exchange = message.getExchange();
        Message inMessage = exchange.getInMessage();
        Message outMessage = exchange.getOutMessage();
        HttpServletRequest httpServletRequest = (HttpServletRequest) message.get(AbstractHTTPDestination.HTTP_REQUEST);
        HttpServletResponse httpServletResponse = (HttpServletResponse) message.get(AbstractHTTPDestination.HTTP_RESPONSE);
        if (inMessage == message && httpServletRequest != null) {
            // 处理请求逻辑
            HttpServletRequest request = (HttpServletRequest) message.get(AbstractHTTPDestination.HTTP_REQUEST);
            httpInfo.setMessageType(Constants.MESSAGE_TYPE_REQ);
            httpInfo.setHttpMethod(request.getMethod());
            httpInfo.setUri(request.getRequestURI());
            httpInfo.setQueryString(request.getQueryString() == null ? "" : request.getQueryString());
            httpInfo.setProtocol(request.getProtocol());
            Enumeration headerNames = request.getHeaderNames();
            HashMap<String, String> requestHeaders = new HashMap<String, String>();
            //使用循环遍历请求头，并通过getHeader()方法获取一个指定名称的头字段
            while (headerNames.hasMoreElements()) {
                String headerName = (String) headerNames.nextElement();
                requestHeaders.put(headerName, request.getHeader(headerName));
            }
            // 定义 Map 类型
            Type hashMapType = new TypeToken<HashMap<String, String>>() {
            }.getType();
            httpInfo.setHttpHeaders(gson.toJson(requestHeaders, hashMapType));
            httpInfo.setHttpStatus(0);
            httpInfo.setResponseTime(0L);
            RequestTimeHelper.setRequestTime(message, date);
            String consumerIP = IPUtil.getConsumerIP(request);
            if (null != consumerIP) {
                httpInfo.setConsumerIP(consumerIP);
                ConsumerIPHelper.setConsumerIP(message, consumerIP);
            }
        }
        // 很烦，我也不想知道怎么能明确的判断何时为请求，何时为响应，主要是代理时处理的不完美。
        // 毫无章法可言，存在httpServletRequest、httpServletResponse都为空的情况。
        // 暂时约定代理时不要对第二个构件进行勾选SAM选项，即只勾选第一个构件的SAM选项。
        // 以上述约定，可保证暂时没问题。 xingzilong 2023-07-13
        if ((outMessage == message || httpServletRequest == null) && httpServletResponse != null) {
            // 处理响应逻辑
            HttpServletResponse response = (HttpServletResponse) message.get(AbstractHTTPDestination.HTTP_RESPONSE);
            httpInfo.setMessageType(Constants.MESSAGE_TYPE_RESP);
            httpInfo.setHttpMethod("");
            httpInfo.setUri("");
            httpInfo.setQueryString("");
            httpInfo.setProtocol("");
            Collection<String> headerNames = response.getHeaderNames();
            HashMap<String, String> responseHeaders = new HashMap<>();
            //使用循环遍历请求头，并通过getHeader()方法获取一个指定名称的头字段
            Iterator<String> iterator = headerNames.iterator();
            while (iterator.hasNext()) {
                String headerName = iterator.next();
                responseHeaders.put(headerName, response.getHeader(headerName));
            }
            // 定义 Map 类型
            Type hashMapType = new TypeToken<HashMap<String, String>>() {
            }.getType();
            httpInfo.setHttpHeaders(gson.toJson(responseHeaders, hashMapType));
            httpInfo.setHttpStatus((Integer) message.get(Message.RESPONSE_CODE));
            Date resquestTime = RequestTimeHelper.getRequestTime(message);
            httpInfo.setResponseTime(date.getTime() - resquestTime.getTime());
            if ((Integer) message.get(Message.RESPONSE_CODE) != 200) {
                //提取失败原因
                httpInfo.setFailureCause(FailureCauseHelper.getFailureCause(message) == null ? "" : FailureCauseHelper.getFailureCause(message));
            }
            String consumerIP = ConsumerIPHelper.getConsumerIP(message);
            httpInfo.setConsumerIP(consumerIP);
        }
        // 无论请求还是响应都设置serviceKey 2023-06-16 xingzilong
        String serviceKey = message.getExchange().getEndpoint().getEndpointInfo().getAddress();
        if (null != serviceKey) {
            httpInfo.setServiceKey(serviceKey);
        }

        return event;
    }

    private String getRestOperationName(Message message) {
        boolean isRequestor = MessageUtils.isRequestor(message);
        boolean isOutbound = MessageUtils.isOutbound(message);

        Message effectiveMessage = message;

        if (isRequestor) {
            if (!isOutbound) {
                effectiveMessage = message.getExchange().getOutMessage();
            }
        } else {
            if (isOutbound) {
                effectiveMessage = message.getExchange().getInMessage();
            }
        }

        return buildRestOperationName(effectiveMessage);
    }

    private static String buildRestOperationName(Message message) {
        if (message.containsKey(Message.HTTP_REQUEST_METHOD)) {
            String httpMethod = message.get(Message.HTTP_REQUEST_METHOD).toString();

            String path = "";
            if (message.containsKey(Message.REQUEST_URI)) {
                String requestUri = message.get(Message.REQUEST_URI).toString();
                int baseUriLength = (message.containsKey(Message.BASE_PATH))
                        ? message.get(Message.BASE_PATH).toString().length() : 0;
                path = requestUri.substring(baseUriLength);
                if (path.isEmpty()) {
                    path = "/";
                }
            }

            return new StringBuffer().append(httpMethod).append('[').append(path).append(']').toString();
        }
        return "";
    }

    /**
     * Get MessageId string.
     * if enforceMessageIDTransfer=true or WS-Addressing enabled explicitly (i.e with <wsa:addressing/> feature),
     * then MessageId is not null and conform with the definition in the WS-Addressing Spec;
     * if enforceMessageIDTransfer=false and WS-Addressing doesn't enable,
     * then MessageId is null.
     * @param message the message
     * @return the message id
     */
    private String getMessageId(Message message) {
        String messageId = null;

        AddressingProperties addrProp = ContextUtils.retrieveMAPs(message, false,
                MessageUtils.isOutbound(message));
        if (addrProp != null && addrProp.getMessageID() != null) {
            messageId = addrProp.getMessageID().getValue();
        }

        return messageId;
    }

    /**
     * Gets the event type from message.
     *
     * @param message the message
     * @return the event type
     */
    private EventTypeEnum getEventType(Message message) {
        boolean isRequestor = MessageUtils.isRequestor(message);
        boolean isFault = MessageUtils.isFault(message);
        boolean isOutbound = MessageUtils.isOutbound(message);

        //Needed because if it is rest request and method does not exists had better to return Fault
        if(!isFault && isRestMessage(message)) {
            isFault = (message.getExchange().get("org.apache.cxf.resource.operation.name") == null);
            if (!isFault) {
                Integer responseCode = (Integer) message.get(Message.RESPONSE_CODE);
                if (null != responseCode) {
                    isFault = (responseCode >= 400);
                }
            }
        }
        if (isOutbound) {
            if (isFault) {
                return EventTypeEnum.FAULT_OUT;
            } else {
                return isRequestor ? EventTypeEnum.REQ_OUT : EventTypeEnum.RESP_OUT;
            }
        } else {
            if (isFault) {
                return EventTypeEnum.FAULT_IN;
            } else {
                return isRequestor ? EventTypeEnum.RESP_IN : EventTypeEnum.REQ_IN;
            }
        }
    }

    private String getOperationName(Message message) {
        String operationName = null;
        BindingOperationInfo boi = null;

        boi = message.getExchange().getBindingOperationInfo();
        if (null == boi) {
            // get BindingOperationInfo from message content
            boi = getOperationFromContent(message);
        }

        // if BindingOperationInfo is still null, try to get it from Request
        // message content
        if (null == boi) {
            Message inMsg = message.getExchange().getInMessage();
            if (null != inMsg) {
                Message reqMsg = inMsg.getExchange().getInMessage();
                if (null != reqMsg) {
                    boi = getOperationFromContent(reqMsg);
                }
            }
        }

        if (null != boi) {
            operationName = boi.getName().toString();
        }

        return operationName;
    }

    private BindingOperationInfo getOperationFromContent(Message message) {
        BindingOperationInfo boi = null;
        XMLStreamReader xmlReader = message.getContent(XMLStreamReader.class);
        if (null != xmlReader) {
            QName qName = xmlReader.getName();
            boi = ServiceModelUtil.getOperation(message.getExchange(), qName);
        }
        return boi;
    }

    /**
     * Gets the message payload.
     *
     * @param message the message
     * @return the payload
     */
    protected String getPayload(Message message) {
        try {
            String encoding = (String) message.get(Message.ENCODING);
            if (encoding == null) {
                encoding = "UTF-8";
            }
            CachedOutputStream cos = message.getContent(CachedOutputStream.class);
            if (cos == null) {
                LOG.warning("Could not find CachedOutputStream in message."
                        + " Continuing without message content");
                return "";
            }
            return new String(cos.getBytes(), encoding);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Gets the max message content length.
     *
     * @return the max content length
     */
    public int getMaxContentLength() {
        return maxContentLength;
    }

    /**
     * Sets the max message content length.
     *
     * @param maxContentLength
     *            the new max content length
     */
    public void setMaxContentLength(int maxContentLength) {
        this.maxContentLength = maxContentLength;
    }

    /**
     * Handle content length.
     *
     * @param event
     *            the event
     */
    private void handleContentLength(Event event) {
        if (event.getContent() == null) {
            return;
        }

        if (maxContentLength == -1 || event.getContent().length() <= maxContentLength) {
            return;
        }

        if (maxContentLength < CUT_START_TAG.length() + CUT_END_TAG.length()) {
            event.setContent("");
            event.setContentCut(true);
            return;
        }

        int contentLength = maxContentLength - CUT_START_TAG.length() - CUT_END_TAG.length();
        event.setContent(CUT_START_TAG + event.getContent().substring(0, contentLength) + CUT_END_TAG);
        event.setContentCut(true);
    }

    /**
     * check if a Message is a Rest Message
     *
     * @param message
     * @return
     */
    static boolean isRestMessage(Message message) {
        return !(message.getExchange().getBinding() instanceof SoapBinding);
    }
}
