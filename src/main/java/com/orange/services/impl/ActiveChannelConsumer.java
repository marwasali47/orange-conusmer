package com.orange.services.impl;

import com.orange.dtos.CommonResponse;
import com.orange.kafka.requests.Request;
import com.orange.services.ConsumerStrategy;
import com.orange.services.OrangeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Mohamed Gaber on Sep, 2018
 */
@Service("consumerStrategy_ACTIVE")
public class ActiveChannelConsumer implements ConsumerStrategy {


    @Autowired
    private OrangeService orangeService;

    @Override
    public List<CommonResponse> consume(Request request) throws IOException, MessagingException {

        List<CommonResponse> commonResponses = orangeService.getAllMails(request);


        Long fetchToDateTime = getFetchToDate(request);

        if (fetchToDateTime != null && fetchToDateTime != 0L) {
            Date fetchToDate = new Date(fetchToDateTime);

            commonResponses = commonResponses.stream().filter(commonResponse -> {
                Date messageDate = new Date(Long.parseLong(commonResponse.getDate()));
                return messageDate.compareTo(fetchToDate) >= 0;
            }).collect(Collectors.toList());
        }
        return commonResponses;
    }

    private Long getFetchToDate(Request request) {
        Calendar calendar = Calendar.getInstance();
        int days = request.getUserChannel().getFetchValue();
        calendar.add(Calendar.DAY_OF_YEAR, days * -1);
        return calendar.getTimeInMillis();
    }
}
