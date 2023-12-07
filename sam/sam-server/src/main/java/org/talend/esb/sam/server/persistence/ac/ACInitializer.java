package org.talend.esb.sam.server.persistence.ac;

import org.talend.esb.sam.agent.ac.flow.FlowRecordTable;
import org.talend.esb.sam.agent.ac.flow.FlowRule;
import org.talend.esb.sam.agent.ac.flow.FlowRuleTable;
import org.talend.esb.sam.agent.ac.frequency.FrequencyRecordTable;
import org.talend.esb.sam.agent.ac.frequency.FrequencyRule;
import org.talend.esb.sam.agent.ac.frequency.FrequencyRuleTable;
import org.talend.esb.sam.agent.ac.ip.IPRule;
import org.talend.esb.sam.agent.ac.ip.IPRuleTable;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import javax.annotation.PreDestroy;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

/**
 * 数初始化访问控制的控制规则
 *
 */
public class ACInitializer {

    private static final Logger LOG = Logger.getLogger(ACInitializer.class.getName());
    /**
     * 如果对访问控制记录表进行序列化至文件，此属性用于设置文件的路径
     */
    private static final String FILE_PATH = System.getProperty("user.dir") + File.separator + "data" + File.separator + "ac-recordtable-cache";
    /**
     * 访问控制相关的数据库服务提供者
     */
    ACProvider acProvider;
    private Gson gson = new Gson();
    /**
     * 是否启用访问控制记录表的序列化
     */
    private boolean enableRecordTable;

    private ScheduledExecutorService scheduler;

    /**
     * 初始等待时间
     */
    private int startDelay = 10;

    /**
     * 轮询时间间隔
     */
    private int scanIntervall = 10;

    public void setAcProvider(ACProvider acProvider) {
        this.acProvider = acProvider;
    }

    public boolean isEnableRecordTable() {
        return enableRecordTable;
    }

    public void setEnableRecordTable(boolean enableRecordTable) {
        this.enableRecordTable = enableRecordTable;
    }

    public int getStartDelay() {
        return startDelay;
    }

    public void setStartDelay(int startDelay) {
        this.startDelay = startDelay;
    }

    public int getScanIntervall() {
        return scanIntervall;
    }

    public void setScanIntervall(int scanIntervall) {
        this.scanIntervall = scanIntervall;
    }

    /**
     * 初始化程序
     */
    public void init() {
        LOG.info("访问控制策略初始化开始");
        // 初始化IP访问控制规则
        List<ACIP> acIPList = acProvider.listAllACIP();
        acIPList.stream().forEach(acIP -> {
            IPRule ipRule = new IPRule();
            ipRule.setType(acIP.getType());
            ipRule.setBlackList(gson.fromJson(acIP.getBlackList(), new TypeToken<HashMap<String, String>>() {
            }.getType()));
            ipRule.setWhiteList(gson.fromJson(acIP.getWhiteList(), new TypeToken<HashMap<String, String>>() {
            }.getType()));
//            ipRule.setBlackList(acIP.getBlackList());
//            ipRule.setWhiteList(acIP.getWhiteList());
            ipRule.setStatus(acIP.getStatus());
            // 将该服务的规则添加进规则表
            IPRuleTable.getInstance().setRule(acIP.getServiceKey(), ipRule);
        });
        // 初始化流量访问控制规则
        List<ACFlow> acFlowList = acProvider.listAllACFlow();
        acFlowList.stream().forEach(acFlow -> {
            FlowRule flowRule = new FlowRule();
            flowRule.setType(acFlow.getType());
            flowRule.setTimeInterval(acFlow.getTimeInterval());
            flowRule.setIntervalThreshold(acFlow.getIntervalThreshold());
            flowRule.setSingleThreshold(acFlow.getSingleThreshold());
            flowRule.setStatus(acFlow.getStatus());
            // 将该服务的规则添加进规则表
            FlowRuleTable.getInstance().setRule(acFlow.getServiceKey(), flowRule);
        });
        // 初始化频次访问控制规则
        List<ACFrequency> acFrequencyList = acProvider.listAllACFrequency();
        acFrequencyList.stream().forEach(acFrequency -> {
            FrequencyRule frequencyRule = new FrequencyRule();
            frequencyRule.setType(acFrequency.getType());
            frequencyRule.setTimeInterval(acFrequency.getTimeInterval());
            frequencyRule.setThreshold(acFrequency.getThreshold());
            frequencyRule.setStatus(acFrequency.getStatus());
            // 将该服务的规则添加进规则表
            FrequencyRuleTable.getInstance().setRule(acFrequency.getServiceKey(), frequencyRule);
        });
        LOG.info("访问控制策略初始化结束");

        if (enableRecordTable) {
            File acRecordTableCacheFile = new File(FILE_PATH);
            if (acRecordTableCacheFile.exists()) {
                LOG.info("访问控制记录表信息缓存文件，反序列化还原记录信息");
                FlowRecordTable.getInstance().fileToObject(FILE_PATH);
                FrequencyRecordTable.getInstance().fileToObject(FILE_PATH);
                LOG.info("访问控制记录表信息反序列化结束");
            } else {
                LOG.info("不存在访问控制记录表信息缓存文件夹【"+FILE_PATH+"】，创建相应的文件夹");
                boolean created = acRecordTableCacheFile.mkdir();
                if (created) {
                    LOG.info("访问控制记录表信息缓存文件夹【"+FILE_PATH+"】，创建成功");
                } else {
                    LOG.info("访问控制记录表信息缓存文件夹【"+FILE_PATH+"】，创建失败");
                }
            }

            scheduler = Executors.newScheduledThreadPool(1, (Runnable r) -> {
                Thread thread = new Thread(r);
                thread.setName("AC-Thread-Serializing-Records");
                return thread;
            });
            scheduler.scheduleWithFixedDelay(new Runnable() {
                @Override
                public void run() {
                    LOG.config("访问控制记录表信息序列化开始--轮询");
                    FlowRecordTable.getInstance().objectToFile(FILE_PATH);
                    FrequencyRecordTable.getInstance().objectToFile(FILE_PATH);
                    LOG.config("访问控制记录表信息序列化结束--轮询");

                }
            }, startDelay, scanIntervall, TimeUnit.SECONDS);
        }
    }

    @PreDestroy
    public void destroy() {
        if (null != scheduler) {
            scheduler.shutdown();
        }
    }

}
