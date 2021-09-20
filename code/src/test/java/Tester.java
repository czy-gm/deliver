//import com.jd.transportation.TransportationApplication;
//import com.jd.transportation.dao.CollectDao;
//import com.jd.transportation.dao.DeliverDao;
//import com.jd.transportation.dao.TransitDao;
//import com.jd.transportation.dao.UpperAddressDao;
//import com.jd.transportation.entity.CollectInfo;
//import com.jd.transportation.entity.DeliverInfo;
//import com.jd.transportation.entity.TransitInfo;
//import org.junit.jupiter.api.Test;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import javax.annotation.Resource;
//import java.time.LocalTime;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//@SpringBootTest(classes = TransportationApplication.class)
//public class Tester {
//
//    @Resource
//    private CollectDao collectDao;
//
//    @Resource
//    private TransitDao transitDao;
//
//    @Resource
//    private DeliverDao deliverDao;
//
//    @Resource
//    private UpperAddressDao upperAddressDao;
//
//    @Test
//    public void colSave() {
//        CollectInfo collectInfo = new CollectInfo();
//        collectInfo.setSrcId(1);
//        collectInfo.setDstId(2);
//        collectInfo.setCollectTime(12);
//        collectInfo.setCollectEndTime(LocalTime.NOON);
//        collectInfo.setEffectiveTime(123);
//        collectInfo.setExpirationTime(234);
//        collectDao.save(collectInfo);
//    }
//
//    @Test
//    public void colSaveBatch() {
//        CollectInfo collectInfo = new CollectInfo();
//        collectInfo.setSrcId(1);
//        collectInfo.setDstId(2);
//        collectInfo.setCollectTime(12);
//        collectInfo.setCollectEndTime(LocalTime.NOON);
//        collectInfo.setEffectiveTime(123);
//        collectInfo.setExpirationTime(234);
//
//        CollectInfo collectInfo1 = new CollectInfo();
//        collectInfo1.setSrcId(1);
//        collectInfo1.setDstId(2);
//        collectInfo1.setCollectTime(12);
//        collectInfo1.setCollectEndTime(LocalTime.NOON);
//        collectInfo1.setEffectiveTime(123);
//        collectInfo1.setExpirationTime(234);
//
//        List<CollectInfo> collectInfoList = new ArrayList<>();
//        collectInfoList.add(collectInfo);
//        collectInfoList.add(collectInfo1);
//
//        collectDao.saveBatch(collectInfoList);
//    }
//
//    @Test
//    public void colGet() {
//        List<CollectInfo> collectInfos = collectDao.getBySrcIdAndDstId(1, 2);
//        for (CollectInfo collectInfo : collectInfos) {
//            System.out.println(collectInfo);
//        }
//    }
//
//    @Test
//    public void transSave() {
//        TransitInfo transitInfo = new TransitInfo();
//        transitInfo.setSrcId(12);
//        transitInfo.setDstId(234);
//        transitInfo.setTransitDays(2);
//        transitInfo.setTransitTime(LocalTime.NOON);
//        transitInfo.setEffectiveTime(123);
//        transitInfo.setExpirationTime(234);
//        transitDao.save(transitInfo);
//    }
//
//    @Test
//    public void transSaveBatch() {
//        TransitInfo transitInfo = new TransitInfo();
//        transitInfo.setSrcId(12);
//        transitInfo.setDstId(234);
//        transitInfo.setTransitDays(2);
//        transitInfo.setTransitTime(LocalTime.NOON);
//        transitInfo.setEffectiveTime(123);
//        transitInfo.setExpirationTime(234);
//
//        TransitInfo transitInfo1 = new TransitInfo();
//        transitInfo1.setSrcId(123);
//        transitInfo1.setDstId(2344);
//        transitInfo1.setTransitDays(3);
//        transitInfo1.setTransitTime(LocalTime.NOON);
//        transitInfo1.setEffectiveTime(123);
//        transitInfo1.setExpirationTime(234);
//
//        List<TransitInfo> transitInfoList = new ArrayList<>();
//        transitInfoList.add(transitInfo);
//        transitInfoList.add(transitInfo1);
//
//        transitDao.saveBatch(transitInfoList);
//    }
//
//    @Test
//    public void transGet() {
//        List<TransitInfo> transitInfoList = transitDao.getBySrcIdAndDstId(12, 234);
//        for (TransitInfo transitInfo : transitInfoList) {
//            System.out.println(transitInfo);
//        }
//    }
//
//    @Test
//    public void deliverSave() {
//        DeliverInfo deliverInfo = new DeliverInfo();
//        deliverInfo.setDstId(234);
//        deliverInfo.setDeliverTime(124);
//        deliverInfo.setEffectiveTime(123);
//        deliverInfo.setExpirationTime(234);
//        deliverDao.save(deliverInfo);
//    }
//
//    @Test
//    public void deliverSaveBatch() {
//        DeliverInfo deliverInfo = new DeliverInfo();
//        deliverInfo.setDstId(234);
//        deliverInfo.setDeliverTime(124);
//        deliverInfo.setEffectiveTime(123);
//        deliverInfo.setExpirationTime(234);
//
//        DeliverInfo deliverInfo1 = new DeliverInfo();
//        deliverInfo1.setDstId(2344);
//        deliverInfo1.setDeliverTime(124);
//        deliverInfo1.setEffectiveTime(123);
//        deliverInfo1.setExpirationTime(234);
//
//        List<DeliverInfo> deliverInfoList = new ArrayList<>();
//        deliverInfoList.add(deliverInfo);
//        deliverInfoList.add(deliverInfo1);
//        deliverDao.saveBatch(deliverInfoList);
//    }
//
//    @Test
//    public void deliverGet() {
//        List<DeliverInfo> deliverInfoList = deliverDao.getByDstId(234);
//        for (DeliverInfo deliverInfo : deliverInfoList) {
//            System.out.println(deliverInfo);
//        }
//    }
//
//    @Test
//    public void upperAddressSave() {
//        upperAddressDao.save(123, 345);
//    }
//
//    @Test
//    public void upperAddressGet() {
//        Integer upperId = upperAddressDao.getUpperLevelAddressId(123);
//        System.out.println(upperId);
//    }
//
//}
