package com.zane.dao;

import lombok.Data;

/**
 * 巴西下单，packageInfo
 */
@Data
public class PackageInfoDo {
    private String codCurrency;
    private String productValue;
    private String clientDeclaredValue;
    private  String clientDeclaredCurrency;
    private  String productValueCurrency;
    private  Integer width;
    private  String goodsType;
    private  String grossWeight;
    private  Integer high;
    private  Integer totalVolume;
    private  Integer totalCount;
    private  String collectingMoney;
    private  Integer length;
    private  String paymentMethod;
}
