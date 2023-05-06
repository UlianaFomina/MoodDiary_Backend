package com.mood.diary.service.config;

import org.modelmapper.ModelMapper;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

@Configuration
public class GlobalConfiguration {

    @Value("${redis.uri}")
    private String redisUrl;

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public RedissonClient redissonClient() {
        Config config = new Config();
        config.useSingleServer()
                .setAddress(redisUrl);

        return Redisson.create(config);
    }

    @Bean(name = "mvcHandlerMappingIntrospector")
    public HandlerMappingIntrospector mvcHandlerMappingIntrospector() {
        return new HandlerMappingIntrospector();
    }
}
