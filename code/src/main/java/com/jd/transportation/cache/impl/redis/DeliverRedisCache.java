package com.jd.transportation.cache.impl.redis;

import com.jd.transportation.cache.DeliverCache;
import com.jd.transportation.entity.DeliverInfo;
import com.jd.transportation.exception.SDKException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

@Repository("deliverRedisCache")
public class DeliverRedisCache implements DeliverCache {

    @Resource
    private RedisTemplate<String, DeliverInfo> deliverRedis;

    /**
     * {@inheritDoc}
     */
    @Override
    public void save(Integer dstId, DeliverInfo deliverInfo) throws SDKException {
        if (dstId == null) {
            throw new SDKException("srcId or dstId invalid");
        }
        String key = "del:" + dstId;
        deliverRedis.opsForList().rightPush(key, deliverInfo);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void saveAll(Integer dstId, List<DeliverInfo> deliverInfoList) throws SDKException {
        if (dstId == null) {
            throw new SDKException("srcId or dstId invalid");
        }
        String key = "del:" + dstId;
        deliverRedis.opsForList().rightPushAll(key, deliverInfoList);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<DeliverInfo> getByDstId(Integer dstId) throws SDKException {
        if (dstId == null) {
            throw new SDKException("srcId or dstId invalid");
        }
        String key = "del:" + dstId;
        return deliverRedis.opsForList().range(key, 0, -1);
    }
}
