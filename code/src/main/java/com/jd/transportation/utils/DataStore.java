package com.jd.transportation.utils;

import com.jd.transportation.cache.CollectCache;
import com.jd.transportation.cache.DeliverCache;
import com.jd.transportation.cache.TransitCache;
import com.jd.transportation.cache.UpperAddressCache;
import com.jd.transportation.dao.CollectDao;
import com.jd.transportation.dao.DeliverDao;
import com.jd.transportation.dao.TransitDao;
import com.jd.transportation.dao.UpperAddressDao;
import com.jd.transportation.entity.CollectInfo;
import com.jd.transportation.entity.DeliverInfo;
import com.jd.transportation.entity.TransitInfo;
import com.jd.transportation.exception.SDKException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class DataStore {

    private final Logger logger = LoggerFactory.getLogger(DataStore.class);

    //上下级地址映射，key为地址id，value为该地址的上级地址id
    private final Map<Integer, Integer> upperLevelAddressIdMap = new HashMap<>();

    //揽收时效数据，一级key为始发地id，二级key为目的地id，value为揽收时效数据列表
    private final Map<Integer, Map<Integer, List<CollectInfo>>> collectInfoMap = new HashMap<>();

    //中转时效数据，一级key为始发地id，二级key为目的地id，value为中转时效数据列表
    private final Map<Integer, Map<Integer, List<TransitInfo>>> transitInfoMap = new HashMap<>();

    //派送时效数据，key为目的地id，value为派送时效数据列表
    private final Map<Integer, List<DeliverInfo>> deliverInfoMap = new HashMap<>();

    @Value("${data.load_data_to_database}")
    private boolean isLoad;

    @javax.annotation.Resource(name = "collect" + "${cache.name}")
    private CollectCache collectCache;

    @javax.annotation.Resource(name = "deliver" + "${cache.name}")
    private DeliverCache deliverCache;

    @javax.annotation.Resource(name = "transit" + "${cache.name}")
    private TransitCache transitCache;

    @javax.annotation.Resource(name = "upperAddress" + "${cache.name}")
    private UpperAddressCache upperAddressCache;

    @javax.annotation.Resource
    private CollectDao collectDao;

    @javax.annotation.Resource
    private TransitDao transitDao;

    @javax.annotation.Resource
    private DeliverDao deliverDao;

    @javax.annotation.Resource
    private UpperAddressDao upperAddressDao;

    public void load() {
        try {
            logger.info("start load data");

            long start = System.currentTimeMillis();

            Resource resource = new ClassPathResource("application.properties");
            Properties properties = PropertiesLoaderUtils.loadProperties(resource);

            String collectDirPath = properties.getProperty("collectInfoPath");
            String transitDirPath = properties.getProperty("transitInfoPath");
            String deliverDirPath = properties.getProperty("deliverInfoPath");

            logger.info("load properties. collectDirPath:{}, transitDirPath:{}, deliverDirPath:{}",
                    collectDirPath, transitDirPath, deliverDirPath);

            if (StringUtils.isBlank(collectDirPath) || StringUtils.isBlank(transitDirPath) || StringUtils.isBlank(deliverDirPath)) {
                logger.error("incorrect data dir. collectDirPath:{}, transitDirPath:{}, deliverDirPath:{}",
                        collectDirPath, transitDirPath, deliverDirPath);
                System.exit(-1);
            }

            File dirFile = new File(collectDirPath);
            FileUtil.checkDirStatus(dirFile);
            File[] dataFileList = dirFile.listFiles();
            if (dataFileList == null) {
                logger.error("collect data is empty.");
                System.exit(-1);
            }
            for (File filePath : dataFileList) {
                FileUtil.loadCollectInfoFile(filePath.getPath(), collectInfoMap,
                        upperLevelAddressIdMap);
            }

            File transitFile = new File(transitDirPath);
            FileUtil.checkDirStatus(transitFile);
            File[] transitDataFiles = transitFile.listFiles();
            if (transitDataFiles == null) {
                logger.error("transit data is empty.");
                System.exit(-1);
            }
            for (File filePath : transitDataFiles) {
                FileUtil.loadTransitInfoFile(filePath.getPath(), transitInfoMap);
            }

            File deliverFile = new File(deliverDirPath);
            FileUtil.checkDirStatus(deliverFile);
            File[] deliverDataFiles = deliverFile.listFiles();
            if (deliverDataFiles == null) {
                logger.error("deliver data is empty.");
                System.exit(-1);
            }
            for (File file : deliverDataFiles) {
                FileUtil.loadDeliverInfoFile(file.getPath(), deliverInfoMap,
                        upperLevelAddressIdMap);
            }
            System.out.printf("load file elapsed:%.3f\n", (System.currentTimeMillis() - start) / 1000.0);
        } catch (IOException | ParseException e) {
            logger.error("load properties file or data file error.", e);
        } catch (SDKException e) {
            logger.error("incorrect data.errMsg:{}", e.getMessage());
        }
    }

    public void storeDataToDB() throws SDKException {
        if (isLoad) {
            load();
            logger.info("start store all data to database");
            storeCollectDataToDB();
            storeTransitDataToDB();
            storeDeliverDataToDB();
            storeAddressLevelDataToDB();
            logger.info("store all data end");
            isLoad = false;
        }
    }

    private void storeCollectDataToDB() throws SDKException {
        logger.info("start store collect data to database");
        for (Map.Entry<Integer, Map<Integer, List<CollectInfo>>> firstEntry : collectInfoMap.entrySet()) {
            int srcId = firstEntry.getKey();
            for (Map.Entry<Integer, List<CollectInfo>> secondEntry : firstEntry.getValue().entrySet()) {
                int dstId = secondEntry.getKey();
                List<CollectInfo> collectInfoList = secondEntry.getValue();
                //collectCache.saveAll(srcId, dstId, collectInfoList);
                collectDao.saveBatch(collectInfoList);
            }
        }
        logger.info("store collect end");
    }

    private void storeTransitDataToDB() throws SDKException {
        logger.info("start store transit data to database");
        for (Map.Entry<Integer, Map<Integer, List<TransitInfo>>> firstEntry : transitInfoMap.entrySet()) {
            int srcId = firstEntry.getKey();
            for (Map.Entry<Integer, List<TransitInfo>> secondEntry : firstEntry.getValue().entrySet()) {
                int dstId = secondEntry.getKey();
                List<TransitInfo> transitInfoList = secondEntry.getValue();
                //transitCache.saveAll(srcId, dstId, transitInfoList);
                transitDao.saveBatch(transitInfoList);
            }
        }
        logger.info("store transit end");
    }

    private void storeDeliverDataToDB() throws SDKException {
        logger.info("start store deliver data to database");
        for (Map.Entry<Integer, List<DeliverInfo>> entry : deliverInfoMap.entrySet()) {
            List<DeliverInfo> deliverInfoList = entry.getValue();
            //deliverCache.saveAll(entry.getKey(), deliverInfoList);
            deliverDao.saveBatch(deliverInfoList);
        }
        logger.info("store deliver end");
    }

    private void storeAddressLevelDataToDB() {
        logger.info("start store address level to database");
        //upperAddressCache.batchSaveUpperAddress(upperLevelAddressIdMap);
        for (Map.Entry<Integer, Integer> entry : upperLevelAddressIdMap.entrySet()) {
            upperAddressDao.save(entry.getKey(), entry.getValue());
        }
        logger.info("store address level end");
    }

}
