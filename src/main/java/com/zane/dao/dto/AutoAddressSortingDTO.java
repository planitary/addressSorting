package com.zane.dao.dto;

import lombok.Data;

@Data
public class AutoAddressSortingDTO {
    // 分拣触发方式 0：下单 1：签收 2：改单
    private Integer sortingType;
    private String consigneeAddress;
    private String consigneeArea;
    private String consigneeCountry;
    private String consigneePhone;
    private String consigneeCity;
    private String consigneeProvince;
    private String consigneeZipCode;
    private String longitude;
    private String latitude;
    private String waybillNo;
    private String consignee;
    private String consigneeContact;
    private String orderType;
    // 签收坐标（必传）
    private String deliverLongitude;
    private String deliverLatitude;
    private String userCode;
    private String password;

}
