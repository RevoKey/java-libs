package com.revokey.lib.auth.hash;

import com.revokey.lib.auth.common.AuthOperationException;

public interface HashAlgorithms {

    byte[] computeHash(byte[] input, byte[] key) throws AuthOperationException;

    byte[] getRandomHashKey();
}
