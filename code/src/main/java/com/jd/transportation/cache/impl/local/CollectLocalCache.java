package com.jd.transportation.cache.impl.local;

import com.jd.transportation.cache.CollectCache;
import com.jd.transportation.entity.CollectInfo;
import com.jd.transportation.exception.SDKException;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository("collectLocalCache")
public class CollectLocalCache implements CollectCache {

    //揽收时效数据，一级key为始发地id，二级key为目的地id，value为揽收时效数据列表
    private final Map<Integer, Map<Integer, List<CollectInfo>>> collectInfoMap = new HashMap<>();

    /**
     * {@inheritDoc}
     */
    @Override
    public void save(Integer srcId, Integer dstId, CollectInfo collectInfo) throws SDKException {
        if (srcId == null || dstId == null) {
            throw new SDKException("addressId invalid");
        }
        Map<Integer, List<CollectInfo>> innerMap = collectInfoMap.get(srcId);
        if (innerMap == null) {
            innerMap = new HashMap<>();
            innerMap.put(dstId, Arrays.asList(collectInfo));
            collectInfoMap.put(srcId, innerMap);
        } else {
            List<CollectInfo> collectInfoList = innerMap.get(dstId);
            if (collectInfoList == null) {
                innerMap.put(dstId, Arrays.asList(collectInfo));
            } else {
                collectInfoList.add(collectInfo);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void saveAll(Integer srcId, Integer dstId, List<CollectInfo> addCollectList) throws SDKException {
        if (srcId == null || dstId == null) {
            throw new SDKException("addressId invalid");
        }
        Map<Integer, List<CollectInfo>> innerMap = collectInfoMap.get(srcId);
        if (innerMap == null) {
            innerMap = new HashMap<>();
            innerMap.put(dstId, addCollectList);
            collectInfoMap.put(srcId, innerMap);
        } else {
            List<CollectInfo> collectInfoList = innerMap.get(dstId);
            if (collectInfoList == null) {
                List<CollectInfo> newCollectInfoList = new ArrayList<>(addCollectList);
                innerMap.put(dstId, newCollectInfoList);
            } else {
                innerMap.put(dstId, addCollectList);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<CollectInfo> getBySrcIdAndDstId(Integer srcId, Integer dstId) {
        Map<Integer, List<CollectInfo>> innerMap = collectInfoMap.get(srcId);
        if (innerMap == null || innerMap.isEmpty()) {
            return null;
        }
        return innerMap.get(dstId);
    }
}
