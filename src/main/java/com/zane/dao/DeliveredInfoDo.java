package com.zane.dao;

import lombok.Data;

@Data
public class DeliveredInfoDo {
    private String consigneeAddress;
    private String consigneeArea;
    private String consigneeCity;
    private String consigneeCountry;
    private String consigneeExternalNumber;
    private String consigneeInternalNumber;
    private String consigneeMobile;
    private String consigneePhone;
    private String consigneeProvince;
    private String consigneeStreetName;
    private String consigneeSuburb;
    private String consigneeZipCode;

    private String country;
    private String courier;
    private String courierCode;
    private String daCode;
    private Integer deliveryMethod;
    private String distanceConfirmResult;
    private String ocCode;
    private String ocName;
    private String oldConsigneeAddress;
    private String waybillNo;
    private String deliveredDateString;
    private String longitude;
    private String latitude;
    private String scanDateString;
    private Integer scanRecordId;


}
