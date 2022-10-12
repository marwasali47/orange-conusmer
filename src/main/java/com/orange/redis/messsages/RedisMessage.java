package com.orange.redis.messsages;

import java.util.Map;

public class RedisMessage implements Message {
    private String functionName;

    private String senderTopic;

    private String response;

    private String receiverTopic;

    private Map<String, String> parameters;

    private String traceId;

    private String userToken;

    public String getFunctionName() {
        return functionName;
    }

    public void setFunctionName(String functionName) {
        this.functionName = functionName;
    }

    public String getSenderTopic() {
        return senderTopic;
    }

    public void setSenderTopic(String senderTopic) {
        this.senderTopic = senderTopic;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getReceiverTopic() {
        return receiverTopic;
    }

    public void setReceiverTopic(String receiverTopic) {
        this.receiverTopic = receiverTopic;
    }

    public Map<String, String> getParameters() {
        return parameters;
    }

    public void setParameters(Map<String, String> parameters) {
        this.parameters = parameters;
    }

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    @Override
    public String toString() {
        return "RedisMessage{" +
                "functionName='" + functionName + '\'' +
                ", senderTopic='" + senderTopic + '\'' +
                ", response='" + response + '\'' +
                ", receiverTopic='" + receiverTopic + '\'' +
                ", parameters=" + parameters +
                ", traceId='" + traceId + '\'' +
                ", userToken='" + userToken + '\'' +
                '}';
    }
}
