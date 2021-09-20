package com.jd.transportation.configure;

import com.jd.transportation.utils.DataStore;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("com.jd.transportation.dao")
public class DataConfig {

    @Bean(initMethod = "storeDataToDB")
    public DataStore dataStore() {
        return new DataStore();
    }
}
