package com.revokey.lib.auth.encrypt;

import com.revokey.lib.auth.common.AuthOperationException;
import com.revokey.lib.auth.common.AuthOperationStatus;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class AesAlgorithms {

    static final String Algorithm = "AES";

    public static byte[] encrypt(byte[] input, byte[] key) throws AuthOperationException {
        try {
            KeyGenerator kgen = KeyGenerator.getInstance(Algorithm);
            kgen.init(128, new SecureRandom(key));
            SecretKey secretKey = kgen.generateKey();
            byte[] enCodeFormat = secretKey.getEncoded();
            SecretKey aeskey = new SecretKeySpec(enCodeFormat, Algorithm);
            Cipher cipher = Cipher.getInstance(Algorithm);
            cipher.init(Cipher.ENCRYPT_MODE, aeskey);
            return cipher.doFinal(input);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
            throw new AuthOperationException(e, AuthOperationStatus.EncryptFail);
        }
    }

    public static byte[] decrypt(byte[] input, byte[] key) throws AuthOperationException {
        try {
            KeyGenerator kgen = KeyGenerator.getInstance(Algorithm);
            kgen.init(128, new SecureRandom(key));
            SecretKey secretKey = kgen.generateKey();
            byte[] enCodeFormat = secretKey.getEncoded();
            SecretKey aeskey = new SecretKeySpec(enCodeFormat, Algorithm);
            Cipher cipher = Cipher.getInstance(Algorithm);
            cipher.init(Cipher.DECRYPT_MODE, aeskey);
            return cipher.doFinal(input);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
            throw new AuthOperationException(e, AuthOperationStatus.DecryptFail);
        }

    }

    public static byte[] getRandomEncryptKey() {
        try {
            KeyGenerator kg = KeyGenerator.getInstance(Algorithm);
            SecretKey sk = kg.generateKey();
            return sk.getEncoded();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return new byte[64];
    }

    public static byte[] encrypt(String content, String sKey) throws AuthOperationException, UnsupportedEncodingException {
        return encrypt(content.getBytes("utf-8"), sKey.getBytes());
    }

    public static byte[] decrypt(byte[] content, String sKey) throws AuthOperationException {
        return decrypt(content, sKey.getBytes());
    }
}