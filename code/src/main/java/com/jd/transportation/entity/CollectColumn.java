package com.jd.transportation.entity;

/**
 * 揽收文件的列名及索引
 *
 * @author czy_gm
 * @version 1.0
 * @since 2021/6/5
 */
public enum CollectColumn {

    SRC_PROVINCE("srcProvince", 0),

    SRC_PROVINCE_ID("srcProvinceId", 1),

    SRC_CITY("srcCity", 2),

    SRC_CITY_ID("srcCityId", 3),

    SRC_COUNTY("srcCounty", 4),

    SRC_COUNTY_ID("srcCountyId", 5),

    SRC_TOWN("srcTown", 6),

    SRC_TOWN_ID("srcTownId", 7),

    DST_PROVINCE("dstProvince", 8),

    DST_PROVINCE_ID("dstProvinceId", 9),

    DST_CITY("dstCity", 10),

    DST_CITY_ID("dstCityId", 11),

    COLLECT_END_TIME("collectEndTime", 12),

    COLLECT_TIME("collectTime", 13),

    EFFECTIVE_TIME("effectiveTime", 14),

    EXPIRATION_TIME("expirationTime", 15);

    public static final int SRC_COL_NUM = 4;

    public static final int DST_COL_NUM = 2;

    private final String name;

    private final int inx;

    CollectColumn(String name, int inx) {
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
        return "CollectColumn{" +
                "name='" + name + '\'' +
                ", inx=" + inx +
                '}';
    }
}
