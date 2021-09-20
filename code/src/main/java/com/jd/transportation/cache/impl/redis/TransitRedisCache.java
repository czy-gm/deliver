package com.jd.transportation.cache.impl.redis;

import com.jd.transportation.cache.TransitCache;
import com.jd.transportation.entity.TransitInfo;
import com.jd.transportation.exception.SDKException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

@Repository("transitRedisCache")
public class TransitRedisCache implements TransitCache {

    @Resource
    private RedisTemplate<String, TransitInfo> transitRedis;

    /**
     * {@inheritDoc}
     */
    @Override
    public void save(Integer srcId, Integer dstId, TransitInfo transitInfo) throws SDKException {
        if (srcId == null || dstId == null) {
            throw new SDKException("srcId or dstId invalid");
        }
        String key = "tra:" + srcId + "-" + dstId;
        transitRedis.opsForList().rightPush(key, transitInfo);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void saveAll(Integer srcId, Integer dstId, List<TransitInfo> transitInfoList) throws SDKException {
        if (srcId == null || dstId == null) {
            throw new SDKException("srcId or dstId invalid");
        }
        String key = "tra:" + srcId + "-" + dstId;
        transitRedis.opsForList().rightPushAll(key, transitInfoList);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<TransitInfo> getBySrcIdAndDstId(Integer srcId, Integer dstId) throws SDKException {
        if (srcId == null || dstId == null) {
            throw new SDKException("srcId or dstId invalid");
        }
        String key = "tra:" + srcId + "-" + dstId;
        return transitRedis.opsForList().range(key, 0, -1);
    }
}
