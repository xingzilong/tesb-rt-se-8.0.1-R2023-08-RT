package org.talend.esb.sam.agent.ac;

import java.util.List;

/**
 * 记录表的接口
 */
public interface RecordTable {

    /**
     * 从文件中恢复记录表信息
     *
     * @param filePath 文件路径信息
     */
    public void fileToObject(String filePath);

    /**
     * 记录表信息写入文件
     *
     * @param filePath 文件路径信息
     */
    public void objectToFile(String filePath);

    /**
     * 当服务的控制规则发生变更时，删除记录表有已有的信息，重新开始记录
     *
     * @param serviceIdentificationList 要清除服务记录的服务集合
     */
    public void clearRecords(List<String> serviceIdentificationList);

    /**
     * 当服务的控制规则发生变更时，删除记录表有已有的信息，重新开始记录
     *
     * @param serviceIdentification 要清除服务记录的服务标识
     */
    public void clearRecord(String serviceIdentification);

}
