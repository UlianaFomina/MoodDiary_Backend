package com.mood.diary.service.auth.service.email.confirmation;

import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RMapCache;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class EmailConfirmationServiceImpl implements EmailConfirmationService {

    private static final String CONFIRMATION_TOKENS = "confirmation-tokens";
    private final RedissonClient redissonClient;

    @Override
    public boolean findById(String id) {
        return redissonClient.getMapCache(CONFIRMATION_TOKENS).containsKey(id);
    }

    @Override
    public void putConfirmationToken(String id, String token) {
        RMapCache<Object, Object> tokenMap = redissonClient.getMapCache(CONFIRMATION_TOKENS);

        tokenMap.put(id, token, 15, TimeUnit.MINUTES);
    }

    @Override
    public String confirmToken(String token) {
        RMapCache<Object, Object> tokenMap = redissonClient.getMapCache(CONFIRMATION_TOKENS);
        boolean isExists = findById(token);

        if (isExists) {
            tokenMap.remove(token);

            return "You account successfully activated!";
        }

        return "Link expired!";
    }

    @PreDestroy
    void destroyRedisson() {
        redissonClient.shutdown();
    }
}
