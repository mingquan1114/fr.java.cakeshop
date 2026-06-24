package com.fr.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fr.javaBean.Goods;
import com.fr.javaBean.GoodsDTO;
import com.fr.javaBean.SalesDTO;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface GoodsMapper extends BaseMapper<Goods> {

    @Select("SELECT g.id as goodsId, g.name, g.cover, g.price, g.intro, COALESCE(SUM(oi.amount), 0) as totalSales " +
            "FROM goods g LEFT JOIN orderitem oi ON g.id = oi.goods_id " +
            "GROUP BY g.id, g.name, g.cover, g.price, g.intro " +
            "ORDER BY totalSales DESC LIMIT 5")
    List<SalesDTO> selectHotSales();

    @Select("SELECT g.id as goodsId, g.name, g.cover, g.price, g.intro, 0 as totalSales " +
            "FROM goods g ORDER BY g.id DESC LIMIT 5")
    List<SalesDTO> selectNewProducts();

    @Select("SELECT g.id, g.name, g.cover, g.image1, g.image2, g.price, g.intro, g.stock, g.type_id, t.name as typeName " +
            "FROM goods g LEFT JOIN `type` t ON g.type_id = t.id")
    List<GoodsDTO> selectGoodsWithType();

    @Select("SELECT g.id, g.name, g.cover, g.image1, g.image2, g.price, g.intro, g.stock, g.type_id, t.name as typeName " +
            "FROM goods g LEFT JOIN `type` t ON g.type_id = t.id WHERE g.id = #{id}")
    GoodsDTO selectGoodsWithTypeById(int id);

    @Select("SELECT g.id, g.name, g.cover, g.image1, g.image2, g.price, g.intro, g.stock, g.type_id, t.name as typeName " +
            "FROM goods g LEFT JOIN `type` t ON g.type_id = t.id WHERE g.name LIKE CONCAT('%', #{keyword}, '%')")
    List<GoodsDTO> selectGoodsWithTypeByName(String keyword);

    @Select("SELECT COUNT(*) FROM goods WHERE type_id = #{typeId}")
    int countByTypeId(int typeId);
}