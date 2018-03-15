package com.revokey.lib.auth.encrypt;

import com.revokey.lib.auth.common.AuthOperationException;
import com.revokey.lib.auth.common.AuthOperationStatus;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class TripleDes implements EncryptAlgorithms {

    static final String Algorithm = "DESede";

    @Override
    public byte[] encrypt(byte[] input, byte[] key) throws AuthOperationException {
        SecretKey deskey = new SecretKeySpec(key, Algorithm);
        Cipher cipher;
        try {
            cipher = Cipher.getInstance(Algorithm);
            cipher.init(Cipher.ENCRYPT_MODE, deskey);
            return cipher.doFinal(input);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
            throw new AuthOperationException(e, AuthOperationStatus.EncryptFail);
        }
    }

    @Override
    public byte[] decrypt(byte[] input, byte[] key) throws AuthOperationException {
        SecretKey deskey = new SecretKeySpec(key, Algorithm);
        try {
            Cipher cipher = Cipher.getInstance(Algorithm);
            cipher.init(Cipher.DECRYPT_MODE, deskey);
            return cipher.doFinal(input);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
            throw new AuthOperationException(e, AuthOperationStatus.DecryptFail);
        }

    }

    @Override
    public byte[] getRandomEncryptKey() {
        try {
            KeyGenerator kg = KeyGenerator.getInstance(Algorithm);
            SecretKey sk = kg.generateKey();
            return sk.getEncoded();
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return new byte[64];
    }

}
