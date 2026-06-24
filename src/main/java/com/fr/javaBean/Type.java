package com.fr.javaBean;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("`type`")
public class Type {
    @TableId
    int id;
    String name;
    
    @TableField(exist = false)
    private String nameEn;
}
