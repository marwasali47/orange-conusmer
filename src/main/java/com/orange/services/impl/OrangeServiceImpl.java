package com.orange.services.impl;

import com.orange.dtos.*;
import com.orange.enums.ChannelType;
import com.orange.enums.MessageType;
import com.orange.kafka.requests.Request;
import com.orange.services.OrangeService;
import com.orange.utils.IMAPStoreUtil;
import com.sun.mail.util.BASE64DecoderStream;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeUtility;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Created by Mohamed Gaber on Aug, 2018
 */

@Service
public class OrangeServiceImpl implements OrangeService {

    private Logger logger = LogManager.getLogger(OrangeServiceImpl.class);

    private static final long K = 1024;
    private static final long M = K * K;

    @Autowired
    private IMAPStoreUtil imapStoreUtil;

    @Override
    public List<CommonResponse> getAllMails(Request request) throws MessagingException, IOException {

        List<CommonResponse> mailsList = new ArrayList<>();
        Store imapStore = null;
        Folder inbox = null;

        try {
            // Connect to IMAP store and access inbox folder
            imapStore = imapStoreUtil.connectToImap(request.getUserChannel().getChannelUsername(), request.getUserChannel().getChannelPassword(), false);
            inbox = imapStore.getFolder("inbox");
            inbox.open(Folder.READ_ONLY);

            int pageIndex = Integer.parseInt(request.getPageIndex());
            int pageSize = request.getItemsPerPage();

            // Calculate startIndex and endIndex for retrieving messages
            int messagesCount = inbox.getMessageCount();
            int startIndex = messagesCount - (pageIndex * pageSize + pageSize) + 1;
            int endIndex = messagesCount - (pageIndex * pageSize);
            int numOfRemainingMessages = messagesCount - (pageIndex * pageSize);
            if (numOfRemainingMessages < 0) {
                return new ArrayList<>();
            } else if (numOfRemainingMessages < pageSize) {
                startIndex = 1;
                endIndex = numOfRemainingMessages;
            }

            // Retrieve messages by startIndex and endIndex that have been calculated above
            Message[] messages = inbox.getMessages(startIndex, endIndex);
            logger.debug("inbox.getMessages ********** {}", messages.length);

            for (int i = 0; i < messages.length; i++) {
                CommonResponse commonResponse = extractMessageDetails(request, messages[i]);
                mailsList.add(commonResponse);
            }

        } finally {
            if (inbox != null)
                inbox.close(false);
            if (imapStore != null)
                imapStore.close();
        }

        return mailsList;

    }

    private CommonResponse extractMessageDetails(Request request, Message message) throws MessagingException, IOException {
        try {
            CommonResponse commonResponse = new CommonResponse();
            commonResponse.setChannel(request.getChannel().getName());
            commonResponse.setUserChannelId("" + request.getUserChannel().getId());
            commonResponse.setDate(String.valueOf(message.getReceivedDate().getTime()));
            commonResponse.setIconUrl(request.getChannel().getIcon());
            commonResponse.setChannelIconUrl(request.getChannel().getIcon());
            String sender = message.getFrom()[0].toString();
            commonResponse.setFrom(MimeUtility.decodeText(sender).toLowerCase());
            commonResponse.setTitle(message.getSubject());

            String userEmail = request.getUserChannel().getChannelUsername();
            Optional<EmailAddressDTO> first = Arrays.stream(message.getAllRecipients())
                    .filter(address -> toEmailAddressDTO(address).getEmail().equals(userEmail))
                    .map(this::toEmailAddressDTO)
                    .findFirst();

            EmailAddressDTO userEmailAddress = new EmailAddressDTO("", "");
            if (first.isPresent())
                userEmailAddress = first.get();

            commonResponse.setSearchableFrom(new SearchableFrom(sender, sender));

            OrangeOptions options = new OrangeOptions();
            options.setMailId(message.getMessageNumber() + "");
            options.setTo(getMessageRecipients(message, Message.RecipientType.TO));
            options.setCc(getMessageRecipients(message, Message.RecipientType.CC));
            options.setBcc(getMessageRecipients(message, Message.RecipientType.BCC));
            options.setAccountAddress(userEmailAddress);
            options.setFromAddress(toEmailAddressDTO(message.getFrom()[0]));
            options.setChannelType(ChannelType.EMAIL);
            options.setMessageType(MessageType.MESSAGE);
            try {
                Arrays.asList(message.getFlags().getSystemFlags()).stream().forEach(flag -> {
                    if (flag.equals(Flags.Flag.SEEN)) {
                        options.setRead(true);
                    }
                });
            } catch (Exception ex) {
                logger.debug("exception setting read flag ", ex);
            }
            options.setAttachments(getAttachmentList(message));
            commonResponse.setOptions(options);

            String htmlContent = getContentAsHtml(message);

            // Extract snippet
            String contentSnippet = Jsoup.parse(htmlContent).text();
            if (contentSnippet != null && contentSnippet.length() > 60) {
                contentSnippet = contentSnippet.substring(0, 59) + "...";
            }
            commonResponse.setSnippet(contentSnippet);

            // Replace inline image with HTML image tag with src of base64 string ex. <img src="data:image/png;base64, bHz9SRwEx4pS1YxV2EdGcnVGJlXcq3O2Oxvpj6" />
            List<InlineImageDto> inlineImages = getInlineImages(message);
            for (InlineImageDto inlineImageDto : inlineImages) {
                htmlContent = htmlContent.replace("cid:" + inlineImageDto.getImageId(), "data:image/png;base64, " + inlineImageDto.getImageBase64String());
            }
            commonResponse.setDesc(htmlContent);
            return commonResponse;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String convertToStringRepresentation(final long value) {
        final long[] dividers = new long[]{M, K, 1};
        final String[] units = new String[]{"MB", "KB", "B"};
        if (value < 1)
            throw new IllegalArgumentException("Invalid file size: " + value);
        String result = null;
        for (int i = 0; i < dividers.length; i++) {
            final long divider = dividers[i];
            if (value >= divider) {
                result = format(value, divider, units[i]);
                break;
            }
        }
        return result;
    }

    private String format(final long value, final long divider, final String unit) {
        final double result =
                divider > 1 ? (double) value / (double) divider : (double) value;
        return String.format("%.1f %s", Double.valueOf(result), unit);
    }

    private EmailAddressDTO toEmailAddressDTO(Address address) {
        String extractEmailRegex = "(?<name>.*)\\s<(?<email>.*)>";
        Pattern pattern = Pattern.compile(extractEmailRegex);
        Matcher matcher = pattern.matcher(address.toString());
        if (matcher.find()) {
            String name = matcher.group("name");
            if (name != null) {
                name = name.replace("\"", "");
            }
            String email = matcher.group("email");
            return new EmailAddressDTO(name, email);
        } else {
            return new EmailAddressDTO(address.toString(), address.toString());
        }
    }

    private List<EmailAddressDTO> getMessageRecipients(Message message, Message.RecipientType recipientType) throws MessagingException {

        Address[] recipients = message.getRecipients(recipientType);
        if (recipients == null || recipients.length == 0) {
            return Collections.emptyList();
        }

        return Arrays.stream(recipients)
                .map(this::toEmailAddressDTO)
                .collect(Collectors.toList());
    }

    private String getContentAsHtml(Part part) throws IOException, MessagingException {

        //check if the content is html text
        if (part.isMimeType("text/html")) {
            return (String) part.getContent();
        }
        //check if the content has attachment
        else if (part.isMimeType("multipart/*")) {
            StringBuilder content = new StringBuilder();
            Multipart mp = (Multipart) part.getContent();
            int count = mp.getCount();
            for (int i = 0; i < count; i++) {
                content.append(getContentAsHtml(mp.getBodyPart(i)));
            }
            return content.toString();
        }
        //check if the content is a nested message
        else if (part.isMimeType("message/rfc822")) {
            return getContentAsHtml((Part) part.getContent());
        }
        return "";
    }

    private List<Attachment> getAttachmentList(Message message) throws MessagingException, IOException {

        String contentType = message.getContentType();
        List<Attachment> attachmentList = new ArrayList<>();
        if (contentType.contains("multipart")) {
            Multipart multiPart = (Multipart) message.getContent();
            int numberOfParts = multiPart.getCount();
            int attachmentIndex = 0;
            for (int partCount = 0; partCount < numberOfParts; partCount++) {
                MimeBodyPart part = (MimeBodyPart) multiPart.getBodyPart(partCount);
                if (Part.ATTACHMENT.equalsIgnoreCase(part.getDisposition())) {
                    String fileName = part.getFileName();
                    Attachment attachment = new Attachment();
                    attachment.setId(fileName+ ":" + attachmentIndex);
                    attachment.setName(fileName);
                    attachment.setSize(convertToStringRepresentation(part.getSize()));
                    attachment.setType("");
                    attachmentList.add(attachment);
                    attachmentIndex++;
                }
            }
        }
        return attachmentList;
    }

    private List<InlineImageDto> getInlineImages(Part part) throws IOException, MessagingException {

        //check if the content is an inline image
        if (part.isMimeType("image/*") && part.getHeader("Content-ID")!= null) {
            String cid = part.getHeader("Content-ID")[0].replaceAll("<", "");
            cid = cid.replaceAll(">", "");
            BASE64DecoderStream decoderStream = (BASE64DecoderStream) part.getContent();
            byte[] buffer = new byte[1024];
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            int bytesRead;
            while ((bytesRead = decoderStream.read(buffer)) != -1) {
                output.write(buffer, 0, bytesRead);
            }
            String imageBase64String = Base64.getEncoder().encodeToString(output.toByteArray());
            output.close();
            return Collections.singletonList(new InlineImageDto(cid, imageBase64String));
        }
        //check if the content has attachment
        else if (part.isMimeType("multipart/*")) {
            List<InlineImageDto> imageDtoList = new ArrayList<>();
            Multipart mp = (Multipart) part.getContent();
            int count = mp.getCount();
            for (int i = 0; i < count; i++) {
                imageDtoList.addAll(getInlineImages(mp.getBodyPart(i)));
            }
            return imageDtoList;
        }
        //check if the content is a nested message
        else if (part.isMimeType("message/rfc822")) {
            return getInlineImages((Part) part.getContent());
        }
        return new ArrayList<>();
    }

}
