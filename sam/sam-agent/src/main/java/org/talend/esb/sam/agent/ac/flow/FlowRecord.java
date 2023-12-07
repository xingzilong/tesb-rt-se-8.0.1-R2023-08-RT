package org.talend.esb.sam.agent.ac.flow;

import java.io.Serializable;

/**
 * 流量控制记录对象
 */
public class FlowRecord implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 时间区间内首次访问的时间戳
     */
    private long firstTimestamp;

    /**
     * 访问流量计数器
     */
    private long flowSize;

    public FlowRecord() {
    }

    public FlowRecord(long firstTimestamp, long flowSize) {
        this.firstTimestamp = firstTimestamp;
        this.flowSize = flowSize;
    }

    public long getFirstTimestamp() {
        return firstTimestamp;
    }

    public void setFirstTimestamp(long firstTimestamp) {
        this.firstTimestamp = firstTimestamp;
    }

    public long getFlowSize() {
        return flowSize;
    }

    public void setFlowSize(long count) {
        this.flowSize = count;
    }

    /**
     * 累加计数器并返回累加后的值
     *
     * @return
     */
    public long incrFlowSize(Long flowSize) {
        this.flowSize += flowSize;
        return this.flowSize;
    }


}
