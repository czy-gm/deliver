package com.jd.transportation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 程序启动类
 *
 * @author czy_gm
 * @version 1.0
 * @since 2021/6/5
 */
@SpringBootApplication
public class TransportationApplication {

    static {
        System.setProperty("Log4jContextSelector", "org.apache.logging.log4j.core.async.AsyncLoggerContextSelector");
        System.setProperty("log4j2AsyncQueueFullPolicy", "Discard");
    }

    public static void main(String[] args) {
        SpringApplication.run(TransportationApplication.class, args);
    }
}
