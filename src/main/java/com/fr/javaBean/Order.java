package com.fr.javaBean;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("`order`")
public class Order {
    @TableId
    String id; //订单编号（主键）
    float total; //订单总金额
    int amount; //商品总数量
    int status; //订单状态 2:已付款 3:已发货 4:已完成
    int paytype; //支付类型
    String name; //收货人姓名
    String phone; //收货人电话
    String address; //收货地址
    String datetime; //创建时间
    Integer user_id; //用户ID
    
    @TableField(exist = false)
    private String statusDisplay;
    
    @TableField(exist = false)
    private String nameDisplay;
    
    @TableField(exist = false)
    private String addressDisplay;

    @TableField("knight_id")
    private String knightId;

    @TableField("knight_name")
    private String knightName;

    @TableField("knight_phone")
    private String knightPhone;

    @TableField("pickup_time")
    private String pickupTime;

    @TableField("delivery_time")
    private String deliveryTime;

    @TableField(exist = false)
    private float deliveryFee;

    public float calculateDeliveryFee() {
        float baseFee = 3.0f;
        float rate = 0.15f;
        float fee = baseFee + this.total * rate;
        float minFee = 5.0f;
        float maxFee = 20.0f;
        return Math.min(Math.max(fee, minFee), maxFee);
    }
}