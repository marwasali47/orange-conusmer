package com.orange.dtos;

import java.util.List;


public class MessagesResponseDto {
    private String username;
    private String lang;
    private String nextPageIndex;
    private int retentionValue;

    public int getRetentionValue() {
        return retentionValue;
    }

    public void setRetentionValue(int retentionValue) {
        this.retentionValue = retentionValue;
    }

    private List<CommonResponse> messages;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getNextPageIndex() {
        return nextPageIndex;
    }

    public void setNextPageIndex(String nextPageIndex) {
        this.nextPageIndex = nextPageIndex;
    }

    public List<CommonResponse> getMessages() {
        return messages;
    }

    public void setMessages(List<CommonResponse> messages) {
        this.messages = messages;
    }

    @Override
    public String toString() {
        return "MessagesResponseDto{" +
                "username='" + username + '\'' +
                ", lang='" + lang + '\'' +
                ", nextPageIndex='" + nextPageIndex + '\'' +
                ", messages=" + messages +
                '}';
    }
}
