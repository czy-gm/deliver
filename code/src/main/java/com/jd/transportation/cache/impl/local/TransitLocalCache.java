package com.jd.transportation.cache.impl.local;

import com.jd.transportation.cache.TransitCache;
import com.jd.transportation.entity.TransitInfo;
import com.jd.transportation.exception.SDKException;
import org.springframework.stereotype.Repository;

import java.util.*;


@Repository("transitLocalCache")
public class TransitLocalCache implements TransitCache {

    //中转时效数据，一级key为始发地id，二级key为目的地id，value为中转时效数据列表
    private final Map<Integer, Map<Integer, List<TransitInfo>>> transitInfoMap = new HashMap<>();

    /**
     * {@inheritDoc}
     */
    @Override
    public void save(Integer srcId, Integer dstId, TransitInfo transitInfo) throws SDKException {
        if (srcId == null || dstId == null) {
            throw new SDKException("addressId invalid");
        }
        Map<Integer, List<TransitInfo>> innerMap = transitInfoMap.get(srcId);
        if (innerMap == null) {
            innerMap = new HashMap<>();
            innerMap.put(dstId, Arrays.asList(transitInfo));
            transitInfoMap.put(srcId, innerMap);
        } else {
            List<TransitInfo> transitInfoList = innerMap.get(dstId);
            if (transitInfoList == null) {
                innerMap.put(dstId, Arrays.asList(transitInfo));
            } else {
                transitInfoList.add(transitInfo);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void saveAll(Integer srcId, Integer dstId, List<TransitInfo> addTransitInfoList) throws SDKException {
        if (srcId == null || dstId == null) {
            throw new SDKException("addressId invalid");
        }
        Map<Integer, List<TransitInfo>> innerMap = transitInfoMap.get(srcId);
        if (innerMap == null) {
            innerMap = new HashMap<>();
            innerMap.put(dstId, addTransitInfoList);
            transitInfoMap.put(srcId, innerMap);
        } else {
            List<TransitInfo> transitInfoList = innerMap.get(dstId);
            if (transitInfoList == null) {
                List<TransitInfo> newTransitList = new ArrayList<>(addTransitInfoList);
                innerMap.put(dstId, newTransitList);
            } else {
                innerMap.put(dstId, addTransitInfoList);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<TransitInfo> getBySrcIdAndDstId(Integer srcId, Integer dstId) throws SDKException {
        Map<Integer, List<TransitInfo>> innerMap = transitInfoMap.get(srcId);
        if (innerMap == null || innerMap.isEmpty()) {
            return null;
        }
        return innerMap.get(dstId);
    }
}
