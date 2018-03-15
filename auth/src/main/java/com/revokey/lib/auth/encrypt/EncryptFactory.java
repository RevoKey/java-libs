package com.revokey.lib.auth.encrypt;

public class EncryptFactory {

    private EncryptFactory() { }

    public static EncryptAlgorithms getInstance(String name) {
        return new TripleDes();
    }
}
