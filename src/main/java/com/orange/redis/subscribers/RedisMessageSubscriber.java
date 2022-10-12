package com.orange.redis.subscribers;

import com.google.gson.Gson;
import com.orange.redis.messsages.RedisMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Service;

/**
 * Created by mohamed_waleed on 19/11/17.
 */
@Service
public class RedisMessageSubscriber implements MessageListener {

    private Logger logger = LogManager.getLogger(RedisMessageSubscriber.class);


    public void onMessage(Message message, byte[] pattern) {

        logger.debug("Redis_Subscriber-Thread_Id: {}", Thread.currentThread().getId());

        RedisMessage parsedMessage = new Gson().fromJson(new String(message.getBody()), RedisMessage.class);

        ThreadContext.put("trace-id", parsedMessage.getTraceId());
        ThreadContext.put("user-token", parsedMessage.getUserToken());
        logger.debug("Receive redis message on topic {} with message from sender-> {}", parsedMessage.getReceiverTopic(), parsedMessage.getSenderTopic());

        RedisMessage redisMessage = new RedisMessage();
        redisMessage.setTraceId(ThreadContext.get("trace-id"));
        redisMessage.setUserToken(ThreadContext.get("user-token"));

    }
}