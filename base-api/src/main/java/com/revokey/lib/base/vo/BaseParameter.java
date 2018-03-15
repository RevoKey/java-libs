package com.revokey.lib.base.vo;

import javax.persistence.Transient;
import java.io.Serializable;

/**
 * @Name: BaseParameter
 * @Description: 
 * @author RevoKey
 * @date 2018/3/15 16:16
 */
public class BaseParameter implements Serializable {
    private static final long serialVersionUID = 2670019770842687686L;

    @Transient
    private Integer pageNum;

    @Transient
    private Integer pageSize;

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    @Override
    public String toString() {
        return new StringBuilder(getClass().getSimpleName())
                .append(": pageNum=").append(getPageNum())
                .append(", pageSize=").append(getPageSize())
                .toString();
    }
}
