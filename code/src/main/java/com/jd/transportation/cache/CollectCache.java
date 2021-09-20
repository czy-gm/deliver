package com.jd.transportation.cache;

import com.jd.transportation.entity.CollectInfo;
import com.jd.transportation.exception.SDKException;

import java.util.List;

public interface CollectCache {

    /**
     * 保存单个数据
     * @param srcId 始发地id
     * @param dstId 目的地id
     * @param collectInfo 揽收时效数据
     * @throws SDKException SDKException
     */
    void save(Integer srcId, Integer dstId, CollectInfo collectInfo) throws SDKException;

    /**
     * 批量保存数据
     * @param srcId 始发地id
     * @param dstId 目的地id
     * @param collectInfoList 揽收时效数据列表
     * @throws SDKException SDKException
     */
    void saveAll(Integer srcId, Integer dstId, List<CollectInfo> collectInfoList) throws SDKException;

    /**
     * 获取揽收时效数据列表
     * @param srcId 始发地id
     * @param dstId 目的地id
     * @return 揽收时效数据列表
     * @throws SDKException SDKException
     */
    List<CollectInfo> getBySrcIdAndDstId(Integer srcId, Integer dstId) throws SDKException;
}
