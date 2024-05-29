package com.capstone.BnagFer.global.util;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class RedisUtil {

    private final RedisTemplate<String, Object> redisTemplate;

    public void save(String key, Object val, Long time, TimeUnit timeUnit) {
        redisTemplate.opsForValue().set(key, val, time, timeUnit);
    }

    public void saveLikeCount(Long tacticId, Long likeCount) {
        redisTemplate.opsForValue().set("tactic:" + tacticId + ":likeCount", likeCount.toString());
    }

    public void saveCommentCount(Long tacticId, Long commentCount){
        redisTemplate.opsForValue().set("tactic:" + tacticId + ":commentCount", commentCount.toString());
    }

    public Long getLikeCount(Long tacticId) {
        String likeCountStr = (String) redisTemplate.opsForValue().get("tactic:" + tacticId + ":likeCount");
        return likeCountStr != null ? Long.valueOf(likeCountStr) : null;
    }

    public Long getCommentCount(Long tacticId){
        String commentCountStr = (String) redisTemplate.opsForValue().get("tactic:" + tacticId + ":commentCount");
        return commentCountStr != null ? Long.valueOf(commentCountStr) : null;
    }

    public boolean hasKey(String key) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }

    public Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public boolean delete(String key) {
        return Boolean.TRUE.equals(redisTemplate.delete(key));
    }

    public void saveFCMToken(String userEmail, String fcmToken) {
        redisTemplate.opsForValue().set(userEmail, fcmToken);
        redisTemplate.expire(userEmail, 30, TimeUnit.DAYS);
    }

    public String getFCMToken(String userEmail) {
        Object tokenObj = redisTemplate.opsForValue().get(userEmail);
        if (tokenObj != null) {
            return (String) tokenObj;
        } else {
            return null;
        }
    }

    public void removeFCMToken(String userEmail) {
        redisTemplate.delete(userEmail);
    }
}

