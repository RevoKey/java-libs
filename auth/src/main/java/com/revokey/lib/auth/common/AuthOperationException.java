package com.revokey.lib.auth.common;

/**
 * @Title: 加密解密过程中的异常
 * @Description:
 * @author RevoKey
 * @date 2018/3/14 17:06
 */
public class AuthOperationException extends Exception {

    private static final long serialVersionUID = 7225538929027523155L;

    public AuthOperationException() {
        super();
    }

    public AuthOperationException(String message) {
        this(message, AuthOperationStatus.Exception);
    }

    public AuthOperationException(String message, AuthOperationStatus status) {
        super(message);
        this.status = status;
    }

    public AuthOperationException(Throwable cause) {
        this(cause, AuthOperationStatus.Exception);
    }

    public AuthOperationException(Throwable cause, AuthOperationStatus status) {
        super(cause);
        this.status = status;
    }

    public AuthOperationException(String message, Throwable cause) {
        this(message, cause, AuthOperationStatus.Exception);
    }

    public AuthOperationException(String message, Throwable cause, AuthOperationStatus status) {
        super(message, cause);
        this.status = status;
    }

    private AuthOperationStatus status;

    public AuthOperationStatus getStatus() {
        return status;
    }

    public void setStatus(AuthOperationStatus status) {
        this.status = status;
    }
}
