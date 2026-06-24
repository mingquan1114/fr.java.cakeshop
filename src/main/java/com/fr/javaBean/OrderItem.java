package com.fr.javaBean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("orderitem")
public class OrderItem {
    @TableId(type = IdType.AUTO)
    private Integer id;
    
    @TableField("price")
    private Float price;
    
    @TableField("amount")
    private Integer amount;
    
    @TableField("goods_id")
    private Integer goodsId;
    
    @TableField("order_id")
    private String orderId;
}