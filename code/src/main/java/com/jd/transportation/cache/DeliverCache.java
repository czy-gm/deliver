package com.jd.transportation.cache;

import com.jd.transportation.entity.DeliverInfo;
import com.jd.transportation.exception.SDKException;

import java.util.List;

public interface DeliverCache {

    /**
     * 保存单个数据
     * @param dstId 目的地id
     * @param deliverInfo 派送时效数据
     * @throws SDKException SDKException
     */
    void save(Integer dstId, DeliverInfo deliverInfo) throws SDKException;

    /**
     * 批量保存数据
     * @param dstId 目的地id
     * @param deliverInfoList 派送时效数据列表
     * @throws SDKException SDKException
     */
    void saveAll(Integer dstId, List<DeliverInfo> deliverInfoList) throws SDKException;

    /**
     * 获取派送时效数据列表
     * @param dstId 目的地id
     * @return 派送时效数据列表
     * @throws SDKException SDKException
     */
    List<DeliverInfo> getByDstId(Integer dstId) throws SDKException;

}
