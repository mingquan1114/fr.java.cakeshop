package com.fr.javaBean;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("cart")
public class Cart {
    @TableId
    int id;
    @TableField("good_id")
    String goodId;
    String user_name;
    @TableField(exist = false)
    String name;
    String intro;
    int amount;
    float price;
    float total_price;
    String cover;
    
    public String getGood_id() {
        return goodId;
    }
    
    public void setGood_id(String good_id) {
        this.goodId = good_id;
    }
}