package com.revokey.lib.auth;

import com.revokey.lib.auth.common.AuthOperationException;
import com.revokey.lib.auth.encrypt.EncryptAlgorithms;
import com.revokey.lib.auth.encrypt.EncryptFactory;
import com.revokey.lib.auth.hash.HashAlgorithms;
import com.revokey.lib.auth.hash.HashFactory;
import com.revokey.lib.auth.hash.Md5Hash;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;
import java.util.Hashtable;

/**
 * 用于字符串加密签名，确保安全性
 */
public class SecurityTool {

    private Logger logger = LoggerFactory.getLogger(SecurityTool.class);

    static final int hashKeyRandomLength = 6;

    static final int hashKeySettingLength = 18;

    static final int encryptKeyRandomLength = 6;

    static final int encryptKeySettingLength = 18;

    static final int hashResultLength = 16;

    /**
     * 设置从配置文件中配置的用于签名加密的密钥
     *
     * @param hashKeyFromConfiguation    长度至少24个字符的十六进制文本
     * @param encryptKeyFromConfiguation 长度至少24个字符的十六进制文本
     */
    public static void InitKeys(String hashKeyFromConfiguation, String encryptKeyFromConfiguation, String configKeyType) {
        byte[] hashKey = TicketSerializer.Convert(hashKeyFromConfiguation);
        byte[] encryptKey = TicketSerializer.Convert(encryptKeyFromConfiguation);
        new SecurityTool(hashKey, encryptKey, configKeyType);
    }

    public static String Encrypt(String data, String configKeyType) {
        return getInstance(configKeyType).encrypt(data);
    }

    public static String Decrypt(String encrypted, String configKeyType) {
        return getInstance(configKeyType).decrypt(encrypted);
    }

    static final boolean DEBUG = false;

    static Hashtable<String, SecurityTool> instanceContainer = new Hashtable<String, SecurityTool>();

    SecurityTool(byte[] hashKey, byte[] encryptKey, String typeName) {
        this.hashKey = hashKey;
        this.encryptKey = encryptKey;
        instanceContainer.put(typeName, this);
    }

    static SecurityTool getInstance(String key) {
        return instanceContainer.get(key);
    }

    byte[] hashKey;

    byte[] encryptKey;

    static final String hashTool = "MD5";

    static final String encryptTool = "3DES";

    String encrypt(String item) {
        byte[] ticket = item.getBytes();
        EncryptAlgorithms encryptor = EncryptFactory.getInstance(encryptTool);
        byte[] keyEncrypt = encryptor.getRandomEncryptKey();
        byte[] storeKey1 = new byte[encryptKeyRandomLength];
        System.arraycopy(keyEncrypt, 0, storeKey1, 0, encryptKeyRandomLength);
        byte[] totalEncrypt = new byte[encryptKeyRandomLength + encryptKeySettingLength];
        System.arraycopy(encryptKey, 0, totalEncrypt, 0, encryptKeySettingLength);
        System.arraycopy(storeKey1, 0, totalEncrypt, encryptKeySettingLength, encryptKeyRandomLength);
        byte[] encrypted = null;
        try {
            encrypted = encryptor.encrypt(ticket, totalEncrypt);
            logger.debug("ER:{}", TicketSerializer.Convert(encrypted));
            logger.debug("EK:{}", TicketSerializer.Convert(totalEncrypt));
            logger.debug("ET:{}", TicketSerializer.Convert(ticket));
        } catch (AuthOperationException e) {
            logger.error(e.getMessage());
            encrypted = ticket;
        }

        HashAlgorithms hash = HashFactory.getInstance(hashTool);
        byte[] key = hash.getRandomHashKey();
        byte[] storeKey2 = new byte[hashKeyRandomLength];
        System.arraycopy(key, 0, storeKey2, 0, hashKeyRandomLength);
        byte[] totalHash = new byte[hashKeyRandomLength + hashKeySettingLength];
        System.arraycopy(hashKey, 0, totalHash, 0, hashKeySettingLength);
        System.arraycopy(storeKey2, 0, totalHash, hashKeySettingLength, hashKeyRandomLength);
        byte[] hashed = null;
        try {
            hashed = hash.computeHash(TicketSerializer.join(totalEncrypt, totalHash, encrypted), totalHash);
            logger.debug("HK:{}", TicketSerializer.Convert(totalHash));
            logger.debug("HT:{}", TicketSerializer.Convert(TicketSerializer.join(totalEncrypt, totalHash, encrypted)));
            logger.debug("HR:{}", TicketSerializer.Convert(hashed));
        } catch (AuthOperationException e) {
            logger.error(e.getMessage());
            hashed = Md5Hash.getHash(ticket, totalHash);
        }
        if (hashed.length != hashResultLength) {
            logger.warn("hash result length not valid:" + hashed.length + "!=" + hashResultLength);
        }

        String result = TicketSerializer.Convert(TicketSerializer.join(storeKey1, storeKey2, encrypted, hashed));
        logger.debug("Result:" + result);
        return result;
    }

    String decrypt(String input) {
        byte[] buffer;
        try {
            buffer = TicketSerializer.Convert(input);
        } catch (IllegalArgumentException e1) {
            logger.error("decrypt IllegalArgumentException:" + input);
            return null;
        }
        byte[] keyEncrypt = new byte[encryptKeyRandomLength + encryptKeySettingLength];
        byte[] keyHash = new byte[hashKeyRandomLength + hashKeySettingLength];
        byte[] hashValue = new byte[hashResultLength];
        byte[] encrypted = new byte[buffer.length - encryptKeyRandomLength - hashKeyRandomLength - hashResultLength];
        System.arraycopy(encryptKey, 0, keyEncrypt, 0, encryptKeySettingLength);
        System.arraycopy(buffer, 0, keyEncrypt, encryptKeySettingLength, encryptKeyRandomLength);
        System.arraycopy(hashKey, 0, keyHash, 0, hashKeySettingLength);
        System.arraycopy(buffer, encryptKeyRandomLength, keyHash, hashKeySettingLength, hashKeyRandomLength);
        System.arraycopy(buffer, encryptKeyRandomLength + hashKeyRandomLength, encrypted, 0, encrypted.length);
        System.arraycopy(buffer, encryptKeyRandomLength + hashKeyRandomLength + encrypted.length, hashValue, 0, hashResultLength);
        EncryptAlgorithms encryptor = EncryptFactory.getInstance(encryptTool);
        String value = null;
        try {
            byte[] decrypt = encryptor.decrypt(encrypted, keyEncrypt);
            logger.debug("ER:{}", TicketSerializer.Convert(encrypted));
            logger.debug("EK:{}", TicketSerializer.Convert(keyEncrypt));
            logger.debug("ET:{}", TicketSerializer.Convert(decrypt));
            value = new String(decrypt, Charset.forName("utf-8"));
        } catch (AuthOperationException e) {
            logger.error(e.getMessage());
            logger.error(input);
            return null;
        }
        HashAlgorithms hash = HashFactory.getInstance(hashTool);
        byte[] hashed = null;
        try {
            hashed = hash.computeHash(TicketSerializer.join(keyEncrypt, keyHash, encrypted), keyHash);
            logger.debug("HK:{}", TicketSerializer.Convert(keyHash));
            logger.debug("HT:{}", TicketSerializer.Convert(TicketSerializer.join(keyEncrypt, keyHash, encrypted)));
            logger.debug("HR:{}", TicketSerializer.Convert(hashed));
        } catch (AuthOperationException e) {
            logger.error(e.getMessage());
            return null;
        }
        for (int i = 0; i < hashed.length; i++) {
            if (hashed[i] != hashValue[i]) {
                return null;
            }
        }
        return value;
    }
}
