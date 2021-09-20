package com.jd.transportation.controller;

import com.jd.transportation.exception.SDKException;
import com.jd.transportation.model.DeliveryTimeRequest;
import com.jd.transportation.model.DeliveryTimeResponse;
import com.jd.transportation.service.TransportService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.time.format.DateTimeParseException;

/**
 * 本类主要是物流运输相关需求的控制层实现
 *
 * @author czy_gm
 * @version 1.0
 * @since 2021/6/5
 */
@RestController
public class TransportController {

    private static final Logger logger = LoggerFactory.getLogger(TransportController.class);

    @Autowired
    private TransportService transportService;

    /**
     * 查询配送时效的接口，根据用户提供的始发地Id、目的地Id、下单时间，返回对应的配送时效
     *
     * @param request 查询时效的请求参数
     * @return 查询时效的结果
     */
    @RequestMapping(value = "/delivery_time", method = RequestMethod.POST)
    public DeliveryTimeResponse getPromiseDeliveryTime(@RequestBody DeliveryTimeRequest request) {

        logger.info("request info: {}", request);
        String promisedTime = null;
        String errMsg = null;
        DeliveryTimeResponse response = null;
        try {
            promisedTime = transportService.getPromisedDeliveryTime(request);
            if (promisedTime == null) {
                errMsg = "no results.";
            }
        } catch (DateTimeParseException e) {
            errMsg = "parameter format error";
            logger.error("exception occurs. {}", e.getMessage());
        } catch (SDKException e) {
            errMsg = e.getMessage();
            logger.error("exception occurs. {}", e.getMessage());
        }
        response = new DeliveryTimeResponse(
                errMsg == null ? DeliveryTimeResponse.NORMAL : DeliveryTimeResponse.ABNORMAL, promisedTime, errMsg);
        return response;
    }
}
