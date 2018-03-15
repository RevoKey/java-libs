package com.revokey.lib.auth.common;

/**
 * 异常或者正常的状态类型
 */
public enum AuthOperationStatus {
    OK,
    ObjectNull,
    DecryptFail,
    EncryptFail,
    InvalidToken,
    SerializationError,
    Uninitialized,
    Exception
}
