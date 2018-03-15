package com.revokey.lib.base.service;

import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * @Name: BaseService
 * @Description: 
 * @author RevoKey
 * @date 2018/3/15 15:05
 */
public interface BaseService<T> {
    T getByPrimaryKey(Object key);

    List<T> list(T t);

    PageInfo<T> listPage(T t, Integer pageNum, Integer pageSize);

    Integer count(T t);

    Integer insert(T t);

    Integer insertSelective(T t);

    Integer insertList(List<T> list);

    Integer updateByPrimaryKey(T t);

    Integer updateByPrimaryKeySelective(T t);

    Integer deleteByPrimaryKey(Object key);

    Integer delete(T t);
}
