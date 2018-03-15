package com.revokey.lib.auth;

import java.net.Inet4Address;
import java.net.UnknownHostException;

public class TicketSerializer {

    static final int lengthWithoutName = Long.BYTES + Long.BYTES + 4 + Long.BYTES;

    public static byte[] Serialize(Ticket input) {
        byte[] name = input.getUserName().getBytes();
        byte[] result = new byte[lengthWithoutName + name.length];
        System.arraycopy(getBytes(input.getUserId()), 0, result, 0, Long.BYTES);
        System.arraycopy(getBytes(input.getLoginTime()), 0, result, Long.BYTES, Long.BYTES);
        System.arraycopy(getBytes(input.getExpireDate()), 0, result, Long.BYTES * 2, Long.BYTES);
        System.arraycopy(input.getLoginIP().getAddress(), 0, result, Long.BYTES * 3, 4);
        System.arraycopy(name, 0, result, lengthWithoutName, name.length);
        return result;
    }

    private static byte[] getBytes(Long input) {
        byte[] result = new byte[8];
        for (int i = 0; i < 8; i++) {
            result[7 - i] = (byte) (input >>> (i * 8));
        }
        return result;
    }

    private static Long getValue(byte[] input, int start) {
        long result = 0l;
        for (int i = 0; i < 8; i++) {
            result <<= 8;
            result ^= (int) input[i + start] & 0xFF;
        }
        return result;
    }

    public static Ticket Deserialize(byte[] input) {
        Ticket result = new Ticket(getValue(input, 0), new String(input, lengthWithoutName, input.length - lengthWithoutName));
        result.setLoginTime(getValue(input, Long.BYTES));
        result.setExpireDate(getValue(input, Long.BYTES * 2));
        byte[] address = new byte[4];
        System.arraycopy(input, Long.BYTES * 3, address, 0, 4);
        try {
            result.setLoginIP((Inet4Address) Inet4Address.getByAddress(address));
        } catch (UnknownHostException e) {
            result.setLoginIP((Inet4Address) Inet4Address.getLoopbackAddress());
        }
        return result;
    }

    public static String Convert(byte[] data) {
        if (data == null)
            return null;
        if (data.length == 0)
            return "";
        StringBuilder builder = new StringBuilder();
        for (byte dataByte : data) {
            String hex = String.format("%02X", dataByte);
            builder.append(hex);
        }
        return builder.toString();
    }

    public static byte[] Convert(String input) {
        if (input == null)
            return null;
        if (input.length() == 0)
            return new byte[0];
        if (input.length() % 2 != 0)
            throw new IllegalArgumentException("input");
        int length = input.length() / 2;
        byte[] result = new byte[length];
        for (int i = 0; i < length; i++) {
            int index = i * 2;
            result[i] = (byte) Integer.parseInt(input.substring(index, index + 2), 16);
        }
        return result;
    }

    public static byte[] join(byte[]... byteArray) {
        int length = 0;
        for (int i = 0; i < byteArray.length; i++) {
            length += byteArray[i].length;
        }
        int destPos = 0;
        byte[] result = new byte[length];
        for (int i = 0; i < byteArray.length; i++) {
            System.arraycopy(byteArray[i], 0, result, destPos, byteArray[i].length);
            destPos += byteArray[i].length;
        }
        return result;
    }
}
