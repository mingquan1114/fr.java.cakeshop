package com.fr.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fr.javaBean.Log;

import java.util.List;

public interface LogService {
    void saveLog(Log log);
    List<Log> findAll(QueryWrapper<Log> queryWrapper);
    int count(QueryWrapper<Log> queryWrapper);
    void deleteById(int id);
    void deleteAll();
}