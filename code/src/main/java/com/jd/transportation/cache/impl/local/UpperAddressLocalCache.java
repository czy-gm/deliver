package com.jd.transportation.cache.impl.local;

import com.jd.transportation.cache.UpperAddressCache;
import com.jd.transportation.exception.SDKException;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository("upperAddressLocalCache")
public class UpperAddressLocalCache implements UpperAddressCache {

    //上下级地址映射，key为地址id，value为该地址的上级地址id
    private final Map<Integer, Integer> upperLevelAddressIdMap = new HashMap<>();

    /**
     * {@inheritDoc}
     */
    @Override
    public void saveUpperAddress(Integer addressId, Integer upperAddressId) throws SDKException {
        if (addressId == null) {
            throw new SDKException("addressId invalid");
        }
        upperLevelAddressIdMap.put(addressId, upperAddressId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void batchSaveUpperAddress(Map<Integer, Integer> addressMap) {
        upperLevelAddressIdMap.putAll(addressMap);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer getUpperLevelAddressId(Integer addressId) {
        return upperLevelAddressIdMap.getOrDefault(addressId, -1);
    }
}
