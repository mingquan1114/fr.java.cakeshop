package com.fr.javaBean;

import lombok.Data;

@Data
public class GoodsDTO extends Goods {
    private String typeName;
    private String typeNameEn;
    private String nameEn;
}