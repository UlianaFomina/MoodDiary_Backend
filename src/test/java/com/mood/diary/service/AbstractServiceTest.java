package com.mood.diary.service;

import com.mood.diary.service.auth.repository.AuthUserRepository;
import org.junit.jupiter.api.AfterEach;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Testcontainers
public abstract class AbstractServiceTest {

    @MockBean
    RedissonClient redissonClient;

    @Autowired
    protected AuthUserRepository authUserRepository;

    @Container
    private static final MongoDBContainer container = new MongoDBContainer("mongo:4.0.10");

    @DynamicPropertySource
    public static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", container::getReplicaSetUrl);
    }

    @AfterEach
    public void clear() {
        authUserRepository.deleteAll();
    }
}
