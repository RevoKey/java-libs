package com.revokey.lib.base.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.revokey.lib.base.mapper.BaseMapper;
import com.revokey.lib.base.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public abstract class BaseServiceImpl<T> implements BaseService<T> {
    @Autowired
    protected BaseMapper<T> baseMapper;

    @Override
    public T getByPrimaryKey(Object key) {
        return baseMapper.selectByPrimaryKey(key);
    }

    @Override
    public List<T> list(T t) {
        return baseMapper.select(t);
    }

    @Override
    public PageInfo<T> listPage(T t, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        return new PageInfo<>(list(t));
    }

    @Override
    public Integer count(T t) {
        return baseMapper.selectCount(t);
    }

    @Override
    public Integer insert(T t) {
        return baseMapper.insert(t);
    }

    @Override
    public Integer insertSelective(T t) {
        return baseMapper.insertSelective(t);
    }

    @Override
    public Integer insertList(List<T> list) {
        return baseMapper.insertList(list);
    }

    @Override
    public Integer updateByPrimaryKey(T t) {
        return baseMapper.updateByPrimaryKey(t);
    }

    @Override
    public Integer updateByPrimaryKeySelective(T t) {
        return baseMapper.updateByPrimaryKeySelective(t);
    }

    @Override
    public Integer deleteByPrimaryKey(Object key) {
        return baseMapper.deleteByPrimaryKey(key);
    }

    @Override
    public Integer delete(T t) {
        return baseMapper.delete(t);
    }
}
