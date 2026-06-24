package com.fr.service;

import com.fr.javaBean.Knight;

public interface KnightService {
    Knight login(String username, String password);
    Knight getById(String id);
    void update(Knight knight);
    void updatePassword(String id, String oldPassword, String newPassword);
}