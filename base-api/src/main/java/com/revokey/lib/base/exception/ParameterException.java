package com.revokey.lib.base.exception;

public class ParameterException extends RuntimeException {
    private static final long serialVersionUID = 6184218493461388235L;

    private Integer errorCode = 501;

    public ParameterException(String message, Integer errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public ParameterException(String message, Throwable cause, Integer errorCode) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public ParameterException(Throwable cause, Integer errorCode) {
        super(cause);
        this.errorCode = errorCode;
    }

    public ParameterException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, Integer errorCode) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.errorCode = errorCode;
    }

    public ParameterException() {
    }

    public ParameterException(String message) {
        super(message);
    }

    public ParameterException(Throwable cause) {
        super(cause);
    }

    public ParameterException(String message, Throwable cause) {
        super(message, cause);
    }

    public ParameterException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }

    public String toString() {
        StringBuilder str = new StringBuilder(getClass().getSimpleName());
        str.append(": errorCode=").append(getErrorCode()).append(", message=").append(getMessage());
        if (getCause() != null) {
            str.append(", describe=").append(getCause().getMessage());
        }
        return str.toString();
    }
}
