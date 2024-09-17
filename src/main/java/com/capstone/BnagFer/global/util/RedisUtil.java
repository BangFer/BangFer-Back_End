package com.capstone.BnagFer.global.util;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class RedisUtil {
    private final RedisTemplate<String, Object> redisTemplate;
    private static final String FCM_TOKEN_PREFIX = "fcm:token:";
    private static final long ONE_WEEK_IN_SECONDS = 7 * 24 * 60 * 60; // 일주일을 초로 표현

    public void save(String key, Object val, Long time, TimeUnit timeUnit) {
        redisTemplate.opsForValue().set(key, val, time, timeUnit);
    }

    private void saveWithOneWeekTTL(String key, Object val) {
        redisTemplate.opsForValue().set(key, val, ONE_WEEK_IN_SECONDS, TimeUnit.SECONDS);
    }

    public void saveLikeCount(Long tacticId, Long likeCount) {
        saveWithOneWeekTTL("tactic:" + tacticId + ":likeCount", likeCount.toString());
    }

    public Long getLikeCount(Long tacticId) {
        String likeCountStr = (String) redisTemplate.opsForValue().get("tactic:" + tacticId + ":likeCount");
        return likeCountStr != null ? Long.valueOf(likeCountStr) : null;
    }

    public void boardSaveLikeCount(Long boardId, Long likeCount) {
        saveWithOneWeekTTL("board:" + boardId + ":likeCount", likeCount.toString());
    }

    public Long boardGetLikeCount(Long boardId) {
        String likeCountStr = (String) redisTemplate.opsForValue().get("board:" + boardId + ":likeCount");
        return likeCountStr != null ? Long.valueOf(likeCountStr) : null;
    }

    public void boardSaveCommentCount(Long boardId, Long commentCount) {
        saveWithOneWeekTTL("board:" + boardId + ":commentCount", commentCount.toString());
    }

    public Long boardGetCommentCount(Long boardId) {
        String commentCountStr = (String) redisTemplate.opsForValue().get("board:" + boardId + ":commentCount");
        return commentCountStr != null ? Long.valueOf(commentCountStr) : null;
    }

    public void saveCommentCount(Long tacticId, Long commentCount) {
        saveWithOneWeekTTL("tactic:" + tacticId + ":commentCount", commentCount.toString());
    }

    public Long getCommentCount(Long tacticId) {
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
        String key = FCM_TOKEN_PREFIX + userEmail;
        redisTemplate.opsForValue().set(key, fcmToken, 30, TimeUnit.DAYS);
    }

    public String getFCMToken(String userEmail) {
        String key = FCM_TOKEN_PREFIX + userEmail;
        Object tokenObj = redisTemplate.opsForValue().get(key);
        if (tokenObj != null) {
            return (String) tokenObj;
        } else {
            return null;
        }
    }

    public void removeFCMToken(String userEmail) {
        String key = FCM_TOKEN_PREFIX + userEmail;
        redisTemplate.delete(key);
    }
}