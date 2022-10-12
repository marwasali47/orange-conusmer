package com.orange.dtos;

import com.orange.enums.ChannelType;
import com.orange.enums.MessageType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mohamed Gaber on Aug, 2018
 */
public class OrangeOptions implements Options {

    private List<EmailAddressDTO> cc;

    private List<EmailAddressDTO> to;

    private List<EmailAddressDTO> bcc;

    private EmailAddressDTO accountAddress;

    private EmailAddressDTO fromAddress;

    private ChannelType channelType;

    private MessageType messageType;

    private List<Attachment> attachments = new ArrayList<>();

    private String mailId;

    public List<Attachment> getAttachments() {
        return attachments;
    }

    public String getMailId() {
        return mailId;
    }

    public void setMailId(String mailId) {
        this.mailId = mailId;
    }
    public void setAttachments(List<Attachment> attachments) {
        this.attachments = attachments;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    private boolean isRead;

    public List<EmailAddressDTO> getCc() {
        return cc;
    }

    public void setCc(List<EmailAddressDTO> cc) {
        this.cc = cc;
    }

    public List<EmailAddressDTO> getTo() {
        return to;
    }

    public void setTo(List<EmailAddressDTO> to) {
        this.to = to;
    }

    public List<EmailAddressDTO> getBcc() {
        return bcc;
    }

    public void setBcc(List<EmailAddressDTO> bcc) {
        this.bcc = bcc;
    }

    public EmailAddressDTO getAccountAddress() {
        return accountAddress;
    }

    public void setAccountAddress(EmailAddressDTO accountAddress) {
        this.accountAddress = accountAddress;
    }

    public EmailAddressDTO getFromAddress() {
        return fromAddress;
    }

    public void setFromAddress(EmailAddressDTO fromAddress) {
        this.fromAddress = fromAddress;
    }

    public ChannelType getChannelType() {
        return channelType;
    }

    public void setChannelType(ChannelType channelType) {
        this.channelType = channelType;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public void setMessageType(MessageType messageType) {
        this.messageType = messageType;
    }

    @Override
    public String toString() {
        return "GmailOptions{" +
                "mailIs" + mailId +
                "cc=" + cc +
                ", to=" + to +
                ", bcc=" + bcc +
                ", accountAddress=" + accountAddress +
                ", fromAddress=" + fromAddress +
                ", channelType=" + channelType +
                ", messageType=" + messageType +
                '}';
    }
}
