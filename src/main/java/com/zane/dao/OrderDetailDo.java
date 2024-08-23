package com.zane.dao;

import lombok.Data;

import java.util.List;

@Data
public class OrderDetailDo {
    /**
     * Copyright 2024 json.cn
     */

    private String dispatchDate;
    private String orderCode;
    private String orderType;
    private String deliveryType;
    private String consignor;
    private String currency;
    private String consigneeContact;
    private String consigneeMobile;
    private String consigneePhone;
    private String consigneeEmail;
    private String consigneeCountry;
    private String consigneeProvince;
    private String consigneeCity;
    private String consigneeArea;
    private String consigneeAddress;
    private String consigneestationcode;
    private String isTemperature;
    private String consigneeZipCode;
    private String consigneeLongitude;
    private String consigneeLatitude;
    private Integer goodsValue;
    private Integer collectingMoney;
    private String paymentMethod;
    private Integer pickType;
    private String pickDate;
    private Integer totalCount;
    private Double totalWeight;
    private Integer totalVolume;
    private Integer skuTotal;
    private String skuName;
    private String buyerId;

    private List<SkuDetailDo> skuDetailList;

}
