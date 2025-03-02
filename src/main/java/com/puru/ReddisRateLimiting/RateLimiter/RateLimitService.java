package com.puru.ReddisRateLimiting.RateLimiter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.concurrent.TimeUnit;

@Service
public class RateLimitService {

    // Maximum allowed requests for whitelisted and non-whitelisted users.
    @Value("${rate.limit.max.whitelisted:10}")
    private Integer MAX_REQUEST_WHITELISTED;

    @Value("${rate.limit.max.not_whitelisted:5}")
    private Integer MAX_REQUEST_NOT_WHITELISTED;

    // Time window (in seconds) for the rate limit.
    @Value("${rate.limit.time.window:60}")
    private Integer TIME_WINDOW;

    // Redis template for interacting with Redis.
    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * This method checks if the user has exceeded the rate limit and if not,
     * it records their request in Redis.
     *
     * @param userName   The username of the client making the request.
     * @param whitelisted Whether the user is whitelisted.
     * @return true if the request is allowed, false if the rate limit is exceeded.
     */
    public boolean consumeRequest(String userName, Boolean whitelisted) {
        String key = "rate_limit:" + userName;
        long currentTime = Instant.now().getEpochSecond(); // Current timestamp in seconds.

        // Get the rate limit for the user based on their whitelist status.
        Integer limit = getLimit(whitelisted);

        // Remove entries older than the time window from Redis (clearing expired requests).
        redisTemplate.opsForZSet().removeRangeByScore(key, 0, currentTime - TIME_WINDOW);

        // Count the number of requests in the time window.
        long requestCount = redisTemplate.opsForZSet().size(key);

        // If the user has exceeded the limit, deny the request.
        if (requestCount >= limit) {
            return false; // Rate limit exceeded.
        }

        // Otherwise, add the current request to the Redis set and set an expiration.
        redisTemplate.opsForZSet().add(key, String.valueOf(currentTime), currentTime);
        redisTemplate.expire(key, TIME_WINDOW, TimeUnit.SECONDS); // Set the TTL for the rate limit.

        return true; // Request allowed.
    }

    /**
     * This method returns the rate limit for the user based on whether they are whitelisted or not.
     *
     * @param whiteListed Boolean indicating if the user is whitelisted.
     * @return the max number of allowed requests for the user.
     */
    public Integer getLimit(Boolean whiteListed) {
        if (whiteListed) {
            return MAX_REQUEST_WHITELISTED; // Max requests for whitelisted users.
        } else {
            return MAX_REQUEST_NOT_WHITELISTED; // Max requests for non-whitelisted users.
        }
    }
}
