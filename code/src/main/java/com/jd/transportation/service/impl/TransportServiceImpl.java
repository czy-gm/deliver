package com.jd.transportation.service.impl;

import com.jd.transportation.cache.CollectCache;
import com.jd.transportation.cache.DeliverCache;
import com.jd.transportation.cache.TransitCache;
import com.jd.transportation.cache.UpperAddressCache;
import com.jd.transportation.dao.CollectDao;
import com.jd.transportation.dao.DeliverDao;
import com.jd.transportation.dao.TransitDao;
import com.jd.transportation.dao.UpperAddressDao;
import com.jd.transportation.entity.*;
import com.jd.transportation.exception.SDKException;
import com.jd.transportation.model.DeliveryTimeRequest;
import com.jd.transportation.service.TransportService;
import com.jd.transportation.utils.DateUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.List;

/**
 * 本类为物流运输相关需求的业务层实现
 *
 * @author czy_gm
 * @version 1.0
 * @since 2021/6/5
 */
@Service("transportService")
public class TransportServiceImpl implements TransportService {

    private static final Logger logger = LoggerFactory.getLogger(TransportServiceImpl.class);

    @Value("${cache.close}")
    private Boolean cacheClose = false;

    @Resource(name = "collect" + "${cache.name}")
    private CollectCache collectCache;

    @Resource(name = "transit" + "${cache.name}")
    private TransitCache transitCache;

    @Resource(name = "deliver" + "${cache.name}")
    private DeliverCache deliverCache;

    @Resource(name = "upperAddress" + "${cache.name}")
    private UpperAddressCache upperAddressCache;

    @Resource
    private CollectDao collectDao;

    @Resource
    private TransitDao transitDao;

    @Resource
    private DeliverDao deliverDao;

    @Resource
    private UpperAddressDao upperAddressDao;

    /**
     * 通过请求参数从数据中心查询相关时效数据，
     * 包括揽收、中转、派送三个过程，若任一过程没有相关数据，则返回null，否则返回时效数据
     *
     * @param request 查询时效的请求参数
     * @return 未查找相关数据返回null，否则返回时效数据
     * @throws SDKException SDKException
     */
    @Override
    public String getPromisedDeliveryTime(DeliveryTimeRequest request) throws SDKException {

        checkParameter(request);

        LocalDateTime createTime = DateUtil.parseDateTime(request.getCreateTime());

        LocalDateTime promisedTime = processCollect(request, createTime);
        logger.info("time of collect phase:{}", promisedTime);
        if (promisedTime == null) {
            logger.info("There is no corresponding data in the collect stage.");
            return null;
        }

        promisedTime = processTransit(request, createTime, promisedTime);
        logger.info("time of transit phase:{}", promisedTime);
        if (promisedTime == null) {
            logger.info("There is no corresponding data in the transit stage.");
            return null;
        }

        promisedTime = processDeliver(request, createTime, promisedTime);
        logger.info("time of deliver phase:{}", promisedTime);
        if (promisedTime == null) {
            logger.info("There is no corresponding data in the deliver stage.");
            return null;
        }

        return DateUtil.formatDateTime(promisedTime);
    }

    /**
     * 处理揽收阶段的配送时效
     *
     * @param request    查询时效的请求参数
     * @param createTime 下单时间
     * @return 揽收阶段时效 or null
     */
    private LocalDateTime processCollect(DeliveryTimeRequest request, LocalDateTime createTime) throws SDKException {

        int srcAddressCode = getAddressID(request.getSrcProvinceID(), request.getSrcCityID(),
                request.getSrcCountyID(), request.getSrcTownID());

        Boolean isSatisfiedCollectTime = null;

        //始发地优先级比目的地优先级高，所以外层循环为始发地从镇到省，内层循环为目的市到-1（目的地址为空）,
        //如果数据中心没有相关数据，就获取对应的上级地址，再继续查询，直到循环完毕。
        for (int i = 0; i < CollectColumn.SRC_COL_NUM; i++) {
            int dstAddressCode = getAddressID(request.getDstProvinceID(), request.getDstCityID());

            for (int j = 0; j < CollectColumn.DST_COL_NUM + 1; j++) {
                List<CollectInfo> collectInfoList =
                        cacheClose ? collectDao.getBySrcIdAndDstId(srcAddressCode, dstAddressCode) :
                                collectCache.getBySrcIdAndDstId(srcAddressCode, dstAddressCode);
                isSatisfiedCollectTime = isSatisfiedCollectionTime(collectInfoList, createTime);
                if (isSatisfiedCollectTime != null || dstAddressCode == -1) {
                    break;
                }
                dstAddressCode =
                        cacheClose ? upperAddressDao.getUpperLevelAddressId(dstAddressCode) :
                                upperAddressCache.getUpperLevelAddressId(dstAddressCode);
            }

            if (isSatisfiedCollectTime != null || srcAddressCode == -1) {
                break;
            }
            srcAddressCode =
                    cacheClose ? upperAddressDao.getUpperLevelAddressId(srcAddressCode) :
                            upperAddressCache.getUpperLevelAddressId(srcAddressCode);
        }
        //数据中心没有相关时效数据，返回为null
        if (isSatisfiedCollectTime == null) {
            return null;
        }
        logger.info("Is the collection time satisfied:{}", isSatisfiedCollectTime);
        LocalDateTime deliveryTime = createTime;
        //当不满足当天揽收截至时间时，将时效数据加一天，相当于第二天进行揽收
        if (!isSatisfiedCollectTime) {
            deliveryTime = createTime.plusDays(1);
        }
        return deliveryTime;
    }

    /**
     * 处理中转阶段的配送时效
     *
     * @param request    查询时效的请求参数
     * @param createTime 下单时间
     * @param predict    上一阶段的时效
     * @return 查询阶段时效 or null
     */
    private LocalDateTime processTransit(DeliveryTimeRequest request, LocalDateTime createTime, LocalDateTime predict) throws SDKException {
        int srcAddressCode = getAddressID(request.getSrcProvinceID(), request.getSrcCityID());

        Pair<Integer, LocalTime> daysAndTime = null;

        //始发地优先级比目的地优先级高，所以外层循环为始发地市到始发地省，内层循环为目的镇到-1（目的地址为空）,
        //如果数据中心没有相关数据，就获取对应的上级地址，再继续查询，直到循环完毕。
        for (int i = 0; i < TransitColumn.SRC_COL_NUM; i++) {
            int dstAddressCode = getAddressID(request.getDstProvinceID(), request.getDstCityID(),
                    request.getDstCountyID(), request.getDstTownID());

            for (int j = 0; j < TransitColumn.DST_COL_NUM + 1; j++) {
                List<TransitInfo> transitInfoList =
                        cacheClose ? transitDao.getBySrcIdAndDstId(srcAddressCode, dstAddressCode) :
                                transitCache.getBySrcIdAndDstId(srcAddressCode, dstAddressCode);

                daysAndTime = getTransitTime(transitInfoList, createTime);
                if (daysAndTime != null || dstAddressCode == -1) {
                    break;
                }
                dstAddressCode =
                        cacheClose ? upperAddressDao.getUpperLevelAddressId(dstAddressCode) :
                                upperAddressCache.getUpperLevelAddressId(dstAddressCode);
            }

            if (daysAndTime != null || srcAddressCode == -1) {
                break;
            }
            srcAddressCode =
                    cacheClose ? upperAddressDao.getUpperLevelAddressId(srcAddressCode) :
                            upperAddressCache.getUpperLevelAddressId(srcAddressCode);
        }
        //数据中心没有相关时效数据，返回为null
        if (daysAndTime == null) {
            return null;
        }

        predict = predict.plusDays(daysAndTime.getKey() - 1);
        return predict.with(daysAndTime.getValue());
    }

    /**
     * 处理派送阶段的配送时效
     *
     * @param request    查询时效的请求参数
     * @param createTime 下单时间
     * @param predict    上一阶段的时效
     * @return 派送阶段时效 or null
     */
    private LocalDateTime processDeliver(DeliveryTimeRequest request, LocalDateTime createTime, LocalDateTime predict) throws SDKException {
        int dstAddressCode = getAddressID(request.getDstProvinceID(), request.getDstCityID(),
                request.getDstCountyID(), request.getDstTownID());

        Integer deliverTime = null;
        //循环为目的镇到目的省，如果数据中心没有相关数据，就获取对应的上级地址，再继续查询，直到循环完毕。
        for (int i = 0; i < DeliverColumn.DST_COL_NUM; i++) {
            List<DeliverInfo> deliverInfoList =
                    cacheClose ? deliverDao.getByDstId(dstAddressCode) :
                            deliverCache.getByDstId(dstAddressCode);
            deliverTime = getDeliverTime(deliverInfoList, createTime);
            if (deliverTime != null || dstAddressCode == -1) {
                break;
            }
            dstAddressCode =
                    cacheClose ? upperAddressDao.getUpperLevelAddressId(dstAddressCode) :
                            upperAddressCache.getUpperLevelAddressId(dstAddressCode);
        }
        //数据中心没有相关时效数据，返回为null
        if (deliverTime == null) {
            return null;
        }
        return predict.plusHours(deliverTime);
    }

    /**
     * 获取非null的最低级地址
     *
     * @param ids 地址ids
     * @return 非null的最低级地址
     */
    private int getAddressID(Integer... ids) {
        for (int i = ids.length - 1; i >= 0; i--) {
            if (ids[i] != null) {
                return ids[i];
            }
        }
        return -1;
    }

    /**
     * 检查请求参数合法性
     *
     * @param request 查询时效的请求参数
     * @throws SDKException SDKException
     */
    private void checkParameter(DeliveryTimeRequest request) throws SDKException {

        if (!DateUtil.isValid(request.getCreateTime(), DateUtil.formalFtf)) {
            throw new SDKException("createTime format error");
        }

        if (request.getSrcProvinceID() == null) {
            throw new SDKException("srcProvinceId cannot be empty");
        }
        if (request.getDstProvinceID() == null) {
            throw new SDKException("dstProvinceId cannot be empty");
        }


        Integer[] srcCodeArr = new Integer[]{request.getSrcProvinceID(), request.getSrcCityID(),
                request.getSrcCountyID(), request.getSrcTownID()};
        //根据数据中心查询始发地各级地址的上级地址与请求参数是否一直，如果不一致则抛出异常
        for (int i = 1; i < srcCodeArr.length; i++) {
            if (srcCodeArr[i] != null) {
                int upperLevelAddressCode =
                        cacheClose ? upperAddressDao.getUpperLevelAddressId(srcCodeArr[i]) :
                                upperAddressCache.getUpperLevelAddressId(srcCodeArr[i]);
                if (srcCodeArr[i - 1] == null || upperLevelAddressCode != srcCodeArr[i - 1]) {
                    throw new SDKException("src address id is invalid");
                }
            }
        }

        Integer[] dstCodeArr = new Integer[]{request.getDstProvinceID(), request.getDstCityID(),
                request.getDstCountyID(), request.getDstTownID()};
        //根据数据中心查询目的地各级地址的上级地址与请求参数是否一直，如果不一致则抛出异常
        for (int i = 1; i < dstCodeArr.length; i++) {
            if (dstCodeArr[i] != null) {
                int upperLevelAddressCode =
                        cacheClose ? upperAddressDao.getUpperLevelAddressId(dstCodeArr[i]) :
                                upperAddressCache.getUpperLevelAddressId(dstCodeArr[i]);
                if (dstCodeArr[i - 1] == null || upperLevelAddressCode != dstCodeArr[i - 1]) {
                    throw new SDKException("dst address id is invalid");
                }
            }
        }
    }

    /**
     * 是否满足揽收时间
     * @param collectInfoList 揽收时效数据列表
     * @param createTime 下单时间
     * @return 是否满足
     */
    private Boolean isSatisfiedCollectionTime(List<CollectInfo> collectInfoList, LocalDateTime createTime) {
        if (collectInfoList == null || collectInfoList.isEmpty()) {
            return null;
        }
        long target = createTime.toInstant(ZoneOffset.ofHours(8)).toEpochMilli();

        //当时效数据列表大小小于5时，采用循环方式查询，否则采用二分法查询
        if (collectInfoList.size() < 5) {
            for (CollectInfo collectInfo : collectInfoList) {
                if (collectInfo.getEffectiveTime() <= target && collectInfo.getExpirationTime() >= target) {
                    logger.info("corresponding collect data:{}", collectInfo);
                    LocalDateTime predictCollectionTime = createTime.plusHours(collectInfo.getCollectTime());
                    if (predictCollectionTime.toLocalDate().isAfter(createTime.toLocalDate())) {
                        return false;
                    }
                    return !predictCollectionTime.toLocalTime().isAfter(collectInfo.getCollectEndTime());
                }
            }
        } else {
            int beg = 0, end = collectInfoList.size();
            while (beg < end) {
                int mid = (beg + end) >> 1;
                CollectInfo collectInfo = collectInfoList.get(mid);
                if (collectInfo.getEffectiveTime() <= target && collectInfo.getExpirationTime() >= target) {
                    logger.info("corresponding collect data:{}", collectInfo);
                    LocalDateTime predictCollectionTime = createTime.plusHours(collectInfo.getCollectTime());
                    if (predictCollectionTime.toLocalDate().isAfter(createTime.toLocalDate())) {
                        return false;
                    }
                    return !predictCollectionTime.toLocalTime().isAfter(collectInfo.getCollectEndTime());
                } else if (collectInfo.getExpirationTime() < target) {
                    beg = mid + 1;
                } else {
                    end = mid;
                }
            }
        }
        return null;
    }

    /**
     * 获取中转时间
     * @param transitInfoList 中转时效数据
     * @param createTime 下单时间
     * @return 中转时间
     */
    private Pair<Integer, LocalTime> getTransitTime(List<TransitInfo> transitInfoList, LocalDateTime createTime) {
        if (CollectionUtils.isEmpty(transitInfoList)) {
            return null;
        }
        long target = createTime.toInstant(ZoneOffset.ofHours(8)).toEpochMilli();
        //当时效数据列表大小小于5时，采用循环方式查询，否则采用二分法查询
        if (transitInfoList.size() < 5) {
            for (TransitInfo transitInfo : transitInfoList) {
                if (transitInfo.getEffectiveTime() <= target && transitInfo.getExpirationTime() >= target) {
                    logger.info("corresponding transit data:{}", transitInfo);
                    return Pair.of(transitInfo.getTransitDays(), transitInfo.getTransitTime());
                }
            }
        } else {
            int beg = 0, end = transitInfoList.size();
            while (beg < end) {
                int mid = (beg + end) >> 1;
                TransitInfo transitInfo = transitInfoList.get(mid);
                if (transitInfo.getEffectiveTime() <= target && transitInfo.getExpirationTime() >= target) {
                    logger.info("corresponding transit data:{}", transitInfo);
                    return Pair.of(transitInfo.getTransitDays(), transitInfo.getTransitTime());
                } else if (transitInfo.getExpirationTime() < target) {
                    beg = mid + 1;
                } else {
                    end = mid;
                }
            }
        }
        return null;
    }

    /**
     * 获取派送时间
     * @param deliverInfoList 派送时效数据列表
     * @param createTime 下单时间
     * @return 派送时间
     */
    private Integer getDeliverTime(List<DeliverInfo> deliverInfoList, LocalDateTime createTime) {
        if (CollectionUtils.isEmpty(deliverInfoList)) {
            return null;
        }
        long target = createTime.toInstant(ZoneOffset.ofHours(8)).toEpochMilli();
        //当时效数据列表大小小于5时，采用循环方式查询，否则采用二分法查询
        if (deliverInfoList.size() < 5) {
            for (DeliverInfo deliverInfo : deliverInfoList) {
                if (deliverInfo.getEffectiveTime() <= target && deliverInfo.getExpirationTime() >= target) {
                    logger.info("corresponding deliver data:{}", deliverInfo);
                    return deliverInfo.getDeliverTime();
                }
            }
        } else {
            int beg = 0, end = deliverInfoList.size();
            while (beg < end) {
                int mid = (beg + end) >>> 1;
                DeliverInfo deliverInfo = deliverInfoList.get(mid);
                if (deliverInfo.getEffectiveTime() <= target && deliverInfo.getExpirationTime() >= target) {
                    logger.info("corresponding deliver data:{}", deliverInfo);
                    return deliverInfo.getDeliverTime();
                } else if (deliverInfo.getExpirationTime() < target) {
                    beg = mid + 1;
                } else {
                    end = mid;
                }
            }
        }
        return null;
    }
}
