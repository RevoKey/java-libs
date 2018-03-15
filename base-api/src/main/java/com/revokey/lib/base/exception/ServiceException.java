package com.revokey.lib.base.exception;

public class ServiceException extends RuntimeException {
    private static final long serialVersionUID = -989314416397802805L;

    private Integer errorCode = 500;

    public ServiceException(String message, Integer errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public ServiceException(String message, Throwable cause, Integer errorCode) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public ServiceException(Throwable cause, Integer errorCode) {
        super(cause);
        this.errorCode = errorCode;
    }

    public ServiceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, Integer errorCode) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.errorCode = errorCode;
    }

    public ServiceException(String message) {
        super(message);
    }

    public ServiceException(Throwable cause) {
        super(cause);
    }

    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public ServiceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(getClass().getSimpleName());
        builder.append(": errorCode=").append(getErrorCode()).append(", message=").append(getMessage());
        if (getCause() != null) {
            builder.append(", describe=").append(getCause().getMessage());
        }
        return builder.toString();
    }
}
