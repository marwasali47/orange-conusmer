package com.orange.config;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.kafka.client.producer.KafkaWriteStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by Mohamed Gaber on Jul, 2018
 */

@Configuration
public class KafkaProducerConfig {

    @Value("${kafka.bootstrap-servers}")
    private String bootStrapServers;

    @Bean
    public KafkaWriteStream kafkaWriteStream(@Autowired Vertx vertx) {
        JsonObject producerConfig = new JsonObject();
        producerConfig.put("bootstrap.servers", bootStrapServers);
        producerConfig.put("max.request.size", 209715200);
        return KafkaWriteStream.create(vertx, producerConfig.getMap(), String.class, String.class);
    }
}
