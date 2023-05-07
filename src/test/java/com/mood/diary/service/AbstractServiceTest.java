package com.mood.diary.service;

import com.mood.diary.service.auth.repository.AuthUserRepository;
import com.mood.diary.service.user.model.AuthUser;
import com.mood.diary.service.util.EntityBuilder;
import org.junit.jupiter.api.AfterEach;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Testcontainers
public abstract class AbstractServiceTest {

    @Autowired
    protected RedissonClient redissonClient;

    @Autowired
    protected AuthUserRepository authUserRepository;

    @AfterEach
    public void clear() {
        authUserRepository.deleteAll();
        redissonClient.getKeys().flushall();
    }

    protected AuthUser initDefaultUser(String username, String email) {
        AuthUser dbUser = EntityBuilder.makeAuthUser(
                username, "pass",
                email, "about", "imageUrl"
        );

        return authUserRepository.save(dbUser);
    }
}
