package com.fr.service;

import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.stereotype.Service;

import com.fr.javaBean.User;
import com.fr.mapper.UserMapper;

import javax.annotation.Resource;

@Service
public class UserServiceImpl implements UserService {

	@Resource
	UserMapper userMapper;

	@Override
	public List<User> getUsers(QueryWrapper<User> queryWrapper) {
		return userMapper.selectList(queryWrapper);
	}

	@Override
	public int addUser(User user) {
		return userMapper.insert(user);
	}

	@Override
	public void updateUser(User user) {
		userMapper.updateById(user);
	}

	@Override
	public void deleteUser(int id) {
		userMapper.deleteById(id);
	}
}