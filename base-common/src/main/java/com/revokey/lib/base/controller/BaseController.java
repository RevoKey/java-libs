package com.revokey.lib.base.controller;

import com.revokey.lib.base.vo.RequestData;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Name: BaseController
 * @Description:
 * @author RevoKey
 * @date 2018/3/16 18:05
 */
public class BaseController {
    public HttpServletRequest getRequest() {
        ServletRequestAttributes attributes = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
        return attributes.getRequest();
    }

    public HttpServletResponse getResponse() {
        ServletRequestAttributes attributes = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
        return attributes.getResponse();
    }

    public RequestData getRequestData() {
        return new RequestData(getRequest());
    }
}
