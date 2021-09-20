package com.jd.transportation.dao;

import com.jd.transportation.entity.TransitInfo;

import java.util.List;

public interface TransitDao {

    void save(TransitInfo transitInfo);

    void saveBatch(List<TransitInfo> transitInfoList);

    List<TransitInfo> getBySrcIdAndDstId(int srcId, int dstId);

}
