package com.orange.orchestration;

import com.orange.redis.messsages.Message;

import java.util.function.Consumer;

/**
 * Created by mohamed_waleed on 20/11/17.
 */
public interface Orchestrator {
    void call(Message message);
    void receive(Consumer<Message> callback);
    void receive(String topic, Consumer<Message> callback);
}
