package com.fr.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fr.javaBean.Goods;
import com.fr.javaBean.GoodsDTO;
import com.fr.javaBean.SalesDTO;

import java.util.List;

public interface GoodsService {
    public List<Goods> findGoods(QueryWrapper<Goods> goodsQueryWrapper);
    public void updateGoods(Goods goods);
    public void updateGoodsFull(Goods goods);
    public Goods getById(int id);
    public void saveGoods(Goods goods);
    public void deleteGoods(int id);
    public List<SalesDTO> findHotSales();
    public List<SalesDTO> findNewProducts();
    public int countByTypeId(int typeId);
    public List<GoodsDTO> findGoodsWithType();
    public GoodsDTO getByIdWithType(int id);
    public List<GoodsDTO> findGoodsWithTypeByName(String keyword);
}