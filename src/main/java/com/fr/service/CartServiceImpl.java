package com.fr.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fr.javaBean.Cart;
import com.fr.mapper.CartMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class CartServiceImpl implements CartService{
    @Resource
    CartMapper cartMapper;

    @Override
    public long find(QueryWrapper<Cart> cartQueryWrapper) {
        return cartMapper.selectCount(cartQueryWrapper);
    }

    @Override
    public List<Cart> findAll(QueryWrapper<Cart> cartQueryWrapper) {
        return cartMapper.selectList(cartQueryWrapper);
    }

    @Override
    public void save(Cart cart) {
        cartMapper.insert(cart);
    }

    @Override
    public void update(Cart cart) {
        cartMapper.updateById(cart);
    }

    @Override
    public Cart getById(int id) {
        return cartMapper.selectById(id);
    }

    @Override
    public void deleteById(int id) {
        cartMapper.deleteById(id);
    }

    @Override
    public List<Cart> findCartWithGoodsName(String userName) {
        return cartMapper.selectCartWithGoodsName(userName);
    }
}
