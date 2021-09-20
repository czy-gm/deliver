package com.jd.transportation.dao;

import com.jd.transportation.entity.DeliverInfo;

import java.util.List;

public interface DeliverDao {

    void save(DeliverInfo deliverInfo);

    void saveBatch(List<DeliverInfo> deliverInfoList);

    List<DeliverInfo> getByDstId(int dstId);
}
