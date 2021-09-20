package com.jd.transportation.entity;

/**
 * 派送文件的列名及索引
 *
 * @author czy_gm
 * @version 1.0
 * @since 2021/6/5
 */
public enum DeliverColumn {

    DST_PROVINCE("dstProvince", 0),

    DST_PROVINCE_ID("dstProvinceId", 1),

    DST_CITY("dstCity", 2),

    DST_CITY_ID("dstCityId", 3),

    DST_COUNTY("dstCounty", 4),

    DST_COUNTY_ID("dstCountyId", 5),

    DST_TOWN("dstTown", 6),

    DST_TOWN_ID("dstTownId", 7),

    DELIVER_TIME("deliverTime", 8),

    EFFECTIVE_TIME("effectiveTime", 9),

    EXPIRATION_TIME("expirationTime", 10);

    public static final int DST_COL_NUM = 4;

    private final String name;

    private final int inx;

    DeliverColumn(String name, int inx) {
        this.name = name;
        this.inx = inx;
    }

    public String getName() {
        return name;
    }

    public int getInx() {
        return inx;
    }

    @Override
    public String toString() {
        return "DeliverColumn{" +
                "name='" + name + '\'' +
                ", inx=" + inx +
                '}';
    }
}
