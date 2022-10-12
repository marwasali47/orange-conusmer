package com.orange.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;
import java.util.Properties;


@Component
public class IMAPStoreUtil {

    @Value("${mail.imap.host}")
    private String host;

    @Value("${mail.imap.protocol}")
    private String protocol;

    @Value("${mail.imap.port}")
    private Integer port;

    @Value("${proxy.host}")
    private String proxyHost;

    @Value("${proxy.port}")
    private Integer proxyPort;

    @Value("${proxy.enabled}")
    private boolean isExchangeProxyEnabled;

    public Store connectToImap(String userEmail, String password, boolean debug) throws MessagingException {
        Properties props = new Properties();

        if(isExchangeProxyEnabled){
            props.put(String.format("mail.%s.proxy.host", protocol), proxyHost);
            props.put(String.format("mail.%s.proxy.port", protocol), proxyPort);
        }
        props.put("mail.imaps.sasl.enable", "true");
        props.put(String.format("mail.%s.host", protocol), host);
        props.put(String.format("mail.%s.port", protocol), port);
        props.setProperty(String.format("mail.%s.socketFactory.class", protocol), "javax.net.ssl.SSLSocketFactory");
        props.setProperty(String.format("mail.%s.socketFactory.fallback", protocol), "false");
        props.setProperty(String.format("mail.%s.socketFactory.port", protocol), String.valueOf(port));
        Session session = Session.getInstance(props);
        session.setDebug(debug);

        Store store = session.getStore("imap");
        store.connect(userEmail, password);
        return store;
    }

}
