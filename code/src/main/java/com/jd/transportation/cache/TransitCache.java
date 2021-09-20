package com.jd.transportation.cache;

import com.jd.transportation.entity.TransitInfo;
import com.jd.transportation.exception.SDKException;

import java.util.List;

public interface TransitCache {

    /**
     * 保存单个数据
     * @param srcId 始发地id
     * @param dstId 目的地id
     * @param transitInfo 中转时效数据
     * @throws SDKException SDKException
     */
    void save(Integer srcId, Integer dstId, TransitInfo transitInfo) throws SDKException;

    /**
     * 批量保存数据
     * @param srcId 始发地id
     * @param dstId 目的地id
     * @param transitInfoList 中转时效数据列表
     * @throws SDKException SDKException
     */
    void saveAll(Integer srcId, Integer dstId, List<TransitInfo> transitInfoList) throws SDKException;

    /**
     * 获取派送时效数据列表
     * @param srcId 始发地id
     * @param dstId 目的地id
     * @return 派送时效数据列表
     * @throws SDKException SDKException
     */
    List<TransitInfo> getBySrcIdAndDstId(Integer srcId, Integer dstId) throws SDKException;

}
