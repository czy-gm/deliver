package com.jd.transportation.cache.impl.redis;

import com.jd.transportation.cache.CollectCache;
import com.jd.transportation.entity.CollectInfo;
import com.jd.transportation.exception.SDKException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

@Repository("collectRedisCache")
public class CollectRedisCache implements CollectCache {

    @Resource
    private RedisTemplate<String, CollectInfo> collectRedis;

    /**
     * {@inheritDoc}
     */
    @Override
    public void save(Integer srcId, Integer dstId, CollectInfo collectInfo) throws SDKException {
        if (srcId == null || dstId == null) {
            throw new SDKException("srcId or dstId invalid");
        }
        String key = "col:" + srcId + "-" + dstId;
        collectRedis.opsForList().rightPush(key, collectInfo);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void saveAll(Integer srcId, Integer dstId, List<CollectInfo> collectInfoList) throws SDKException {
        if (srcId == null || dstId == null) {
            throw new SDKException("srcId or dstId invalid");
        }
        String key = "col:" + srcId + "-" + dstId;
        collectRedis.opsForList().rightPushAll(key, collectInfoList);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<CollectInfo> getBySrcIdAndDstId(Integer srcId, Integer dstId) throws SDKException {
        if (srcId == null || dstId == null) {
            throw new SDKException("srcId or dstId invalid");
        }
        String key = "col:" + srcId + "-" + dstId;
        return collectRedis.opsForList().range(key, 0, -1);
    }
}
