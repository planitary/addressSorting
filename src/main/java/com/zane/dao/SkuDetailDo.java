package com.zane.dao;

import lombok.Data;

@Data
public class SkuDetailDo {
    private String skuNo;
    private String skuName;
    private String skuDesc;
    private Integer skuQty;
    private String skuUrl;
    private Double skuGoodsValue;
    private Double skuWeight;
    private String skuHsCode;
}
