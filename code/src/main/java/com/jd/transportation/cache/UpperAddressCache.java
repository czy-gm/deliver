package com.jd.transportation.cache;

import com.jd.transportation.exception.SDKException;

import java.util.Map;

public interface UpperAddressCache {

    /**
     * 保存上级地址
     * @param addressId 当前地址id
     * @param upperAddressId 上级地址id
     * @throws SDKException SDKException
     */
    void saveUpperAddress(Integer addressId, Integer upperAddressId) throws SDKException;

    /**
     * 批量保存上级地址
     * @param addressMap 地址映射
     */
    void batchSaveUpperAddress(Map<Integer, Integer> addressMap);

    /**
     * 获取当前地址id的上一级地址id
     *
     * @param addressId 地址id
     * @return 上一级地址id
     */
    Integer getUpperLevelAddressId(Integer addressId);

}
