package com.orange.kafka.requests;

import com.orange.entities.Channel;
import com.orange.entities.UserChannel;

import java.time.LocalDate;

/**
 * Created by mohamed_waleed on 11/10/17.
 */

public class Request {

    private Long id;

    private String userToken;

    private LocalDate date;

    private String type;

    private String firebaseToken;

    private String pageIndex;

    private int itemsPerPage;

    private UserChannel userChannel;

    private Channel channel;

    private String lang;

    private String traceId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFirebaseToken() {
        return firebaseToken;
    }

    public void setFirebaseToken(String firebaseToken) {
        this.firebaseToken = firebaseToken;
    }

    public String getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(String pageIndex) {
        this.pageIndex = pageIndex;
    }

    public int getItemsPerPage() {
        return itemsPerPage;
    }

    public void setItemsPerPage(int itemsPerPage) {
        this.itemsPerPage = itemsPerPage;
    }

    public UserChannel getUserChannel() {
        return userChannel;
    }

    public void setUserChannel(UserChannel userChannel) {
        this.userChannel = userChannel;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    @Override
    public String toString() {
        return "Request{" +
                "id=" + id +
                ", userToken='" + userToken + '\'' +
                ", date=" + date +
                ", type='" + type + '\'' +
                ", firebaseToken='" + firebaseToken + '\'' +
                ", pageIndex='" + pageIndex + '\'' +
                ", itemsPerPage=" + itemsPerPage +
                ", userChannel=" + userChannel +
                ", channel=" + channel +
                ", lang='" + lang + '\'' +
                ", traceId='" + traceId + '\'' +
                '}';
    }
}
