package net.engineeringdigest.journalApp.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.data.redis.core.RedisTemplate;

@SpringBootTest
public class RedisTests {

    //    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    public RedisTests(@Qualifier("redisTemplate") RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Test
    void testRedisTemplate() {

        redisTemplate.opsForValue().set("email", "saurabh@email.com");  //once set, we can get value of email from redis anywhere in application

        Object email = redisTemplate.opsForValue().get("email");        //to get value from redis
        System.out.println(email);
    }
}
