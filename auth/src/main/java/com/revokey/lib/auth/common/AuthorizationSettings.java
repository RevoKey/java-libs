package com.revokey.lib.auth.common;

import com.revokey.lib.auth.TicketSerializer;

/**
 * 配置信息
 */
public class AuthorizationSettings {

    /**
     * 过期的毫秒数
     */
    public static Long ExpireDays = 2592000000000l;//30*24*60*60*1000*1000,30,000天cookie过期

    public static Integer maxErrorCount = 5;

    public static Integer maxErrorInMinutes = 30;

    public static String cookieName = "UA";

    public static String cookieDomain = "localhost";

    public static String cookiePath = "/";

    static byte[] hashKey = new byte[] { 0x12, 0x33, (byte) 0xF3, 0x5F, (byte) 0xE1, 0x77, (byte) 0xA2, 0x12, 0x33, (byte) 0xF3, 0x5F, (byte) 0xE1 };

    static byte[] encryptKey = new byte[] { 0x12, 0x33, (byte) 0xF3, 0x5F, (byte) 0xE1, 0x77, (byte) 0xA2, 0x12, 0x33, (byte) 0xF3, 0x5F, (byte) 0xE1 };

    public static void setExpireDays(Long expireDays) {
        ExpireDays = expireDays;
    }

    public static void setMaxErrorCount(Integer maxErrorCount) {
        AuthorizationSettings.maxErrorCount = maxErrorCount;
    }

    public static void setMaxErrorInMinutes(Integer maxErrorInMinutes) {
        AuthorizationSettings.maxErrorInMinutes = maxErrorInMinutes;
    }

    public static void setCookieName(String cookieName) {
        AuthorizationSettings.cookieName = cookieName;
    }

    public static void setCookieDomain(String cookieDomain) {
        AuthorizationSettings.cookieDomain = cookieDomain;
    }

    public static void setCookiePath(String cookiePath) {
        AuthorizationSettings.cookiePath = cookiePath;
    }

    public static void setHashKey(String hashKey) {
        AuthorizationSettings.hashKey = TicketSerializer.Convert(hashKey);
    }

    public static void setEncryptKey(String encryptKey) {
        AuthorizationSettings.encryptKey = TicketSerializer.Convert(encryptKey);
    }

}
