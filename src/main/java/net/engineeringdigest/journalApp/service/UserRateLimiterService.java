package net.engineeringdigest.journalApp.service;


import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class UserRateLimiterService {

    private final RateLimiterConfig config;
    private final ConcurrentHashMap<String, RateLimiter> userLimitersMap = new ConcurrentHashMap<>();

    @Autowired
    public UserRateLimiterService() {

        //RateLimiter config here
        this.config = RateLimiterConfig.custom()
                .limitForPeriod(5)                          // 5 requests
                .limitRefreshPeriod(Duration.ofSeconds(10)) // per 10 seconds
                .timeoutDuration(Duration.ZERO)             // no waiting: fail immediately
                .build();

    }

    /*
    *   computeIfAbsent checks if a RateLimiter exists for this user
    *   If it does not exist, it creates a new one using the config      (id is the same as userId in this case.)
    *   Ensures each user gets their own limiter instance
    *   Then..
    *   Tries to acquire a permit
    *   If the user has available quota (hasn't hit the 5-requests-in-10-seconds limit), it returns true
    *   Otherwise false immediately
    */
    public boolean tryConsume(String userId) {
        RateLimiter limiter = userLimitersMap.computeIfAbsent(userId,
                id -> RateLimiter.of(id, config));

        // This will try to acquire one permit, waiting up to the timeoutDuration specified in the config.
        return limiter.acquirePermission();
    }

}

