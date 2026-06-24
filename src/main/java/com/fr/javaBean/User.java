package com.fr.javaBean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("user")
public class User {

    @TableId(type = IdType.AUTO)
    private Integer id;
    
    @TableField("username")
    private String username;
    
    @TableField("email")
    private String email;
    
    @TableField("password")
    private String password;
    
    @TableField("name")
    private String name;
    
    @TableField("phone")
    private String phone;
    
    @TableField("address")
    private String address;
    
    @TableField("isadmin")
    private String isadmin;
    
    @TableField("isvalidate")
    private String isvalidate;
    
    @TableField(exist = false)
    private String roleDisplay;
    
    @TableField(exist = false)
    private String statusDisplay;
    
    @TableField(exist = false)
    private String nameDisplay;
    
    @TableField(exist = false)
    private String addressDisplay;

    public User() {
    }

    public User(int id, String username, String email, String password, String name, String phone, String address, String isadmin, String isvalidate) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.isadmin = isadmin;
        this.isvalidate = isvalidate;
    }
}