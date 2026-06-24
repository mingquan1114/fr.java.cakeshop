package com.fr.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fr.javaBean.Cart;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface CartMapper extends BaseMapper<Cart> {
    
    @Select("SELECT c.id, c.good_id, c.user_name, g.name, c.intro, c.amount, c.price, c.total_price, c.cover " +
            "FROM cart c LEFT JOIN goods g ON c.good_id = g.id " +
            "WHERE c.user_name = #{userName}")
    List<Cart> selectCartWithGoodsName(@Param("userName") String userName);
}
