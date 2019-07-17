package zxframe.util;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import zxframe.cache.redis.RedisCacheManager;
import zxframe.config.ZxFrameConfig;

import java.util.concurrent.CountDownLatch;

/**
 *  test {@link zxframe.util.DistributedLocks}
 * @author nero520
 */
public class DistributedLocksTests {

    private static Logger logger = LoggerFactory.getLogger(DistributedLocksTests.class);


    DistributedLocks distributedLocks;

    static int num = 20;

    @Before
    public void init(){
        ZxFrameConfig.ropen = true;
        ZxFrameConfig.rClusters = "127.0.0.1:7000,127.0.0.1:7001,127.0.0.1:7002,127.0.0.1:7003,127.0.0.1:7004,127.0.0.1:7005";
        //redis load
        RedisCacheManager.init();
        distributedLocks = new DistributedLocks();
    }

    @Test
    public void distributedLocksTest(){
        logger.info("distributedLocksTest() test start *************************！");
        final String key = "distributedLocksTestKey";
        final int ms = 20000;
        int count=30;//30 threads gain 20 datas
        final CountDownLatch cdl=new CountDownLatch(count);
        for (int i = 0; i < count; i++) {
            new Thread(new Runnable() {
                public void run() {
                    cdl.countDown();
                    try {
                        cdl.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    try {
                        String s = Thread.currentThread().getName() + "=====================";
                        logger.info(s + "enter the distributedLocks" );
                        distributedLocks.mustGetLock(key,ms );

                        if (num > 0) {
                            logger.info(s + "get data success,data :" + num);
                            num--;
                        } else {
                            logger.info(s + "get data fail, data is null");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }finally {
                        distributedLocks.unLock(key);
                    }
                }
            }).start();
        }
        try {
            Thread.sleep(50000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        logger.info("distributedLocksTest() test end *************************！");
    }


}
