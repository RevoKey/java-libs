package com.revokey.lib.base.vo;

/**
 * @Name: ResponseData
 * @Description:
 * @author RevoKey
 * @date 2018/3/20 12:03
 */
public class ResponseData {
    public final static int SUCCESS = 0;

    public final static int FAILURE = 500;

    private Integer code;

    private String message;

    private Object data;

    public Long getTime() {
        return System.currentTimeMillis() / 1000;
    }

    public static ResponseData success() {
        ResponseData responseData = new ResponseData();
        responseData.code = SUCCESS;
        responseData.message = "";
        return responseData;
    }

    public static ResponseData success(Object data) {
        ResponseData responseData = new ResponseData();
        responseData.code = SUCCESS;
        responseData.message = "";
        responseData.data = data;
        return responseData;
    }

    public static ResponseData success(Object data, String message) {
        ResponseData responseData = new ResponseData();
        responseData.code = SUCCESS;
        responseData.message = message;
        responseData.data = data;
        return responseData;
    }

    public static ResponseData failure(String message) {
        ResponseData responseData = new ResponseData();
        responseData.code = FAILURE;
        responseData.message = message;
        return responseData;
    }

    public static ResponseData failure(String message, Integer code) {
        ResponseData responseData = new ResponseData();
        responseData.code = code;
        responseData.message = message;
        return responseData;
    }

    public static ResponseData custom(Object data, String message, Integer code) {
        ResponseData responseData = new ResponseData();
        responseData.code = code;
        responseData.message = message;
        responseData.data = data;
        return responseData;
    }
}
