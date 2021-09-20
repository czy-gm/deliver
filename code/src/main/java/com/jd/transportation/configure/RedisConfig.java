package com.jd.transportation.configure;

import com.jd.transportation.entity.CollectInfo;
import com.jd.transportation.entity.DeliverInfo;
import com.jd.transportation.entity.TransitInfo;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    @Bean
    @ConditionalOnMissingBean(StringRedisTemplate.class)
    public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory factory) {
        StringRedisTemplate stringRedisTemplate = new StringRedisTemplate();
        stringRedisTemplate.setConnectionFactory(factory);
        return stringRedisTemplate;
    }

    @Bean(name = "collectRedisTemplate")
    @ConditionalOnMissingBean(name = "collectRedisTemplate")
    public RedisTemplate<String, CollectInfo> collectRedisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, CollectInfo> template = new RedisTemplate<>();
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new Jackson2JsonRedisSerializer<>(CollectInfo.class));
        template.setConnectionFactory(factory);
        return template;
    }

    @Bean(name = "transitRedisTemplate")
    @ConditionalOnMissingBean(name = "transitRedisTemplate")
    public RedisTemplate<String, TransitInfo> transitRedisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, TransitInfo> template = new RedisTemplate<>();
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new Jackson2JsonRedisSerializer<>(TransitInfo.class));
        template.setConnectionFactory(factory);
        return template;
    }

    @Bean(name = "deliverRedisTemplate")
    @ConditionalOnMissingBean(name = "deliverRedisTemplate")
    public RedisTemplate<String, DeliverInfo> deliverRedisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, DeliverInfo> template = new RedisTemplate<>();
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new Jackson2JsonRedisSerializer<>(DeliverInfo.class));
        template.setConnectionFactory(factory);
        return template;
    }

}
