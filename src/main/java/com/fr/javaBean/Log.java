package com.fr.javaBean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("log")
public class Log {

    @TableId(type = IdType.AUTO)
    private Integer id;

    @TableField("user_isadmin")
    private String userIsadmin;

    @TableField("name")
    private String name;

    @TableField("operation")
    private String operation;

    @TableField("module")
    private String module;

    @TableField("details")
    private String details;

    public Log() {
    }

    public Log(String userIsadmin, String name, String operation, String module, String details) {
        this.userIsadmin = userIsadmin;
        this.name = name;
        this.operation = operation;
        this.module = module;
        this.details = details;
    }
}