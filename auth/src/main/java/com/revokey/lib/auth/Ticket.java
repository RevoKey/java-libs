package com.revokey.lib.auth;

import com.revokey.lib.auth.common.AuthOperationException;
import com.revokey.lib.auth.common.AuthorizationSettings;

import java.io.Serializable;
import java.net.Inet4Address;
import java.security.Principal;


public class Ticket implements Serializable, Principal {
    private static final long serialVersionUID = 2556354829021838029L;

    public Ticket(Long userId, String userName, String ticketType) {
        this.userId = userId;
        this.userName = userName;
        loginIP = (Inet4Address) Inet4Address.getLoopbackAddress();
        loginTime = System.currentTimeMillis();
        expireDate = loginTime + AuthorizationSettings.ExpireDays;
        this.ticketType = ticketType;
    }

    public Ticket(Long userId, String userName) {
        this(userId, userName, null);
    }

    private Long userId;

    private Long loginTime;

    private Inet4Address loginIP;

    private Long expireDate;

    private String userName;

    private String ticketType;

    public Long getUserId() {
        return userId;
    }

    public Long getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(Long loginTime) {
        this.loginTime = loginTime;
    }

    public Inet4Address getLoginIP() {
        return loginIP;
    }

    public void setLoginIP(Inet4Address loginIP) {
        this.loginIP = loginIP;
    }

    public Long getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(Long expireDate) {
        this.expireDate = expireDate;
    }

    public String getUserName() {
        return userName;
    }

    public String toString() {
        if (ticketType == null || ticketType.length() == 0) {
            return TicketGenerator.getInstance().getTicket(this);
        } else {
            return TicketGenerator.getInstance(ticketType).getTicket(this);
        }
    }

    public Ticket(String from, String ticketType) throws AuthOperationException {
        Ticket result = null;
        try {
            if (ticketType == null || ticketType.length() == 0) {
                result = TicketGenerator.getInstance().getTicket(from);
            } else {
                result = TicketGenerator.getInstance(ticketType).getTicket(from);
            }
        } catch (Exception e) {
            throw new AuthOperationException("invalid ticket.", e);
        }
        if (result == null) {
            throw new AuthOperationException("invalid ticket.");
        }
        userId = result.userId;
        loginTime = result.loginTime;
        loginIP = result.loginIP;
        expireDate = result.expireDate;
        userName = result.userName;

        this.ticketType = ticketType;
    }

    public Ticket(String from) throws AuthOperationException {
        this(from, null);
    }

    @Override
    public String getName() {
        return userName;
    }

    /**
     * 初始化默认的加密签名密钥
     *
     * @param hashKeyFromConfiguation
     * @param encryptKeyFromConfiguation
     */
    public static void InitKeys(String hashKeyFromConfiguation, String encryptKeyFromConfiguation) {
        byte[] hashKey = TicketSerializer.Convert(hashKeyFromConfiguation);
        byte[] encryptKey = TicketSerializer.Convert(encryptKeyFromConfiguation);
        TicketGenerator.InitKeys(hashKey, encryptKey);
    }

    /**
     * 初始化特定的加密签名密钥，通过Ticket构造函数和toString函数传入特定的typeName类型
     *
     * @param hashKeyFromConfiguation
     * @param encryptKeyFromConfiguation
     * @param typeName                   特定的加密签名密钥的算法名称
     */
    public static void InitKeys(String hashKeyFromConfiguation, String encryptKeyFromConfiguation, String typeName) {
        byte[] hashKey = TicketSerializer.Convert(hashKeyFromConfiguation);
        byte[] encryptKey = TicketSerializer.Convert(encryptKeyFromConfiguation);
        TicketGenerator.InitKeys(hashKey, encryptKey, typeName);
    }
}
