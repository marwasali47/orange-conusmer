package com.orange.redis.publishers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * Created by mohamed_waleed on 19/11/17.
 */
@Service
public class MessagePublisherService {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;


    public void publish(String topic, String message) {
        redisTemplate.convertAndSend(topic, message);
    }

}
