package com.fr.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fr.javaBean.Type;
import com.fr.mapper.TypeMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class TypeServiceImpl implements TypeService{
    @Resource
    TypeMapper typeMapper;

    @Override
    public List<Type> listType(QueryWrapper<Type> typeQueryWrapper) {
        return typeMapper.selectList(typeQueryWrapper);
    }

    @Override
    public void saveType(Type type) {
        typeMapper.insert(type);
    }

    @Override
    public void updateType(Type type) {
        typeMapper.updateById(type);
    }

    @Override
    public void deleteType(int id) {
        typeMapper.deleteById(id);
    }

    @Override
    public Type getById(int id) {
        return typeMapper.selectById(id);
    }
}