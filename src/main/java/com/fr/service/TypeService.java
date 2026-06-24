package com.fr.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fr.javaBean.Type;

import java.util.List;

public interface TypeService {
    public List<Type> listType(QueryWrapper<Type> typeQueryWrapper);
    public void saveType(Type type);
    public void updateType(Type type);
    public void deleteType(int id);
    public Type getById(int id);
}