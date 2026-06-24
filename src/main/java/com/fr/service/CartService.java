package com.fr.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fr.javaBean.Cart;

import java.util.List;

public interface CartService {
    public long find(QueryWrapper<Cart> cartQueryWrapper);
    public List<Cart> findAll(QueryWrapper<Cart> cartQueryWrapper);
    public void save(Cart cart);
    public void update(Cart cart);
    public Cart getById(int id);
    public void deleteById(int id);
    public List<Cart> findCartWithGoodsName(String userName);
}
