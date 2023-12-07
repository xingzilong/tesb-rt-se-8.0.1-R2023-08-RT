package org.talend.esb.sam.agent.ac.frequency;

import org.talend.esb.sam.agent.ac.RecordTable;
import org.talend.esb.sam.agent.ac.util.Constants;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

/**
 * 访问频次记录表
 * 频次控制记录表结构Map<String,Map<String,FrequencyRecord>>
 * key为服务标识,value(Map<String,FrequencyRecord>)为相应服务对应的频次控制记录信息， key(String)为消费方IP，FrequencyRecord为频次记录 ，
 * {@link FrequencyRecord}类型的对象。
 */
public class FrequencyRecordTable<K, V> extends ConcurrentHashMap<K, V> implements RecordTable, Serializable {

    private static final Logger LOG = Logger.getLogger(FrequencyRecordTable.class.getName());

    private static final long serialVersionUID = 1L;

    private static FrequencyRecordTable<String, Map<String, FrequencyRecord>> instance = new FrequencyRecordTable<String, Map<String, FrequencyRecord>>();

    private FrequencyRecordTable() {

    }

    public static FrequencyRecordTable<String, Map<String, FrequencyRecord>> getInstance() {
        return instance;
    }

    /**
     * 从记录表中获取访问记录信息 GLOBAL模式
     *
     * @param currentTime           调用的当前时间
     * @param serviceIdentification 服务标识
     * @return frequencyRule  记录信息
     */
    public FrequencyRecord getFrequencyRecord(long currentTime, String serviceIdentification) {
        Map<String, FrequencyRecord> consumerFrequencyRecord = instance.get(serviceIdentification);
        FrequencyRecord frequencyRecord = null;
        // 若记录表中不存在该服务的访问记录，即首次请求该服务，则创建相应的记录
        if (consumerFrequencyRecord == null) {
            consumerFrequencyRecord = new HashMap<String, FrequencyRecord>();
            frequencyRecord = putFirstFrequencyRecord(currentTime, serviceIdentification, consumerFrequencyRecord);
        } else {
            frequencyRecord = consumerFrequencyRecord.get(Constants.FTEQUENCY_TYPE_GLOBAL);
        }
        return frequencyRecord;
    }

    /**
     * 服务的首次调用，向记录表中写入记录数据 GLOBAL模式
     *
     * @param currentTime             调用的当前时间
     * @param serviceIdentification   服务标识
     * @param consumerFrequencyRecord 服务调用记录 一个map，记录了该服务的所有消费者的调用记录
     * @return FrequencyRecord  记录信息
     */
    public FrequencyRecord putFirstFrequencyRecord(long currentTime, String serviceIdentification, Map<String, FrequencyRecord> consumerFrequencyRecord) {
        FrequencyRecord frequencyRecord = new FrequencyRecord(currentTime, 0L);
        consumerFrequencyRecord.put(Constants.FTEQUENCY_TYPE_GLOBAL, frequencyRecord);
        instance.put(serviceIdentification, consumerFrequencyRecord);
        return frequencyRecord;

    }

    /**
     * 从记录表中获取访问记录信息 CONSUMER模式
     *
     * @param currentTime            调用的当前时间
     * @param serviceIdentification  服务标识
     * @param consumerIdentification 消费方标识
     * @return frequencyRule  记录信息
     */
    public FrequencyRecord getFrequencyRecord(long currentTime, String serviceIdentification, String consumerIdentification) {
        Map<String, FrequencyRecord> consumerFrequencyRecord = instance.get(serviceIdentification);
        FrequencyRecord frequencyRecord = null;
        // 若记录表中不存在该服务的访问记录，即首次请求该服务，则创建相应的记录
        if (consumerFrequencyRecord == null) {
            consumerFrequencyRecord = new HashMap<String, FrequencyRecord>();
            frequencyRecord = putFirstFrequencyRecord(currentTime, consumerIdentification, serviceIdentification, consumerFrequencyRecord);
        } else {
            frequencyRecord = consumerFrequencyRecord.get(consumerIdentification);
            if (frequencyRecord == null) {
                // 若记录表中存服务的记录信息，但无该消费方相应的记录信息，即该消费方首次请求该服务，则创建相应的记录
                frequencyRecord = putFirstFrequencyRecord(currentTime, consumerIdentification, serviceIdentification, consumerFrequencyRecord);
            }
        }
        return frequencyRecord;
    }

    /**
     * 服务的首次调用，向记录表中写入记录数据 CONSUMER模式
     *
     * @param currentTime             调用的当前时间
     * @param consumerIdentification  消费方标识
     * @param serviceIdentification   服务标识
     * @param consumerFrequencyRecord 服务调用记录 一个map，记录了该服务的所有消费者的调用记录
     * @return FrequencyRecord  记录信息
     */
    public FrequencyRecord putFirstFrequencyRecord(long currentTime, String consumerIdentification, String serviceIdentification, Map<String, FrequencyRecord> consumerFrequencyRecord) {
        FrequencyRecord frequencyRecord = new FrequencyRecord(currentTime, 0L);
        consumerFrequencyRecord.put(consumerIdentification, frequencyRecord);
        instance.put(serviceIdentification, consumerFrequencyRecord);
        return frequencyRecord;

    }

    /**
     * 进入新的访问频度计算时间区间，对初次访问时间(firstTimestamp)和访问次数计数器(count)进行重置
     *
     * @param currentTime     调用的当前时间
     * @param frequencyRecord 调用记录
     */
    public void reset(long currentTime, FrequencyRecord frequencyRecord) {
        frequencyRecord.setFirstTimestamp(currentTime);
        frequencyRecord.setCount(0L);
    }

    @Override
    public void fileToObject(String filePath) {
        filePath = filePath + File.separator + getClass().getName();
        FileInputStream fileInputStream = null;
        ObjectInputStream inputStream = null;
        try {
            fileInputStream = new FileInputStream(filePath);
            inputStream = new ObjectInputStream(fileInputStream);
            instance.putAll((FrequencyRecordTable) inputStream.readObject());
        } catch (FileNotFoundException e) {
            LOG.severe("FrequencyRecordTable 反序列化时，[" + filePath + "]文件路径不存在。" + e.getMessage());
            // e.printStackTrace();
        } catch (ClassNotFoundException e) {
            LOG.severe("FrequencyRecordTable 反序列化时，未找到类型[" + getClass().getName() + "]。" + e.getMessage());
            // e.printStackTrace();
        } catch (IOException e) {
            LOG.severe("FrequencyRecordTable 反序列化时，出现异常。" + e.getMessage());
            // e.printStackTrace();
        } finally {
            // 关闭输入流
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    LOG.severe("FrequencyRecordTable 反序列化时，出现异常。" + e.getMessage());
                    // e.printStackTrace();
                }
            }
            // 关闭文件输入流
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    LOG.severe("FrequencyRecordTable 反序列化时，出现异常。" + e.getMessage());
                    // e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void objectToFile(String filePath) {
        filePath = filePath + File.separator + getClass().getName();
        FileOutputStream fileOutputStream = null;
        ObjectOutputStream outputStream = null;
        try {
            fileOutputStream = new FileOutputStream(filePath);
            outputStream = new ObjectOutputStream(fileOutputStream);
            outputStream.writeObject(instance);
        } catch (FileNotFoundException e) {
            LOG.severe("FrequencyRecordTable 序列化时，[" + filePath + "]文件路径不存在。" + e.getMessage());
            // e.printStackTrace();
        } catch (IOException e) {
            LOG.severe("FrequencyRecordTable 序列化时，出现异常。" + e.getMessage());
            // e.printStackTrace();
        } finally {
            // 关闭输出流
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    LOG.severe("FrequencyRecordTable 序列化时，出现异常。" + e.getMessage());
                    // e.printStackTrace();
                }
            }
            // 关闭文件输出流
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    LOG.severe("FrequencyRecordTable 序列化时，出现异常。" + e.getMessage());
                    // e.printStackTrace();
                }
            }
        }
    }

    /**
     * 当服务的访问频次规则发生变更时，删除记录表有已有的信息，重新开始记录
     *
     * @param serviceIdentificationList 要清除服务记录的服务集合
     */
    public void clearRecords(List<String> serviceIdentificationList) {
        for (String serviceAddress : serviceIdentificationList) {
            if (instance.get(serviceAddress) != null) {
                instance.remove(serviceAddress);
            }
        }
    }

    /**
     * 当服务的访问频次规则发生变更时，删除记录表有已有的信息，重新开始记录
     *
     * @param serviceIdentification 要清除服务记录的服务标识
     */
    public void clearRecord(String serviceIdentification) {
        if (instance.get(serviceIdentification) != null) {
            instance.remove(serviceIdentification);
        }

    }

}
