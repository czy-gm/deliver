package com.jd.transportation.service;

import com.jd.transportation.exception.SDKException;
import com.jd.transportation.model.DeliveryTimeRequest;

public interface TransportService {

    /**
     * 通过请求参数从数据中心查询相关时效数据，
     * @param request 查询时效的请求参数
     * @return 未查找相关数据返回null，否则返回时效数据
     * @throws SDKException SDKException
     */
    String getPromisedDeliveryTime(DeliveryTimeRequest request) throws SDKException;

}
