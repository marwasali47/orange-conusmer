package com.orange.config;

import com.orange.redis.subscribers.RedisMessageSubscriber;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.StringRedisSerializer;


/**
 * Created by mohamed_waleed on 12/11/17.
 */
@Configuration
public class RedisConfig {

    private Logger logger = LogManager.getLogger(RedisConfig.class);

    @Value("${eventbus.redis.host}")
    private String redisHost;

    @Value("${eventbus.redis.port}")
    private Integer redisPort;

    @Bean
    public JedisConnectionFactory jedisConnectionFactory() {
        logger.debug(redisHost);
        JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory();
        jedisConnectionFactory.setHostName(redisHost);
        jedisConnectionFactory.setPort(redisPort);
        return jedisConnectionFactory;
    }

    @Bean
    public RedisTemplate<String, String> redisTemplate() {
        RedisTemplate<String, String> template = new RedisTemplate<>();
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new StringRedisSerializer());
        template.setConnectionFactory(jedisConnectionFactory());
        return template;
    }

    @Bean
    public RedisMessageListenerContainer redisContainer( MessageListenerAdapter messageListenerAdapter) {
        RedisMessageListenerContainer container
                = new RedisMessageListenerContainer();
        container.setConnectionFactory(jedisConnectionFactory());
        container.addMessageListener(messageListenerAdapter, topic());
        return container;
    }

    @Bean
    public MessageListenerAdapter messageListener(@Lazy RedisMessageSubscriber redisMessageSubscriber) {
        return new MessageListenerAdapter(redisMessageSubscriber);
    }

    @Bean
    public MessageListenerAdapter messageListener() {
        return new MessageListenerAdapter();
    }

    @Bean
    public ChannelTopic topic() {
        return new ChannelTopic("orange-consumer");
    }
}
