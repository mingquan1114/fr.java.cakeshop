package com.fr.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fr.javaBean.OrderDetailDTO;
import com.fr.javaBean.OrderItem;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface OrderItemMapper extends BaseMapper<OrderItem> {
    
    @Select("SELECT oi.id, oi.price, oi.amount, oi.goods_id, oi.order_id, " +
            "g.name as goods_name, g.cover as goods_cover, g.intro as goods_intro, g.price as goods_price " +
            "FROM orderitem oi LEFT JOIN goods g ON oi.goods_id = g.id " +
            "WHERE oi.order_id = #{orderId}")
    List<OrderDetailDTO> selectOrderDetails(@Param("orderId") String orderId);
}