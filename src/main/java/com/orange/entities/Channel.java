package com.orange.entities;

/**
 * Created by Karim on 10/12/2017.
 */
public class Channel {

    private int id;
    private String name;
    private String topicName;
    private String connectorTopicName;
    private String authenticationType;
    private String domain;
    private String url;
    private String icon;
    private String readIconUrl;
    private String label;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public String getConnectorTopicName() {
        return connectorTopicName;
    }

    public void setConnectorTopicName(String connectorTopicName) {
        this.connectorTopicName = connectorTopicName;
    }

    public String getAuthenticationType() {
        return authenticationType;
    }

    public void setAuthenticationType(String authenticationType) {
        this.authenticationType = authenticationType;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getReadIconUrl() {
        return readIconUrl;
    }

    public void setReadIconUrl(String readIconUrl) {
        this.readIconUrl = readIconUrl;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return "Channel{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", topicName='" + topicName + '\'' +
                ", connectorTopicName='" + connectorTopicName + '\'' +
                ", authenticationType='" + authenticationType + '\'' +
                ", domain='" + domain + '\'' +
                ", url='" + url + '\'' +
                ", icon='" + icon + '\'' +
                ", readIconUrl='" + readIconUrl + '\'' +
                ", label='" + label + '\'' +
                '}';
    }
}
