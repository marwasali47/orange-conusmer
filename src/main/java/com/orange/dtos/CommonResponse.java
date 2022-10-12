package com.orange.dtos;

import lombok.Data;

/**
 * Created by Mohamed Gaber on Aug, 2018
 */
@Data
public class CommonResponse {

    private String userChannelId;

    private String iconUrl;

    private String channelIconUrl;

    private String date;

    private String from;

    private SearchableFrom searchableFrom;

    private String snippet;

    private String title;

    private String desc;

    private String channel;

    private Options options;

    public void setOptions(Options options) {
        this.options = options;
    }

    @Override
    public String toString() {
        return "CommonResponse{" +
                "userChannelId='" + userChannelId + '\'' +
                ", iconUrl='" + iconUrl + '\'' +
                ", channelIconUrl='" + channelIconUrl + '\'' +
                ", date='" + date + '\'' +
                ", from='" + from + '\'' +
                ", snippet='" + snippet + '\'' +
                ", title='" + title + '\'' +
                ", desc='" + desc + '\'' +
                ", channel='" + channel + '\'' +
                ", options=" + options +
                '}';
    }
}
