package com.mood.diary.service.auth.service.email;

import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RMapCache;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class EmailConfirmationServiceImpl implements EmailConfirmationService {

    private final RedissonClient redissonClient;

    @Override
    public boolean findById(String id) {
        return redissonClient.getMapCache("confirmation-tokens").containsKey(id);
    }

    @Override
    public void putConfirmationToken(String id, String token) {
        RMapCache<Object, Object> tokenMap = redissonClient.getMapCache("confirmation-tokens");

        tokenMap.put(id, token, 15, TimeUnit.MINUTES);
    }

    @Override
    public String confirmToken(String token) {
        RMapCache<Object, Object> tokenMap = redissonClient.getMapCache("confirmation-tokens");
        boolean isExists = findById(token);

        if (isExists) {
            tokenMap.remove(token);

            return "You account successfully activated!";
        }

        return "Your account not activated!";
    }

    @PreDestroy
    void destroyRedisson() {
        redissonClient.shutdown();
    }
}
