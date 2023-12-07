package org.talend.esb.sam.service.rest.ac;

import org.talend.esb.sam.agent.ac.RecordTable;
import org.talend.esb.sam.agent.ac.flow.FlowRecord;
import org.talend.esb.sam.agent.ac.flow.FlowRecordTable;
import org.talend.esb.sam.agent.ac.flow.FlowRule;
import org.talend.esb.sam.agent.ac.flow.FlowRuleTable;
import org.talend.esb.sam.agent.ac.frequency.FrequencyRecord;
import org.talend.esb.sam.agent.ac.frequency.FrequencyRecordTable;
import org.talend.esb.sam.agent.ac.frequency.FrequencyRule;
import org.talend.esb.sam.agent.ac.frequency.FrequencyRuleTable;
import org.talend.esb.sam.agent.ac.ip.IPRule;
import org.talend.esb.sam.agent.ac.ip.IPRuleTable;
import org.talend.esb.sam.server.persistence.ac.ACFlow;
import org.talend.esb.sam.server.persistence.ac.ACFrequency;
import org.talend.esb.sam.server.persistence.ac.ACIP;
import org.talend.esb.sam.server.persistence.ac.ACProvider;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import javax.ws.rs.core.Response;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ACRestServiceImpl implements ACRestService {

    ACProvider acProvider;

    private Gson excludeFieldsGson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

    private Gson gson = new Gson();

    // 定义 ACIP 类型
    private Type acIPType = new TypeToken<ACIP>() {
    }.getType();

    // 定义 ACFlow 类型
    private Type acFlowType = new TypeToken<ACFlow>() {
    }.getType();

    // 定义 ACFrequency 类型
    private Type acFrequencyType = new TypeToken<ACFrequency>() {
    }.getType();

    public void setAcProvider(ACProvider acProvider) {
        this.acProvider = acProvider;
    }

    @Override
    public Response addACIP(String requestBody) {
        ACIP acIP = excludeFieldsGson.fromJson(requestBody, acIPType);
        // 设置主键
        acIP.setId(UUID.randomUUID().toString());
        int rs = acProvider.saveACIP(acIP);
        IPRule ipRule = new IPRule();
        ipRule.setType(acIP.getType());
//        ipRule.setBlackList(acIP.getBlackList());
//        ipRule.setWhiteList(acIP.getWhiteList());
        ipRule.setBlackList(excludeFieldsGson.fromJson(acIP.getBlackList(), new TypeToken<HashMap<String, String>>() {
        }.getType()));
        ipRule.setWhiteList(excludeFieldsGson.fromJson(acIP.getWhiteList(), new TypeToken<HashMap<String, String>>() {
        }.getType()));
        ipRule.setStatus(acIP.getStatus());
        // 将该服务的规则添加进规则表
        IPRuleTable.getInstance().setRule(acIP.getServiceKey(), ipRule);
        ResponseResult<Integer> responseResult = new ResponseResult<>(200, "添加成功", rs);
        return Response.ok(gson.toJson(responseResult, new TypeToken<ResponseResult<Integer>>() {
        }.getType())).build();
    }

    @Override
    public Response removeACIP(String requestBody) {
        ACIP acIP = excludeFieldsGson.fromJson(requestBody, acIPType);
        int rs = acProvider.removeACIPByServiceKey(acIP.getServiceKey());
        // 清空规则表中该服务对应的规则设置
        IPRuleTable.getInstance().removeRule(acIP.getServiceKey());
        ResponseResult<Integer> responseResult = new ResponseResult<>(200, "删除成功", rs);
        return Response.ok(gson.toJson(responseResult, new TypeToken<ResponseResult<Integer>>() {
        }.getType())).build();
    }

    @Override
    public Response updateACIP(String requestBody) {
        ACIP acIP = excludeFieldsGson.fromJson(requestBody, acIPType);
        int rs = acProvider.updateACIP(acIP);
        IPRule ipRule = new IPRule();
        ipRule.setType(acIP.getType());
//        ipRule.setBlackList(acIP.getBlackList());
//        ipRule.setWhiteList(acIP.getWhiteList());
        ipRule.setBlackList(excludeFieldsGson.fromJson(acIP.getBlackList(), new TypeToken<HashMap<String, String>>() {
        }.getType()));
        ipRule.setWhiteList(excludeFieldsGson.fromJson(acIP.getWhiteList(), new TypeToken<HashMap<String, String>>() {
        }.getType()));
        ipRule.setStatus(acIP.getStatus());
        // 只有是使用状态时去更新本地规则表，否则只更新数据库
        if (acIP.getStatus().equals("1")) {
            // 将该服务的规则添加进规则表，即更新规则或重置规则
            IPRuleTable.getInstance().setRule(acIP.getServiceKey(), ipRule);
        }
        ResponseResult<Integer> responseResult = new ResponseResult<>(200, "更新成功", rs);
        return Response.ok(gson.toJson(responseResult, new TypeToken<ResponseResult<Integer>>() {
        }.getType())).build();
    }

    @Override
    public Response listAllACIP() {
        List<ACIP> acIPList = acProvider.listAllACIP();
        // 定义 List 类型
        Type acIPListType = new TypeToken<List<ACIP>>() {
        }.getType();
        ResponseResult<List<ACIP>> responseResult = new ResponseResult<>(200, "查询成功", acIPList);
        return Response.ok(gson.toJson(responseResult, new TypeToken<ResponseResult<List<ACIP>>>() {
        }.getType())).build();
    }

    @Override
    public Response getACIP(String serviceKey) {
        ACIP acIP = acProvider.getACIPByServiceKey(serviceKey);
        ResponseResult<ACIP> responseResult = new ResponseResult<>(200, "查询成功", acIP);
        return Response.ok(gson.toJson(responseResult, new TypeToken<ResponseResult<ACIP>>() {
        }.getType())).build();
    }

    @Override
    public Response enableACIP(String requestBody) {
        ACIP acIP = excludeFieldsGson.fromJson(requestBody, acIPType);
        acIP.setStatus("1");
        int rs = acProvider.updateACIPStatus(acIP);
        ACIP newACIP = acProvider.getACIPByServiceKey(acIP.getServiceKey());
        IPRule ipRule = new IPRule();
        ipRule.setType(acIP.getType());
//        ipRule.setBlackList(newACIP.getBlackList());
//        ipRule.setWhiteList(newACIP.getWhiteList());
        ipRule.setBlackList(excludeFieldsGson.fromJson(acIP.getBlackList(), new TypeToken<HashMap<String, String>>() {
        }.getType()));
        ipRule.setWhiteList(excludeFieldsGson.fromJson(acIP.getWhiteList(), new TypeToken<HashMap<String, String>>() {
        }.getType()));
        ipRule.setStatus(acIP.getStatus());
        // 将该服务的规则添加进规则表，即更新规则或重置规则
        IPRuleTable.getInstance().setRule(newACIP.getServiceKey(), ipRule);
        ResponseResult<Integer> responseResult = new ResponseResult<>(200, "启用成功", rs);
        return Response.ok(gson.toJson(responseResult, new TypeToken<ResponseResult<Integer>>() {
        }.getType())).build();
    }

    @Override
    public Response disabledACIP(String requestBody) {
        ACIP acIP = excludeFieldsGson.fromJson(requestBody, acIPType);
        acIP.setStatus("0");
        int rs = acProvider.updateACIPStatus(acIP);
        IPRuleTable.getInstance().removeRule(acIP.getServiceKey());
        ResponseResult<Integer> responseResult = new ResponseResult<>(200, "禁用成功", rs);
        return Response.ok(gson.toJson(responseResult, new TypeToken<ResponseResult<Integer>>() {
        }.getType())).build();
    }

    @Override
    public Response addACFlow(String requestBody) {
        ACFlow acFlow = excludeFieldsGson.fromJson(requestBody, acFlowType);
        // 设置主键
        acFlow.setId(UUID.randomUUID().toString());
        int rs = acProvider.saveACFlow(acFlow);
        FlowRule flowRule = new FlowRule();
        flowRule.setType(acFlow.getType());
        flowRule.setTimeInterval(acFlow.getTimeInterval());
        flowRule.setIntervalThreshold(acFlow.getIntervalThreshold());
        flowRule.setSingleThreshold(acFlow.getSingleThreshold());
        flowRule.setStatus(acFlow.getStatus());
        // 将该服务的规则添加进规则表
        FlowRuleTable.getInstance().setRule(acFlow.getServiceKey(), flowRule);
        ResponseResult<Integer> responseResult = new ResponseResult<>(200, "添加成功", rs);
        return Response.ok(gson.toJson(responseResult, new TypeToken<ResponseResult<Integer>>() {
        }.getType())).build();
    }

    @Override
    public Response removeACFlow(String requestBody) {
        ACFlow acFlow = excludeFieldsGson.fromJson(requestBody, acFlowType);
        int rs = acProvider.removeACFlowByServiceKey(acFlow.getServiceKey());
        // 清空规则表中该服务对应的规则设置
        FlowRuleTable.getInstance().removeRule(acFlow.getServiceKey());
        // 服务的控制规则被删除，清除记录表中有关此服务的所有记录
        FlowRecordTable.getInstance().clearRecord(acFlow.getServiceKey());
        ResponseResult<Integer> responseResult = new ResponseResult<>(200, "删除成功", rs);
        return Response.ok(gson.toJson(responseResult, new TypeToken<ResponseResult<Integer>>() {
        }.getType())).build();
    }

    @Override
    public Response updateACFlow(String requestBody) {
        ACFlow acFlow = excludeFieldsGson.fromJson(requestBody, acFlowType);
        int rs = acProvider.updateACFlow(acFlow);
        FlowRule flowRule = new FlowRule();
        flowRule.setType(acFlow.getType());
        flowRule.setTimeInterval(acFlow.getTimeInterval());
        flowRule.setIntervalThreshold(acFlow.getIntervalThreshold());
        flowRule.setSingleThreshold(acFlow.getSingleThreshold());
        flowRule.setStatus(acFlow.getStatus());
        // 只有是使用状态时去更新本地规则表，否则只更新数据库
        if (acFlow.getStatus().equals("1")) {
            // 将该服务的规则添加进规则表，即更新规则或重置规则
            FlowRuleTable.getInstance().setRule(acFlow.getServiceKey(), flowRule);
            // 服务的控制规则发生变更，清除记录表中有关此服务的所有记录
            FlowRecordTable.getInstance().clearRecord(acFlow.getServiceKey());
        }
        ResponseResult<Integer> responseResult = new ResponseResult<>(200, "更新成功", rs);
        return Response.ok(gson.toJson(responseResult, new TypeToken<ResponseResult<Integer>>() {
        }.getType())).build();
    }

    @Override
    public Response listAllACFlow() {
        List<ACFlow> acFlowList = acProvider.listAllACFlow();
        // 定义 List 类型
        Type acFlowListType = new TypeToken<List<ACFlow>>() {
        }.getType();
        ResponseResult<List<ACFlow>> responseResult = new ResponseResult<>(200, "查询成功", acFlowList);
        return Response.ok(gson.toJson(responseResult, new TypeToken<ResponseResult<List<ACFlow>>>() {
        }.getType())).build();
    }

    @Override
    public Response getACFlow(String serviceKey) {
        ACFlow acFlow = acProvider.getACFlowByServiceKey(serviceKey);
        ResponseResult<ACFlow> responseResult = new ResponseResult<>(200, "查询成功", acFlow);
        return Response.ok(gson.toJson(responseResult, new TypeToken<ResponseResult<ACFlow>>() {
        }.getType())).build();
    }

    @Override
    public Response enableACFlow(String requestBody) {
        ACFlow acFlow = excludeFieldsGson.fromJson(requestBody, acFlowType);
        acFlow.setStatus("1");
        int rs = acProvider.updateACFlowStatus(acFlow);
        ACFlow newACFlow = acProvider.getACFlowByServiceKey(acFlow.getServiceKey());
        FlowRule flowRule = new FlowRule();
        flowRule.setType(newACFlow.getType());
        flowRule.setTimeInterval(newACFlow.getTimeInterval());
        flowRule.setIntervalThreshold(newACFlow.getIntervalThreshold());
        flowRule.setSingleThreshold(newACFlow.getSingleThreshold());
        flowRule.setStatus(newACFlow.getStatus());
        // 将该服务的规则添加进规则表
        FlowRuleTable.getInstance().setRule(acFlow.getServiceKey(), flowRule);
        ResponseResult<Integer> responseResult = new ResponseResult<>(200, "启用成功", rs);
        return Response.ok(gson.toJson(responseResult, new TypeToken<ResponseResult<Integer>>() {
        }.getType())).build();
    }

    @Override
    public Response disabledACFlow(String requestBody) {
        ACFlow acFlow = excludeFieldsGson.fromJson(requestBody, acFlowType);
        acFlow.setStatus("0");
        int rs = acProvider.updateACFlowStatus(acFlow);
        FlowRuleTable.getInstance().removeRule(acFlow.getServiceKey());
        FlowRecordTable.getInstance().clearRecord(acFlow.getServiceKey());
        ResponseResult<Integer> responseResult = new ResponseResult<>(200, "禁用成功", rs);
        return Response.ok(gson.toJson(responseResult, new TypeToken<ResponseResult<Integer>>() {
        }.getType())).build();
    }

    @Override
    public Response addACFrequency(String requestBody) {
        ACFrequency acFrequency = excludeFieldsGson.fromJson(requestBody, acFrequencyType);
        // 设置主键
        acFrequency.setId(UUID.randomUUID().toString());
        int rs = acProvider.saveACFrequency(acFrequency);
        FrequencyRule frequencyRule = new FrequencyRule();
        frequencyRule.setType(acFrequency.getType());
        frequencyRule.setTimeInterval(acFrequency.getTimeInterval());
        frequencyRule.setThreshold(acFrequency.getThreshold());
        frequencyRule.setStatus(acFrequency.getStatus());
        // 将该服务的规则添加进规则表
        FrequencyRuleTable.getInstance().setRule(acFrequency.getServiceKey(), frequencyRule);
        ResponseResult<Integer> responseResult = new ResponseResult<>(200, "添加成功", rs);
        return Response.ok(gson.toJson(responseResult, new TypeToken<ResponseResult<Integer>>() {
        }.getType())).build();
    }

    @Override
    public Response removeACFrequency(String requestBody) {
        ACFrequency acFrequency = excludeFieldsGson.fromJson(requestBody, acFrequencyType);
        int rs = acProvider.removeACFrequencyByServiceKey(acFrequency.getServiceKey());
        // 清空规则表中该服务对应的规则设置
        FlowRuleTable.getInstance().removeRule(acFrequency.getServiceKey());
        // 服务的控制规则被删除，清除记录表中有关此服务的所有记录
        FrequencyRecordTable.getInstance().clearRecord(acFrequency.getServiceKey());
        ResponseResult<Integer> responseResult = new ResponseResult<>(200, "删除成功", rs);
        return Response.ok(gson.toJson(responseResult, new TypeToken<ResponseResult<Integer>>() {
        }.getType())).build();
    }

    @Override
    public Response updateACFrequency(String requestBody) {
        ACFrequency acFrequency = excludeFieldsGson.fromJson(requestBody, acFrequencyType);
        int rs = acProvider.updateACFrequency(acFrequency);
        FrequencyRule frequencyRule = new FrequencyRule();
        frequencyRule.setType(acFrequency.getType());
        frequencyRule.setTimeInterval(acFrequency.getTimeInterval());
        frequencyRule.setThreshold(acFrequency.getThreshold());
        frequencyRule.setStatus(acFrequency.getStatus());
        // 只有是使用状态时去更新本地规则表，否则只更新数据库
        if (acFrequency.getStatus().equals("1")) {
            // 将该服务的规则添加进规则表，即更新规则或重置规则
            FrequencyRuleTable.getInstance().setRule(acFrequency.getServiceKey(), frequencyRule);
            // 服务的控制规则发生变更，清除记录表中有关此服务的所有记录
            FrequencyRecordTable.getInstance().clearRecord(acFrequency.getServiceKey());
        }
        ResponseResult<Integer> responseResult = new ResponseResult<>(200, "更新成功", rs);
        return Response.ok(gson.toJson(responseResult, new TypeToken<ResponseResult<Integer>>() {
        }.getType())).build();
    }

    @Override
    public Response listAllACFrequency() {
        List<ACFrequency> acFrequencyList = acProvider.listAllACFrequency();
        // 定义 List 类型
        Type acFrequencyListType = new TypeToken<List<ACFrequency>>() {
        }.getType();
        ResponseResult<List<ACFrequency>> responseResult = new ResponseResult<>(200, "查询成功", acFrequencyList);
        return Response.ok(gson.toJson(responseResult, new TypeToken<ResponseResult<List<ACFrequency>>>() {
        }.getType())).build();
    }

    @Override
    public Response getACFrequency(String serviceKey) {
        ACFrequency acFrequency = acProvider.getACFrequencyByServiceKey(serviceKey);
        ResponseResult<ACFrequency> responseResult = new ResponseResult<>(200, "查询成功", acFrequency);
        return Response.ok(gson.toJson(responseResult, new TypeToken<ResponseResult<ACFrequency>>() {
        }.getType())).build();
    }

    @Override
    public Response enableACFrequency(String requestBody) {
        ACFrequency acFrequency = excludeFieldsGson.fromJson(requestBody, acFrequencyType);
        acFrequency.setStatus("1");
        int rs = acProvider.updateACFrequencyStatus(acFrequency);
        ACFrequency newACFrequency = acProvider.getACFrequencyByServiceKey(acFrequency.getServiceKey());
        FrequencyRule frequencyRule = new FrequencyRule();
        frequencyRule.setType(newACFrequency.getType());
        frequencyRule.setTimeInterval(newACFrequency.getTimeInterval());
        frequencyRule.setThreshold(newACFrequency.getThreshold());
        frequencyRule.setStatus(newACFrequency.getStatus());
        // 将该服务的规则添加进规则表
        FrequencyRuleTable.getInstance().setRule(acFrequency.getServiceKey(), frequencyRule);
        ResponseResult<Integer> responseResult = new ResponseResult<>(200, "启用成功", rs);
        return Response.ok(gson.toJson(responseResult, new TypeToken<ResponseResult<Integer>>() {
        }.getType())).build();
    }

    @Override
    public Response disabledACFrequency(String requestBody) {
        ACFrequency acFrequency = excludeFieldsGson.fromJson(requestBody, acFrequencyType);
        acFrequency.setStatus("0");
        int rs = acProvider.updateACFrequencyStatus(acFrequency);
        FrequencyRuleTable.getInstance().removeRule(acFrequency.getServiceKey());
        FrequencyRecordTable.getInstance().clearRecord(acFrequency.getServiceKey());
        ResponseResult<Integer> responseResult = new ResponseResult<>(200, "禁用成功", rs);
        return Response.ok(gson.toJson(responseResult, new TypeToken<ResponseResult<Integer>>() {
        }.getType())).build();
    }

    @Override
    public Response listAllRecords() {
        HashMap<String, RecordTable> rsHashMap = new HashMap<>();
        FlowRecordTable<String, Map<String, FlowRecord>> flowRecordTable = FlowRecordTable.getInstance();
        FrequencyRecordTable<String, Map<String, FrequencyRecord>> frequencyRecordTable = FrequencyRecordTable.getInstance();
        rsHashMap.put("FlowRecordTable", flowRecordTable);
        rsHashMap.put("FrequencyRecordTable", frequencyRecordTable);
        ResponseResult<HashMap<String, RecordTable>> responseResult = new ResponseResult<>(200, "查询成功", rsHashMap);
        return Response.ok(gson.toJson(responseResult, new TypeToken<ResponseResult<HashMap<String, RecordTable>>>() {
        }.getType())).build();

    }

    @Override
    public Response listAllRules() {
        HashMap<String, Object> rsHashMap = new HashMap<>();
        FlowRuleTable<String, FlowRule> flowRuleTable = FlowRuleTable.getInstance();
        FrequencyRuleTable<String, FrequencyRule> frequencyRuleTable = FrequencyRuleTable.getInstance();
        IPRuleTable<String, IPRule> ipRuleTable = IPRuleTable.getInstance();
        rsHashMap.put("FlowRuleTable", flowRuleTable);
        rsHashMap.put("FrequencyRuleTable", frequencyRuleTable);
        rsHashMap.put("IPRuleTable", ipRuleTable);
        ResponseResult<HashMap<String, Object>> responseResult = new ResponseResult<>(200, "查询成功", rsHashMap);
        return Response.ok(gson.toJson(responseResult, new TypeToken<ResponseResult<HashMap<String, Object>>>() {
        }.getType())).build();
    }
}