package com.revokey.lib.auth;

import com.revokey.lib.auth.common.AuthOperationException;
import com.revokey.lib.auth.common.AuthorizationSettings;
import com.revokey.lib.auth.encrypt.EncryptAlgorithms;
import com.revokey.lib.auth.encrypt.EncryptFactory;
import com.revokey.lib.auth.hash.HashAlgorithms;
import com.revokey.lib.auth.hash.HashFactory;
import com.revokey.lib.auth.hash.Md5Hash;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.Hashtable;

/**
 * 加密：<br/>
 * 原始值=二进制序列化（票据）<br/>
 * 加密：加密值=加密算法（设定加密密钥+随机加密密钥+原始值）<br/>
 * 签名：哈希值=哈希算法（设定加密密钥+随机加密密钥+设定哈希密钥+随机哈希密钥+加密值）<br/>
 * 票据文本 = 二进制到文本（随机哈希密钥+随机加密密钥+加密值+哈希值）
 * <p>
 * <p>
 * 解密：<br/>
 * 获取数据：随机哈希密钥，随机加密密钥，加密值，哈希值 = 文本到二进制（票据文本）<br/>
 * 数据解密：原始值=解密算法（设定加密密钥+随机加密密钥+加密值）<br/>
 * 票据=二进制反序列化（原始值）<br/>
 * 验证签名：哈希值=哈希算法（设定加密密钥+随机加密密钥+设定哈希密钥+随机哈希密钥+加密值）==票据中的哈希值<br/>
 * 解密成功+签名验证通过=> 票据返回<br/>
 *
 * <p>
 */
public class TicketGenerator {

    private static Logger logger = LoggerFactory.getLogger(TicketGenerator.class);

    static final Boolean DEBUG = false;

    static Hashtable<String, TicketGenerator> instanceContainer = new Hashtable<String, TicketGenerator>();

    static final String defaultName = "Default";

    /**
     * 从com.doushi.santong.auth.common.Settings获取默认的加密和哈希密钥，密钥可以被InitKeys改写
     */
    static {
        try {
            Field defaultSetting = AuthorizationSettings.class.getDeclaredField("hashKey");
            defaultSetting.setAccessible(true);
            byte[] hashKey = (byte[]) defaultSetting.get(null);
            defaultSetting = AuthorizationSettings.class.getDeclaredField("encryptKey");
            defaultSetting.setAccessible(true);
            byte[] encryptKey = (byte[]) defaultSetting.get(null);
            InitKeys(hashKey, encryptKey);
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
            logger.error(e.getMessage());
        }
    }

    static void InitKeys(byte[] hashKey, byte[] encryptKey) {
        InitKeys(hashKey, encryptKey, defaultName);
    }

    static void InitKeys(byte[] hashKey, byte[] encryptKey, String instanceName) {
        new TicketGenerator(hashKey, encryptKey, instanceName);
    }

    TicketGenerator(byte[] hashKey, byte[] encryptKey, String typeName) {
        this.hashKey = hashKey;
        this.encryptKey = encryptKey;
        instanceContainer.put(typeName, this);
    }

    public static TicketGenerator getInstance() {
        return getInstance(defaultName);
    }

    public static TicketGenerator getInstance(String key) {
        return instanceContainer.get(key);
    }

    byte[] hashKey;

    byte[] encryptKey;

    static final String hashTool = "MD5";

    static final String encryptTool = "3DES";

    static final int hashKeyRandomLength = 12;

    static final int hashKeySettingLength = 12;

    static final int encryptKeyRandomLength = 12;

    static final int encryptKeySettingLength = 12;

    static final int hashResultLength = 16;

    String getTicket(Ticket item) {
        byte[] ticket = TicketSerializer.Serialize(item);
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
        String result = TicketSerializer.Convert(TicketSerializer.join(storeKey1, storeKey2, encrypted, hashed));
        logger.debug("Result:" + result);
        return result;
    }

    Ticket getTicket(String input) {
        byte[] buffer = TicketSerializer.Convert(input);
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
        Ticket value = null;
        try {
            byte[] decrypt = encryptor.decrypt(encrypted, keyEncrypt);
            logger.debug("ER:{}", TicketSerializer.Convert(encrypted));
            logger.debug("EK:{}", TicketSerializer.Convert(keyEncrypt));
            logger.debug("ET:{}", TicketSerializer.Convert(decrypt));
            value = TicketSerializer.Deserialize(decrypt);
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
