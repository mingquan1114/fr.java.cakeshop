package com.fr.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fr.javaBean.Goods;
import com.fr.javaBean.GoodsDTO;
import com.fr.javaBean.SalesDTO;
import com.fr.mapper.GoodsMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class GoodsServiceImpl implements GoodsService{
    @Resource
    GoodsMapper goodsMapper;

    @Override
    public List<Goods> findGoods(QueryWrapper<Goods> goodsQueryWrapper) {
        return goodsMapper.selectList(goodsQueryWrapper);
    }

    @Override
    public void updateGoods(Goods goods) {
        com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper<Goods> updateWrapper = new com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper<>();
        updateWrapper.eq("id", goods.getId()).set("stock", goods.getStock());
        goodsMapper.update(null, updateWrapper);
    }

    @Override
    public void updateGoodsFull(Goods goods) {
        goodsMapper.updateById(goods);
    }

    @Override
    public Goods getById(int id) {
        return goodsMapper.selectById(id);
    }

    @Override
    public void saveGoods(Goods goods) {
        goodsMapper.insert(goods);
    }

    @Override
    public void deleteGoods(int id) {
        goodsMapper.deleteById(id);
    }

    @Override
    public List<SalesDTO> findHotSales() {
        return goodsMapper.selectHotSales();
    }

    @Override
    public List<SalesDTO> findNewProducts() {
        return goodsMapper.selectNewProducts();
    }

    @Override
    public int countByTypeId(int typeId) {
        return goodsMapper.countByTypeId(typeId);
    }

    @Override
    public List<GoodsDTO> findGoodsWithType() {
        return goodsMapper.selectGoodsWithType();
    }

    @Override
    public GoodsDTO getByIdWithType(int id) {
        return goodsMapper.selectGoodsWithTypeById(id);
    }

    @Override
    public List<GoodsDTO> findGoodsWithTypeByName(String keyword) {
        return goodsMapper.selectGoodsWithTypeByName(keyword);
    }
}