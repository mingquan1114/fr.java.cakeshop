package com.fr.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fr.javaBean.Knight;
import com.fr.mapper.KnightMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class KnightServiceImpl implements KnightService {
    
    @Resource
    private KnightMapper knightMapper;

    @Override
    public Knight login(String username, String password) {
        QueryWrapper<Knight> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        queryWrapper.eq("password", password);
        List<Knight> knights = knightMapper.selectList(queryWrapper);
        return knights.isEmpty() ? null : knights.get(0);
    }

    @Override
    public Knight getById(String id) {
        return knightMapper.selectById(id);
    }

    @Override
    public void update(Knight knight) {
        knightMapper.updateById(knight);
    }

    @Override
    public void updatePassword(String id, String oldPassword, String newPassword) {
        Knight knight = knightMapper.selectById(id);
        if (knight != null && knight.getPassword().equals(oldPassword)) {
            knight.setPassword(newPassword);
            knightMapper.updateById(knight);
        }
    }
}