package com.revokey.lib.auth.hash;

import com.revokey.lib.auth.common.AuthOperationException;

import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class HmacMd5 implements HashAlgorithms {

    static final String Algorithm = "HmacMD5";

    @Override
    public byte[] computeHash(byte[] input, byte[] key) throws AuthOperationException {
        // Generate secret key for HMAC-MD5
        SecretKey sk = new SecretKeySpec(key, Algorithm);

        // Get instance of Mac object implementing HMAC-MD5, and
        // initialize it with the above secret key
        Mac mac;
        try {
            mac = Mac.getInstance(Algorithm);
        } catch (NoSuchAlgorithmException e) {
            throw new AuthOperationException(e);
        }
        try {
            mac.init(sk);
        } catch (InvalidKeyException e) {
            throw new AuthOperationException(e);
        }
        return mac.doFinal(input);
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
