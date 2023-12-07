package org.talend.esb.sam.agent.ac.frequency;

import java.io.Serializable;

/**
 * 频次记录对象
 */
public class FrequencyRecord implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 时间区间内首次访问的时间戳
     */
    private long firstTimestamp;

    /**
     * 访问次数计数器
     */
    private long count;

    public FrequencyRecord() {
    }

    public FrequencyRecord(long firstTimestamp, long count) {
        this.firstTimestamp = firstTimestamp;
        this.count = count;
    }

    public long getFirstTimestamp() {
        return firstTimestamp;
    }

    public void setFirstTimestamp(long firstTimestamp) {
        this.firstTimestamp = firstTimestamp;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    /**
     * 累加计数器并返回累加后的值
     *
     * @return
     */
    public long incrCount() {
        this.count += 1L;
        return this.count;
    }
}
