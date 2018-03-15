package com.revokey.lib.auth.encrypt;

import com.revokey.lib.auth.common.AuthOperationException;

public interface EncryptAlgorithms {

    byte[] encrypt(byte[] input, byte[] key) throws AuthOperationException;

    byte[] decrypt(byte[] input, byte[] key) throws AuthOperationException;

    byte[] getRandomEncryptKey();
}
