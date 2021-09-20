package com.jd.transportation.cache.impl.local;

import com.jd.transportation.cache.DeliverCache;
import com.jd.transportation.entity.DeliverInfo;
import com.jd.transportation.exception.SDKException;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository("deliverLocalCache")
public class DeliverLocalCache implements DeliverCache {

    //派送时效数据，key为目的地id，value为派送时效数据列表
    private final Map<Integer, List<DeliverInfo>> deliverInfoMap = new HashMap<>();

    /**
     * {@inheritDoc}
     */
    @Override
    public void save(Integer dstId, DeliverInfo deliverInfo) throws SDKException {
        if (dstId == null) {
            throw new SDKException("addressId invalid");
        }
        List<DeliverInfo> deliverInfoList = deliverInfoMap.get(dstId);

        if (deliverInfoList == null) {
            deliverInfoMap.put(dstId, Arrays.asList(deliverInfo));
        } else {
            deliverInfoList.add(deliverInfo);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void saveAll(Integer dstId, List<DeliverInfo> newDelivers) throws SDKException {
        if (dstId == null) {
            throw new SDKException("addressId invalid");
        }
        List<DeliverInfo> deliverInfoList = deliverInfoMap.get(dstId);

        if (deliverInfoList == null) {
            List<DeliverInfo> deliverInfos = new ArrayList<>(newDelivers);
            deliverInfoMap.put(dstId, deliverInfos);
        } else {
            deliverInfoList.addAll(newDelivers);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<DeliverInfo> getByDstId(Integer dstId) {
        return deliverInfoMap.get(dstId);
    }
}
