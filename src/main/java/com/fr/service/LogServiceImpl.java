package com.fr.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fr.javaBean.Log;
import com.fr.mapper.LogMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class LogServiceImpl implements LogService {

    @Resource
    LogMapper logMapper;

    @Override
    @org.springframework.transaction.annotation.Transactional
    public void saveLog(Log log) {
        logMapper.insert(log);
        System.out.println("Log inserted to database: " + log.getOperation());
    }

    @Override
    public List<Log> findAll(QueryWrapper<Log> queryWrapper) {
        return logMapper.selectList(queryWrapper);
    }

    @Override
    public int count(QueryWrapper<Log> queryWrapper) {
        return (int) (long) logMapper.selectCount(queryWrapper);
    }

    @Override
    public void deleteById(int id) {
        logMapper.deleteById(id);
    }

    @Override
    public void deleteAll() {
        QueryWrapper<Log> queryWrapper = new QueryWrapper<>();
        logMapper.delete(queryWrapper);
    }
}