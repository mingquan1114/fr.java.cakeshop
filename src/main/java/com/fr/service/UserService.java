package com.fr.service;

import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fr.javaBean.User;

public interface UserService {

	public List<User> getUsers(QueryWrapper<User> queryWrapper);

	public int addUser(User user);

	public void updateUser(User user);

	public void deleteUser(int id);
}