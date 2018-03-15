package com.revokey.lib.auth.hash;

import com.revokey.lib.auth.common.AuthOperationException;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Md5Hash implements HashAlgorithms {

    @Override
    public byte[] computeHash(byte[] input, byte[] key) throws AuthOperationException {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new AuthOperationException(e);
        }
        md.update(key);
        md.update(input);
        md.update(key);
        return md.digest();
    }

    public static byte[] getHash(byte[] input, byte[] key) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
        }
        md.update(key);
        md.update(input);
        md.update(key);
        return md.digest();
    }

    @Override
    public byte[] getRandomHashKey() {
        try {
            KeyGenerator kg = KeyGenerator.getInstance("HmacMD5");
            SecretKey sk = kg.generateKey();
            return sk.getEncoded();
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return new byte[64];
    }

}
