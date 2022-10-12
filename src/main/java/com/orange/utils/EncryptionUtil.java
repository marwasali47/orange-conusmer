package com.orange.utils;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Properties;


/**
 * Created by Yousef Khaled on Oct, 2018
 */

public class EncryptionUtil {

    private static final String KEY;
    private static final String ALGORITHM = "AES";


    static {
        Properties properties = loadProperties();
        KEY = properties.getProperty("encryption_key");
    }

    public static String encrypt(String str) {
        try {
            SecretKeySpec skeySpec = new SecretKeySpec(KEY.getBytes(StandardCharsets.UTF_8), ALGORITHM);
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
            byte[] encrypted = cipher.doFinal(str.getBytes());
            return Base64.getEncoder().encodeToString(encrypted);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String decrypt(String str) {
        try {
            SecretKeySpec skeySpec = new SecretKeySpec(KEY.getBytes(StandardCharsets.UTF_8), ALGORITHM);
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, skeySpec);
            byte[] decrypted = cipher.doFinal(Base64.getDecoder().decode(str));
            return new String(decrypted);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    private static Properties loadProperties() {
        Properties prop = new Properties();
        InputStream input = null;

        try {

            String filename = "encryption-key.properties";
            input = EncryptionUtil.class.getClassLoader().getResourceAsStream(filename);
            if (input == null) {
                return null;
            }

            //load a properties file from class path, inside static method
            prop.load(input);

            return prop;
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
}
