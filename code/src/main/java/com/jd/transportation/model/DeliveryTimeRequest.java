package com.jd.transportation.model;

/**
 * 查询配送时效数据的请求参数类
 *
 * @author czy_gm
 * @version 1.0
 * @since 2021/6/5
 */
public class DeliveryTimeRequest {

    //下单时间
    private String createTime;

    //始发地的省id
    private Integer srcProvinceID;

    //始发地的市id
    private Integer srcCityID;

    //始发地的县id
    private Integer srcCountyID;

    //始发地的镇id
    private Integer srcTownID;

    //目的地的省id
    private Integer dstProvinceID;

    //目的地的市id
    private Integer dstCityID;

    //目的地的县id
    private Integer dstCountyID;

    //目的地的镇id
    private Integer dstTownID;

    public DeliveryTimeRequest(String createTime, Integer srcProvinceID, Integer srcCityID,
                               Integer srcCountyID, Integer srcTownID, Integer dstProvinceID,
                               Integer dstCityID, Integer dstCountyID, Integer dstTownID) {
        this.createTime = createTime;
        this.srcProvinceID = srcProvinceID;
        this.srcCityID = srcCityID;
        this.srcCountyID = srcCountyID;
        this.srcTownID = srcTownID;
        this.dstProvinceID = dstProvinceID;
        this.dstCityID = dstCityID;
        this.dstCountyID = dstCountyID;
        this.dstTownID = dstTownID;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public Integer getSrcProvinceID() {
        return srcProvinceID;
    }

    public void setSrcProvinceID(Integer srcProvinceID) {
        this.srcProvinceID = srcProvinceID;
    }

    public Integer getSrcCityID() {
        return srcCityID;
    }

    public void setSrcCityID(Integer srcCityID) {
        this.srcCityID = srcCityID;
    }

    public Integer getSrcCountyID() {
        return srcCountyID;
    }

    public void setSrcCountyID(Integer srcCountyID) {
        this.srcCountyID = srcCountyID;
    }

    public Integer getSrcTownID() {
        return srcTownID;
    }

    public void setSrcTownID(Integer srcTownID) {
        this.srcTownID = srcTownID;
    }

    public Integer getDstProvinceID() {
        return dstProvinceID;
    }

    public void setDstProvinceID(Integer dstProvinceID) {
        this.dstProvinceID = dstProvinceID;
    }

    public Integer getDstCityID() {
        return dstCityID;
    }

    public void setDstCityID(Integer dstCityID) {
        this.dstCityID = dstCityID;
    }

    public Integer getDstCountyID() {
        return dstCountyID;
    }

    public void setDstCountyID(Integer dstCountyID) {
        this.dstCountyID = dstCountyID;
    }

    public Integer getDstTownID() {
        return dstTownID;
    }

    public void setDstTownID(Integer dstTownID) {
        this.dstTownID = dstTownID;
    }

    @Override
    public String toString() {
        return "OrderInfo{" +
                "createTime='" + createTime + '\'' +
                ", srcProvinceID=" + srcProvinceID +
                ", srcCityID=" + srcCityID +
                ", srcCountyID=" + srcCountyID +
                ", srcTownID=" + srcTownID +
                ", dstProvinceID=" + dstProvinceID +
                ", dstCityID=" + dstCityID +
                ", dstCountyID=" + dstCountyID +
                ", dstTownID=" + dstTownID +
                '}';
    }
}
