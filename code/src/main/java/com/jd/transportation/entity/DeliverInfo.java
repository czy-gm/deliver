package com.jd.transportation.entity;

import java.io.Serializable;

/**
 * 派送时效数据
 *
 * @author czy_gm
 * @version 1.0
 * @since 2021/6/5
 */
public class DeliverInfo implements Serializable {
    //目的地id
    private transient int dstId;

    //派送时长
    private int deliverTime;

    //生效日期
    private long effectiveTime;

    //失效日期
    private long expirationTime;

    public DeliverInfo() {
    }

    public DeliverInfo(int dstId, int deliverTime, long effectiveTime, long expirationTime) {
        this.dstId = dstId;
        this.deliverTime = deliverTime;
        this.effectiveTime = effectiveTime;
        this.expirationTime = expirationTime;
    }

    public int getDstId() {
        return dstId;
    }

    public void setDstId(int dstId) {
        this.dstId = dstId;
    }

    public int getDeliverTime() {
        return deliverTime;
    }

    public void setDeliverTime(int deliverTime) {
        this.deliverTime = deliverTime;
    }

    public long getEffectiveTime() {
        return effectiveTime;
    }

    public void setEffectiveTime(long effectiveTime) {
        this.effectiveTime = effectiveTime;
    }

    public long getExpirationTime() {
        return expirationTime;
    }

    public void setExpirationTime(long expirationTime) {
        this.expirationTime = expirationTime;
    }

    @Override
    public String toString() {
        return "DeliverInfo{" +
                "dstId=" + dstId +
                ", deliverTime=" + deliverTime +
                ", effectiveTime=" + effectiveTime +
                ", expirationTime=" + expirationTime +
                '}';
    }
}
