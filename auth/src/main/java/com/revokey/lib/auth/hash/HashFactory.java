package com.revokey.lib.auth.hash;

public class HashFactory {

    private HashFactory() { }

    public static HashAlgorithms getInstance(String name) {
        return new HmacMd5();
    }
}
