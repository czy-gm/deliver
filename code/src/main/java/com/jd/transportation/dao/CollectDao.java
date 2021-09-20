package com.jd.transportation.dao;


import com.jd.transportation.entity.CollectInfo;

import java.util.List;

public interface CollectDao {

    void save(CollectInfo collectInfo);

    void saveBatch(List<CollectInfo> collectInfoList);

    List<CollectInfo> getBySrcIdAndDstId(int srcId, int dstId);
}
