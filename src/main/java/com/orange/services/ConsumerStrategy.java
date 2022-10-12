package com.orange.services;

import com.orange.dtos.CommonResponse;
import com.orange.exceptions.OrangeException;
import com.orange.kafka.requests.Request;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.List;

public interface ConsumerStrategy {
    List<CommonResponse> consume(Request request) throws OrangeException, IOException, MessagingException;
}
