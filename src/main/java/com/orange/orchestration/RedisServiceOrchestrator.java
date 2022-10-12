package com.orange.orchestration;

import com.google.gson.Gson;
import com.orange.redis.messsages.Message;
import com.orange.redis.messsages.RedisMessage;
import com.orange.redis.publishers.MessagePublisherService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Service;

import java.util.function.Consumer;

/**
 * Created by mohamed_waleed on 20/11/17.
 */
@Service
public class RedisServiceOrchestrator implements Orchestrator {

    private Logger logger = LogManager.getLogger(RedisServiceOrchestrator.class);

    @Autowired
    private MessagePublisherService messagePublisherService;

    @Autowired
    private RedisMessageListenerContainer redisMessageListenerContainer;


    @Override
    public void call(Message message) {
        RedisMessage redisMessage = (RedisMessage)message;
        String topic = redisMessage.getReceiverTopic();
        redisMessage.setTraceId(ThreadContext.get("trace-id"));
        redisMessage.setUserToken(ThreadContext.get("user-token"));
        logger.debug("Send redis message on topic {} with message function name -> {}", redisMessage.getReceiverTopic(), redisMessage.getReceiverTopic());
        messagePublisherService.publish(topic, new Gson().toJson(redisMessage));
    }

    @Override
    public void receive(Consumer<Message> callback) {
        MessageListener messageListener = new MessageListener() {
            @Override
            public void onMessage(org.springframework.data.redis.connection.Message message, byte[] bytes) {
                RedisMessage parsedMessage = new Gson().fromJson(new String(message.getBody()), RedisMessage.class);
                ThreadContext.put("trace-id", parsedMessage.getTraceId());
                ThreadContext.put("user-token", parsedMessage.getUserToken());
                logger.debug("Receive redis message from sender -> {}", parsedMessage.getSenderTopic());
                callback.accept(parsedMessage);
                redisMessageListenerContainer.removeMessageListener(this);
            }

        };
        redisMessageListenerContainer.addMessageListener(messageListener, new ChannelTopic("user-channel"));

    }

    @Override
    public void receive(String topic ,Consumer<Message> callback) {
        MessageListener messageListener = new MessageListener() {
            @Override
            public void onMessage(org.springframework.data.redis.connection.Message message, byte[] bytes) {
                RedisMessage parsedMessage = new Gson().fromJson(new String(message.getBody()), RedisMessage.class);
                ThreadContext.put("trace-id", parsedMessage.getTraceId());
                ThreadContext.put("user-token", parsedMessage.getUserToken());
                logger.debug("Receive redis message from sender-> {}", parsedMessage.getSenderTopic());
                callback.accept(parsedMessage);
                redisMessageListenerContainer.removeMessageListener(this);
            }

        };
        redisMessageListenerContainer.addMessageListener(messageListener, new ChannelTopic(topic));

    }
}
