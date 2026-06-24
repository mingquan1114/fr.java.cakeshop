package com.fr.javaBean;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("goods")
public class Goods {
    @TableId
    int id; //商品id
    String name; //商品名称
    String cover; //图片
    String image1;//图片1
    String image2;//图片2
    String price;//单价
    String intro;//介绍
    int stock; //库存
    int type_id; //分类ID，对应type表中的id



}
