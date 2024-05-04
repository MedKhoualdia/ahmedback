package com.dance.mo.auth.Service;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
public class RedisService {

    private static final String REDIS_KEY_PREFIX = "authToken:";
    private static final String RESET_TOKEN_PREFIX = "resetToken:";

    private final RedisTemplate<String, String> redisTemplate;

    public RedisService(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void storeToken(String token, String email) {
        System.out.println(token);
        redisTemplate.opsForValue().set(REDIS_KEY_PREFIX + email, token);

    }

    public String FindEmailToken(String token) {
        Set<String> keys = redisTemplate.keys(REDIS_KEY_PREFIX + "*");
        for (String key : keys) {
            String storedToken = redisTemplate.opsForValue().get(key);
            if (token.equals(storedToken)) {
                return key.substring(REDIS_KEY_PREFIX.length());
            }
        }
        return null; // Token not found
    }


    public String getToken(String email) {

        return redisTemplate.opsForValue().get(REDIS_KEY_PREFIX + email);
    }

    public void removeToken(String email) {

        redisTemplate.delete(REDIS_KEY_PREFIX + email);
    }
    ////////////////////////////////
    // Method to store password reset token
    public void storeResetToken(String token, String email) {
        redisTemplate.opsForValue().set(RESET_TOKEN_PREFIX + email, token, 1, TimeUnit.HOURS);
    }

    // Method to find email associated with a reset token
    public String findEmailByResetToken(String token) {
        String email = redisTemplate.opsForValue().get(RESET_TOKEN_PREFIX + token);
        if (email != null) {
            // Extend the expiration time for the token upon access (optional)
            redisTemplate.expire(RESET_TOKEN_PREFIX + token, 1, TimeUnit.HOURS);
        }
        return email;
    }

    // Method to remove password reset token
    public void removeResetToken(String token) {
        redisTemplate.delete(RESET_TOKEN_PREFIX + token);
    }
}
