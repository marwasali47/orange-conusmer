package com.orange.services;

import com.orange.dtos.CommonResponse;
import com.orange.kafka.requests.Request;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.List;

/**
 * Created by Mohamed Gaber on Aug, 2018
 */
public interface OrangeService {

    List<CommonResponse> getAllMails(Request request) throws MessagingException, IOException;
}
