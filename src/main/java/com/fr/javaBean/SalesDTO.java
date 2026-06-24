package com.fr.javaBean;

import lombok.Data;

@Data
public class SalesDTO {
    private Integer goodsId;
    private String name;
    private String cover;
    private String price;
    private String intro;
    private Integer totalSales;
}