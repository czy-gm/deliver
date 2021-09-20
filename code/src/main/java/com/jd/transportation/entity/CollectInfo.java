package com.jd.transportation.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;

import java.io.Serializable;
import java.time.LocalTime;

/**
 * 揽收时效数据
 *
 * @author czy_gm
 * @version 1.0
 * @since 2021/6/5
 */
public class CollectInfo implements Serializable {

    //始发地id
    private transient int srcId;

    //目的地id
    private transient int dstId;

    //揽收截至时间
    @JsonFormat(pattern = "HH:mm")
    @JsonDeserialize(using = LocalTimeDeserializer.class)
    @JsonSerialize(using = LocalTimeSerializer.class)
    private LocalTime collectEndTime;

    //揽收时长
    private int collectTime;

    //生效日期
    private long effectiveTime;

    //失效日期
    private long expirationTime;

    public CollectInfo() {
    }

    public CollectInfo(int srcId, int dstId, LocalTime collectEndTime, int collectTime,
                       long effectiveTime, long expirationTime) {
        this.srcId = srcId;
        this.dstId = dstId;
        this.collectEndTime = collectEndTime;
        this.collectTime = collectTime;
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

    public LocalTime getCollectEndTime() {
        return collectEndTime;
    }

    public void setCollectEndTime(LocalTime collectEndTime) {
        this.collectEndTime = collectEndTime;
    }

    public int getCollectTime() {
        return collectTime;
    }

    public void setCollectTime(int collectTime) {
        this.collectTime = collectTime;
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
        return "CollectInfo{" +
                "srcId=" + srcId +
                ", dstId=" + dstId +
                ", collectEndTime=" + collectEndTime +
                ", collectTime=" + collectTime +
                ", effectiveTime=" + effectiveTime +
                ", expirationTime=" + expirationTime +
                '}';
    }
}
