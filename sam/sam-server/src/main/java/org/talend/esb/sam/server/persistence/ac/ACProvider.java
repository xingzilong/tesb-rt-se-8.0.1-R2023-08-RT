package org.talend.esb.sam.server.persistence.ac;

import java.util.List;

/**
 * 访问控制， Restful管理控制接口数据库服务提供者接口， 类似DAO
 *
 */
public interface ACProvider {

    /**
     * 向 access_control_ip 表中新增一条数据项
     *
     * @param acIP
     * @return
     */
    int saveACIP(ACIP acIP);

    /**
     * 根据 service_key 删除 access_control_ip 表中一条数据项
     *
     * @param serviceKey
     * @return
     */
    int removeACIPByServiceKey(String serviceKey);

    /**
     * 更新 access_control_ip 表中一条数据项
     *
     * @param acIP
     * @return
     */
    int updateACIP(ACIP acIP);

    /**
     * 根据 service_key 获取一条记录信息
     *
     * @return
     */
    ACIP getACIPByServiceKey(String serviceKey);

    /**
     * 获取所有的 access_control_ip 表中的数据项
     *
     * @return
     */
    List<ACIP> listAllACIP();

    /**
     * 更新 access_control_ip 表中一条数据项的 status 字段值
     *
     * @param acIP
     * @return
     */
    int updateACIPStatus(ACIP acIP);


    /**
     * 向 access_control_flow 表中新增一条数据项
     *
     * @param acFlow
     * @return
     */
    int saveACFlow(ACFlow acFlow);

    /**
     * 根据 service_key 删除 access_control_flow 表中一条数据项
     *
     * @param serviceKey
     * @return
     */
    int removeACFlowByServiceKey(String serviceKey);

    /**
     * 更新 access_control_flow 表中一条数据项
     *
     * @param acFlow
     * @return
     */
    int updateACFlow(ACFlow acFlow);

    /**
     * 根据 service_key 获取一条记录信息
     *
     * @return
     */
    ACFlow getACFlowByServiceKey(String serviceKey);

    /**
     * 获取所有的 access_control_flow 表中的数据项
     *
     * @return
     */
    List<ACFlow> listAllACFlow();

    /**
     * 更新 access_control_flow 表中一条数据项的 status 字段值
     *
     * @param acFlow
     * @return
     */
    int updateACFlowStatus(ACFlow acFlow);


    /**
     * 向 access_control_frequency 表中新增一条数据项
     *
     * @param acFrequency
     * @return
     */
    int saveACFrequency(ACFrequency acFrequency);

    /**
     * 根据 service_key 删除 access_control_frequency 表中一条数据项
     *
     * @param serviceKey
     * @return
     */
    int removeACFrequencyByServiceKey(String serviceKey);

    /**
     * 更新 access_control_frequency 表中一条数据项
     *
     * @param acFrequency
     * @return
     */
    int updateACFrequency(ACFrequency acFrequency);

    /**
     * 根据 service_key 获取一条记录信息
     *
     * @return
     */
    ACFrequency getACFrequencyByServiceKey(String serviceKey);

    /**
     * 获取所有的 access_control_frequency 表中的数据项
     *
     * @return
     */
    List<ACFrequency> listAllACFrequency();

    /**
     * 更新 access_control_frequency 表中一条数据项的 status 字段值
     *
     * @param acFrequency
     * @return
     */
    int updateACFrequencyStatus(ACFrequency acFrequency);
}
