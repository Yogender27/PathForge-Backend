package com.pathforge.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class RedisServices {

    @Autowired
    private StringRedisTemplate redisTemplate;

    private static final long OTP_EXPIRATION_MINUTES = 1;

    public void saveOtp(String email, String otp) {
        String key = buildOtpKey(email);
        redisTemplate.opsForValue().set(key, otp, Duration.ofMinutes(OTP_EXPIRATION_MINUTES));
    }

    public String getOtp(String email) {
        return redisTemplate.opsForValue().get(buildOtpKey(email));
    }

    public void deleteOtp(String email) {
        redisTemplate.delete(buildOtpKey(email));
    }

    private String buildOtpKey(String email) {
        return "OTP:" + email;
    }
}

