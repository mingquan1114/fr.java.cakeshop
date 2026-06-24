package com.fr.javaBean;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

@Data
public class OrderDetailDTO {
    private Integer id;
    private Float price;
    private Integer amount;
    
    @TableField("goods_id")
    private Integer goodsId;
    
    @TableField("order_id")
    private String orderId;
    
    @TableField("goods_name")
    private String goodsName;
    
    @TableField("goods_cover")
    private String goodsCover;
    
    @TableField("goods_intro")
    private String goodsIntro;
    
    @TableField("goods_price")
    private Float goodsPrice;
}