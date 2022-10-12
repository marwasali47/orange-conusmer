package com.orange.services.impl;

import com.google.gson.Gson;
import com.orange.dtos.CommonResponse;
import com.orange.enums.FirstLoadStatus;
import com.orange.exceptions.OrangeException;
import com.orange.kafka.requests.Request;
import com.orange.orchestration.Orchestrator;
import com.orange.redis.messsages.RedisMessage;
import com.orange.services.ConsumerStrategy;
import com.orange.services.OrangeService;
import io.vertx.kafka.client.producer.KafkaWriteStream;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by Mohamed Gaber on Sep, 2018
 */

@Service("consumerStrategy_NEW")
public class FirstTimeChannelConsumer implements ConsumerStrategy {

    private Logger logger = LogManager.getLogger(FirstTimeChannelConsumer.class);

    @Autowired
    private OrangeService orangeService;

    @Autowired
    private KafkaWriteStream<String, String> kafkaWriteStream;

    @Value("${kafka.consumer.topic}")
    private String orangeTopic;

    @Autowired
    private Orchestrator orchestrator;

    @Override
    public List<CommonResponse> consume(Request request) throws OrangeException, IOException, MessagingException {

        sendFirstLoadStatusToUserChannel(request);

        List<CommonResponse> commonResponses = orangeService.getAllMails(request);

        if(commonResponses == null || commonResponses.size() == 0){
            logger.debug("No messages are returned");
            throw new OrangeException("No messages are returned");
        }

        Date fetchToDate = new Date(request.getUserChannel().getFirstLoadFetchValue());

        boolean firstLoadFetchCompleted = commonResponses.stream().anyMatch(commonResponse -> {
            Date messageDate = new Date(Long.parseLong(commonResponse.getDate()));
            return messageDate.compareTo(fetchToDate) < 0;
        });

        List<CommonResponse> filteredCommonResponses = commonResponses.stream().filter(commonResponse -> {
            Date messageDate = new Date(Long.parseLong(commonResponse.getDate()));
            return messageDate.compareTo(fetchToDate) >= 0;
        }).collect(Collectors.toList());


        if(!firstLoadFetchCompleted){
            Integer nextPageIndex = Integer.parseInt(request.getPageIndex()) + 1;
            request.setPageIndex("" + nextPageIndex);
            sendToOrangeTopic(request);
        }else {
            logger.debug("Some messages date are now before or equal to fetchToDate");
        }

        return filteredCommonResponses;
    }

    private void sendToOrangeTopic(Request request) {
        String key = request.getUserChannel().getUsername() + "-" + request.getUserChannel().getId();
        ProducerRecord<String, String> producerRecord = new ProducerRecord<>(orangeTopic, key, new Gson().toJson(request));
        kafkaWriteStream.write(producerRecord);
    }

    private void sendFirstLoadStatusToUserChannel(Request request) {
        logger.debug("Send to user channel to update first load status to ACTIVE");
        RedisMessage redisMessage = new RedisMessage();
        redisMessage.setSenderTopic("orange-consumer");
        redisMessage.setFunctionName("updateFirstLoadStatus");
        redisMessage.setReceiverTopic("user-channel");
        Map<String, String> params = new HashMap<>();
        params.put("firstLoadStatus", FirstLoadStatus.ACTIVE.toString());
        params.put("userChannelId", "" + request.getUserChannel().getId());
        redisMessage.setParameters(params);
        orchestrator.call(redisMessage);
    }
}
