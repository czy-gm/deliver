package com.jd.transportation.utils;

import com.csvreader.CsvReader;
import com.jd.transportation.entity.*;
import com.jd.transportation.exception.SDKException;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.jd.transportation.utils.DateUtil.parseLocalTime;
import static com.jd.transportation.utils.DateUtil.parseTimestamp;

/**
 * 文件处理类，处理各种数据加载
 *
 * @author czy_gm
 * @version 1.0
 * @since 2021/6/5
 */
public class FileUtil {

    private static final Logger logger = LoggerFactory.getLogger(FileUtil.class);

    /**
     * 加载揽收时效数据及上下级关系
     *
     * @param filePath               文件路径
     * @param collectInfoMap         存放揽收时效数据
     * @param upperLevelAddressIdMap 存放上下级关系
     * @throws IOException    IOException
     * @throws ParseException ParseException
     * @throws SDKException   SDKException
     */
    public static void loadCollectInfoFile(String filePath, Map<Integer, Map<Integer, List<CollectInfo>>> collectInfoMap,
                                           Map<Integer, Integer> upperLevelAddressIdMap)
            throws IOException, ParseException, SDKException {
        File file = new File(filePath);
        checkFileStatus(file);
        CsvReader reader = new CsvReader(filePath, ',', Charset.forName("gbk"));
        reader.readHeaders();
        while (reader.readRecord()) {
            String[] srcCodeArr = new String[]{
                    reader.get(CollectColumn.SRC_PROVINCE_ID.getInx()),
                    reader.get(CollectColumn.SRC_CITY_ID.getInx()),
                    reader.get(CollectColumn.SRC_COUNTY_ID.getInx()),
                    reader.get(CollectColumn.SRC_TOWN_ID.getInx())};
            String[] dstCodeArr = new String[]{
                    reader.get(CollectColumn.DST_PROVINCE_ID.getInx()),
                    reader.get(CollectColumn.DST_CITY_ID.getInx())};
            //获取合法的最低级地址索引
            int srcCodeInx = getIndexOfMatchAddressId(srcCodeArr);
            int dstCodeInx = getIndexOfMatchAddressId(dstCodeArr);
            //根据索引得到对应地址id
            int srcAddressId = srcCodeInx == -1 ? -1 : parseInt(srcCodeArr[srcCodeInx]);
            int dstAddressId = dstCodeInx == -1 ? -1 : parseInt(dstCodeArr[dstCodeInx]);

            CollectInfo collectInfo = new CollectInfo(
                    srcAddressId, dstAddressId,
                    parseLocalTime(reader.get(CollectColumn.COLLECT_END_TIME.getInx())),
                    parseInt(reader.get(CollectColumn.COLLECT_TIME.getInx())),
                    parseTimestamp(reader.get(CollectColumn.EFFECTIVE_TIME.getInx())),
                    parseTimestamp(reader.get(CollectColumn.EXPIRATION_TIME.getInx())));

            //存放各级地址及上级地址
            for (int i = 0; i <= srcCodeInx; i++) {
                int addressCode = parseInt(srcCodeArr[i]);
                Integer getUpperCode = upperLevelAddressIdMap.getOrDefault(addressCode, null);
                int upperLevelCode = i == 0 ? -1 : parseInt(srcCodeArr[i - 1]);
                if (getUpperCode == null) {
                    upperLevelAddressIdMap.put(addressCode, upperLevelCode);
                } else if (getUpperCode != upperLevelCode) {
                    throw new SDKException("upper code error!");
                }
            }

            Map<Integer, List<CollectInfo>> codeMap = collectInfoMap.getOrDefault(srcAddressId, new HashMap<>());
            List<CollectInfo> collectInfoList = codeMap.getOrDefault(dstAddressId, new ArrayList<>());
            collectInfoList.add(collectInfo);
            codeMap.put(dstAddressId, collectInfoList);
            collectInfoMap.put(srcAddressId, codeMap);
        }
    }

    /**
     * 加载中转时效数据
     *
     * @param filePath       文件路径
     * @param transitInfoMap 存放中转时效数据
     * @throws IOException    IOException
     * @throws ParseException ParseException
     */
    public static void loadTransitInfoFile(String filePath, Map<Integer, Map<Integer, List<TransitInfo>>> transitInfoMap)
            throws IOException, ParseException {
        File file = new File(filePath);
        checkFileStatus(file);
        CsvReader reader = new CsvReader(filePath, ',', Charset.forName("gbk"));
        reader.readHeaders();
        while (reader.readRecord()) {

            Pair<Integer, LocalTime> daysAndTime = DateUtil.parseDaysAndTime(reader.get(TransitColumn.TRANSIT_TIME.getInx()));

            String[] srcAddressCodeArr = new String[]{
                    reader.get(TransitColumn.SRC_PROVINCE_ID.getInx()),
                    reader.get(TransitColumn.SRC_CITY_ID.getInx())};
            String[] dstAddressCodeArr = new String[]{
                    reader.get(TransitColumn.DST_PROVINCE_ID.getInx()),
                    reader.get(TransitColumn.DST_CITY_ID.getInx()),
                    reader.get(TransitColumn.DST_COUNTY_ID.getInx()),
                    reader.get(TransitColumn.DST_TOWN_ID.getInx())};

            int srcCodeInx = getIndexOfMatchAddressId(srcAddressCodeArr);
            int dstCodeInx = getIndexOfMatchAddressId(dstAddressCodeArr);
            int srcAddressCode = srcCodeInx == -1 ? -1 : parseInt(srcAddressCodeArr[srcCodeInx]);
            int dstAddressCode = dstCodeInx == -1 ? -1 : parseInt(dstAddressCodeArr[dstCodeInx]);

            TransitInfo transitInfo = new TransitInfo(
                    srcAddressCode, dstAddressCode,
                    daysAndTime.getKey(), daysAndTime.getValue(),
                    parseTimestamp(reader.get(TransitColumn.EFFECTIVE_TIME.getInx())),
                    parseTimestamp(reader.get(TransitColumn.EXPIRATION_TIME.getInx())));

            Map<Integer, List<TransitInfo>> codeMap = transitInfoMap.getOrDefault(srcAddressCode, new HashMap<>());
            List<TransitInfo> transitInfoList = codeMap.getOrDefault(dstAddressCode, new ArrayList<>());
            transitInfoList.add(transitInfo);
            codeMap.put(dstAddressCode, transitInfoList);
            transitInfoMap.put(srcAddressCode, codeMap);
        }
    }

    /**
     * 获取派送时效数据及上下级关系
     *
     * @param filePath                 文件路径
     * @param deliverInfoMap           存放派送时效数据
     * @param upperLevelAddressCodeMap 存放上下级关系
     * @throws IOException    IOException
     * @throws ParseException ParseException
     * @throws SDKException   SDKException
     */
    public static void loadDeliverInfoFile(String filePath, Map<Integer, List<DeliverInfo>> deliverInfoMap,
                                           Map<Integer, Integer> upperLevelAddressCodeMap)
            throws IOException, ParseException, SDKException {
        File file = new File(filePath);
        checkFileStatus(file);
        CsvReader reader = new CsvReader(filePath, ',', Charset.forName("gbk"));
        reader.readHeaders();
        while (reader.readRecord()) {

            String[] dstCodeArr = new String[]{
                    reader.get(DeliverColumn.DST_PROVINCE_ID.getInx()),
                    reader.get(DeliverColumn.DST_CITY_ID.getInx()),
                    reader.get(DeliverColumn.DST_COUNTY_ID.getInx()),
                    reader.get(DeliverColumn.DST_TOWN_ID.getInx())};
            int dstCodeInx = getIndexOfMatchAddressId(dstCodeArr);
            int dstAddressCode = dstCodeInx == -1 ? -1 : parseInt(dstCodeArr[dstCodeInx]);

            DeliverInfo deliverInfo = new DeliverInfo(
                    dstAddressCode,
                    parseInt(reader.get(DeliverColumn.DELIVER_TIME.getInx())),
                    parseTimestamp(reader.get(DeliverColumn.EFFECTIVE_TIME.getInx())),
                    parseTimestamp(reader.get(DeliverColumn.EXPIRATION_TIME.getInx())));

            for (int i = 0; i <= dstCodeInx; i++) {
                int addressCode = parseInt(dstCodeArr[i]);
                Integer getUpperCode = upperLevelAddressCodeMap.getOrDefault(addressCode, null);
                int upperLevelCode = i == 0 ? -1 : parseInt(dstCodeArr[i - 1]);
                if (getUpperCode == null) {
                    upperLevelAddressCodeMap.put(addressCode, upperLevelCode);
                } else if (getUpperCode != upperLevelCode) {
                    throw new SDKException("upper code error!");
                }
            }

            List<DeliverInfo> deliverInfoList = deliverInfoMap.getOrDefault(dstAddressCode, new ArrayList<>());
            deliverInfoList.add(deliverInfo);
            deliverInfoMap.put(dstAddressCode, deliverInfoList);
        }
    }

    /**
     * 检查文件状态，是否存在，是否为文件
     *
     * @param file 文件
     * @throws FileNotFoundException FileNotFoundException
     */
    public static void checkFileStatus(File file) throws FileNotFoundException {
        if (!file.exists() || !file.isFile()) {
            logger.error("when loading data, {} not found or non-file.", file.getName());
            throw new FileNotFoundException(file.getName() + " not found or non-file");
        }
    }

    /**
     * 检查目录状态，是否存在，是否为目录
     *
     * @param file 文件
     * @throws FileNotFoundException FileNotFoundException
     */
    public static void checkDirStatus(File file) throws FileNotFoundException {
        if (!file.exists() || !file.isDirectory()) {
            logger.error("when loading data, {} not found or non-directory.", file.getName());
            throw new FileNotFoundException(file.getName() + " not found or non-directory");
        }
    }

    /**
     * 解析整数值
     *
     * @param s string
     * @return int
     */
    private static int parseInt(String s) {
        if (StringUtils.isBlank(s)) {
            return -1;
        } else {
            return Integer.parseInt(s);
        }
    }

    /**
     * 获取合法最低级地址id的索引
     *
     * @param args 地址ids
     * @return 索引
     */
    private static int getIndexOfMatchAddressId(String... args) {

        for (int i = args.length - 1; i >= 0; i--) {
            if (StringUtils.isNotBlank(args[i])) {
                return i;
            }
        }
        return -1;
    }
}
