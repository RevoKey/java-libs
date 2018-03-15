package com.revokey.lib.base.vo;

import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class RequestData extends HashMap<Object, Object> {
    private static final long serialVersionUID = -6316259159758183106L;

    public RequestData(HttpServletRequest request) {
        Entry entry;
        Object key;
        Object value;
        Iterator<Map.Entry<String, String[]>> iterator = request.getParameterMap().entrySet().iterator();
        while (iterator.hasNext()) {
            entry = iterator.next();
            key = entry.getKey();
            value = entry.getValue();
            if (value instanceof String[]) {
                value = StringUtils.join((String[])value, ",");
            }
            put(key, value);
        }
    }

    public String getString(Object key) {
        Object value = get(key);
        return (value != null) ? value.toString() : null;
    }

    public Integer getInteger(Object key) {
        String value = getString(key);
        if (StringUtils.isNotBlank(value)) {
            return Integer.parseInt(value);
        }
        return null;
    }

    public Long getLong(Object key) {
        String value = getString(key);
        if (StringUtils.isNotBlank(value)) {
            return Long.parseLong(value);
        }
        return null;
    }

    public Float getFloat(Object key) {
        String value = getString(key);
        if (StringUtils.isNotBlank(value)) {
            return Float.parseFloat(value);
        }
        return null;
    }

    public Double getDouble(Object key) {
        String value = getString(key);
        if (StringUtils.isNotBlank(value)) {
            return Double.parseDouble(value);
        }
        return null;
    }
}
