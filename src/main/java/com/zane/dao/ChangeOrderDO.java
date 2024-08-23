package com.zane.dao;

import lombok.Data;

@Data
public class ChangeOrderDO {
    private String clientCode;
    private String clientName;
    private String consignee;
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
    private String consigneeLongitude;
    private String consigneeLatitude;
    private Integer shippingMethod;

    private String consignor;
    private String consignorAddress;
    private String consignorArea;
    private String consignorCity;
    private String consignorContact;
    private String consignorCountry;
    private String consignorExternalNumber;
    private String consignorInternalNumber;
    private String consignorMobile;
    private String consignorPhone;
    private String consignorProvince;
    private String consignorStreetName;
    private String consignorZipCode;
    private String consignorLongitude;
    private String consignorLatitude;
    private String orderType;
    private String sellerId;
    private String waybillNO;

}
