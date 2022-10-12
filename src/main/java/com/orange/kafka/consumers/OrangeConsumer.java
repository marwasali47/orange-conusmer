package com.orange.kafka.consumers;

import com.google.gson.Gson;
import com.orange.dtos.CommonResponse;
import com.orange.dtos.MessagesResponseDto;
import com.orange.exceptions.OrangeException;
import com.orange.kafka.requests.Request;
import com.orange.orchestration.Orchestrator;
import com.orange.services.ConsumerStrategy;
import com.orange.services.OrangeService;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.kafka.client.consumer.KafkaReadStream;
import io.vertx.kafka.client.producer.KafkaWriteStream;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.mail.MessagingException;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

/**
 * Created by Mohamed Gaber on Aug, 2018
 */

@Component
public class OrangeConsumer {

    private Logger logger = LogManager.getLogger(OrangeConsumer.class);

    @Value("${kafka.bootstrap-servers}")
    private String bootStrapServers;

    @Value("${kafka.consumer.group.id}")
    private String consumerGroupId;

    @Value("${kafka.consumer.topic}")
    private String activeStatusTopic;

    @Value("${kafka.consumer.new-status-topic}")
    private String newStatusTopic;

    @Value("${kafka.producer.ingestor.topic}")
    private String ingestorTopic;

    @Autowired
    private Vertx vertx;

    @Autowired
    private OrangeService orangeService;

    @Autowired
    private KafkaWriteStream<String, String> kafkaWriteStream;

    @Autowired
    private Orchestrator orchestrator;

    @Autowired
    private ApplicationContext applicationContext;

    @PostConstruct
    public void startConsumer(){

        JsonObject consumerConfig = getConsumerConfig();

        // Active status activeStatusConsumer
        KafkaReadStream<String, String> activeStatusConsumer = KafkaReadStream.create(vertx, consumerConfig.getMap());
        activeStatusConsumer.handler(record -> processKafkaMessage(record));
        activeStatusConsumer.subscribe(Collections.singleton(activeStatusTopic));

        // New status activeStatusConsumer
        KafkaReadStream<String, String> newStatusConsumer = KafkaReadStream.create(vertx, consumerConfig.getMap());
        newStatusConsumer.handler(record -> processKafkaMessage(record));
        newStatusConsumer.subscribe(Collections.singleton(newStatusTopic));
    }

    private void processKafkaMessage(ConsumerRecord<String, String> record) {

        vertx.executeBlocking(future -> {

            logger.debug(Thread.currentThread().getName());

            String response = record.value();
            Gson gson = new Gson();
            Request request = gson.fromJson(response, Request.class);

            logger.debug(request);

            try {

                ConsumerStrategy consumerStrategy = (ConsumerStrategy)applicationContext
                        .getBean("consumerStrategy_" + request.getUserChannel().getFirstLoadStatus().toString());

                List<CommonResponse> mailList = consumerStrategy.consume(request);
                MessagesResponseDto messagesResponseDto = new MessagesResponseDto();
                messagesResponseDto.setUsername(request.getUserChannel().getUsername());
                messagesResponseDto.setRetentionValue(request.getUserChannel().getRetentionValue());
                messagesResponseDto.setMessages(mailList);
                String key = request.getUserChannel().getUsername() + "-" + request.getUserChannel().getId();
                String messagesResponseStr = new Gson().toJson(messagesResponseDto);
                ProducerRecord<String, String> producerRecord = new ProducerRecord<>(ingestorTopic, key, messagesResponseStr);
                kafkaWriteStream.write(producerRecord);
                logger.debug("{} mails are sent to Ingestor", mailList.size());
                future.complete();

            } catch (IOException | OrangeException | MessagingException e ) {
                logger.error(e);
                e.printStackTrace();
                kafkaWriteStream.write(new ProducerRecord<>(record.topic(), record.key(), gson.toJson(request)));
            }
        }, asyncResult -> {

        });
    }


    private JsonObject getConsumerConfig() {
        JsonObject consumerConfig = new JsonObject();
        consumerConfig.put("bootstrap.servers", bootStrapServers);
        consumerConfig.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        consumerConfig.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        consumerConfig.put("group.id", consumerGroupId);
        consumerConfig.put("enable.auto.commit", "true");
        return consumerConfig;
    }
}
