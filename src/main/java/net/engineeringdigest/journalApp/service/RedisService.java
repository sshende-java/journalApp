package net.engineeringdigest.journalApp.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class RedisService {

    private RedisTemplate redisTemplate;


    @Autowired      //constructor injection
    public RedisService(@Qualifier("redisTemplate") RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }


    //getting from redis cache
    public <T> T get(String key, Class<T> entityClass) {
        try {
            Object cacheObject = redisTemplate.opsForValue().get(key);      //get json value

            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(cacheObject.toString(), entityClass);   //convert to entity class specified
        } catch (Exception e) {
            log.error("Error occurred during get from redis for key: {}", key, e);
            return null;
        }
    }


    //setting in redis cache
    public void set(String key, Object obj, Long ttl) {     //time-to-live
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            //convert to json for saving to redis
            String jsonValue = objectMapper.writeValueAsString(obj);

            redisTemplate.opsForValue().set(key, jsonValue, ttl, TimeUnit.HOURS);       //key will be expired after 4Hours
        } catch (Exception e) {
            log.error("Error occurred while setting value in redis for key: {}", key, e);
        }
    }


}
