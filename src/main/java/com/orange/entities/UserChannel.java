package com.orange.entities;

import com.orange.enums.FirstLoadFetchByCriteria;
import com.orange.enums.FirstLoadStatus;
import com.orange.utils.EncryptionUtil;
import lombok.Data;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Date;

/**
 * Created by Karim on 10/12/2017.
 */
@Data
public class UserChannel {

    private static Logger logger = LogManager.getLogger(UserChannel.class);

    private int id;

    private String username;

    private int channelId;

    private String channelUsername;

    private String channelPassword;

    private String accountName;

    private Date lastMessageDate;

    private FirstLoadStatus firstLoadStatus;

    private FirstLoadFetchByCriteria firstLoadFetchByCriteria;

    private Long firstLoadFetchValue;

    private int retentionValue;

    private int fetchValue;

    public String getChannelUsername() {
        try {
            return EncryptionUtil.decrypt(channelUsername);
        } catch (Exception e) {
            logger.error(e);
        }
        return null;
    }

    public void setChannelUsername(String channelUsername) {
        try {
            this.channelUsername = EncryptionUtil.encrypt(channelUsername);
        } catch (Exception e) {
            logger.error(e);
        }
    }

    public String getChannelPassword() {
        try {
            return EncryptionUtil.decrypt(channelPassword);
        } catch (Exception e) {
            logger.error(e);
        }
        return null;
    }

    public void setChannelPassword(String channelPassword) {
        try {
            this.channelPassword = EncryptionUtil.encrypt(channelPassword);
        } catch (Exception e) {
            logger.error(e);
        }
    }
}
