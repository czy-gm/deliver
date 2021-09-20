package com.jd.transportation.entity;

/**
 * 中转文件的列名及索引
 *
 * @author czy_gm
 * @version 1.0
 * @since 2021/6/5
 */
public enum TransitColumn {

    SRC_PROVINCE("srcProvince", 0),

    SRC_PROVINCE_ID("srcProvinceId", 1),

    SRC_CITY("srcCity", 2),

    SRC_CITY_ID("srcCityId", 3),

    DST_PROVINCE("dstProvince", 4),

    DST_PROVINCE_ID("dstProvinceId", 5),

    DST_CITY("dstCity", 6),

    DST_CITY_ID("dstCityId", 7),

    DST_COUNTY("dstCounty", 8),

    DST_COUNTY_ID("dstCountyId", 9),

    DST_TOWN("dstTown", 10),

    DST_TOWN_ID("dstTownId", 11),

    TRANSIT_TIME("transitTime", 12),

    EFFECTIVE_TIME("effectiveTime", 13),

    EXPIRATION_TIME("expirationTime", 14);

    public static final int SRC_COL_NUM = 2;

    public static final int DST_COL_NUM = 4;

    private final String name;

    private final int inx;

    TransitColumn(String name, int inx) {
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
        return "TransitColumn{" +
                "name='" + name + '\'' +
                ", inx=" + inx +
                '}';
    }
}
