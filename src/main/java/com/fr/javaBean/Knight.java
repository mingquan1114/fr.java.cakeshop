package com.fr.javaBean;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("knight")
public class Knight {
    @TableId
    private String id;
    
    @TableField("username")
    private String username;
    
    @TableField("password")
    private String password;
    
    @TableField("name")
    private String name;
    
    @TableField("phone")
    private String phone;
    
    @TableField("avatar")
    private String avatar;
    
    @TableField("status")
    private Integer status;
    
    @TableField("create_time")
    private String createTime;
    
    @TableField("update_time")
    private String updateTime;
}