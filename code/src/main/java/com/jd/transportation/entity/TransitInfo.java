package com.jd.transportation.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;

import java.io.Serializable;
import java.time.LocalTime;

/**
 * 中转时效数据
 *
 * @author czy_gm
 * @version 1.0
 * @since 2021/6/5
 */
public class TransitInfo implements Serializable {

    //始发地id
    private transient int srcId;

    //目的地id
    private transient int dstId;

    //中转天数
    private int transitDays;

    //中转时间（时分）
    @JsonFormat(pattern = "HH:mm")
    @JsonDeserialize(using = LocalTimeDeserializer.class)
    @JsonSerialize(using = LocalTimeSerializer.class)
    private LocalTime transitTime;

    //生效日期
    private long effectiveTime;

    //失效日期
    private long expirationTime;

    public TransitInfo() {
    }

    public TransitInfo(int srcId, int dstId, int transitDays, LocalTime transitTime, long effectiveTime, long expirationTime) {
        this.srcId = srcId;
        this.dstId = dstId;
        this.transitDays = transitDays;
        this.transitTime = transitTime;
        this.effectiveTime = effectiveTime;
        this.expirationTime = expirationTime;
    }

    public int getSrcId() {
        return srcId;
    }

    public void setSrcId(int srcId) {
        this.srcId = srcId;
    }

    public int getDstId() {
        return dstId;
    }

    public void setDstId(int dstId) {
        this.dstId = dstId;
    }

    public int getTransitDays() {
        return transitDays;
    }

    public void setTransitDays(int transitDays) {
        this.transitDays = transitDays;
    }

    public LocalTime getTransitTime() {
        return transitTime;
    }

    public void setTransitTime(LocalTime transitTime) {
        this.transitTime = transitTime;
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
        return "TransitInfo{" +
                "srcId=" + srcId +
                ", dstId=" + dstId +
                ", transitDays=" + transitDays +
                ", transitTime=" + transitTime +
                ", effectiveTime=" + effectiveTime +
                ", expirationTime=" + expirationTime +
                '}';
    }
}
