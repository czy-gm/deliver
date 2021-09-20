package com.jd.transportation.cache.impl.redis;

import com.jd.transportation.cache.UpperAddressCache;
import com.jd.transportation.exception.SDKException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.Map;
import java.util.stream.Collectors;

@Repository("upperAddressRedisCache")
public class UpperAddressRedisCache implements UpperAddressCache {

    @Resource
    private StringRedisTemplate addressLevelRedis;

    /**
     * {@inheritDoc}
     */
    @Override
    public void saveUpperAddress(Integer addressId, Integer upperAddressId) throws SDKException {
        if (addressId == null || upperAddressId == null) {
            throw new SDKException("addressId invalid");
        }
        addressLevelRedis.opsForValue().set(String.valueOf(addressId), String.valueOf(upperAddressId));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void batchSaveUpperAddress(Map<Integer, Integer> addressMap) {
        Map<String, String> stringAddressMap = addressMap.entrySet().stream().
                collect(Collectors.toMap(x -> String.valueOf(x.getKey()), y -> String.valueOf(y.getValue())));
        addressLevelRedis.opsForValue().multiSet(stringAddressMap);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer getUpperLevelAddressId(Integer addressId) {
        String upperAddressId = addressLevelRedis.opsForValue().get(String.valueOf(addressId));
        return StringUtils.isNumeric(upperAddressId) ? Integer.parseInt(upperAddressId) : -1;
    }
}
