package com.zane.dao.dto;

import lombok.Data;
import org.hibernate.procedure.spi.ParameterRegistrationImplementor;
import org.locationtech.jts.index.strtree.SIRtree;

@Data
public class OrderTMSInfo {

    private String actualReceiveStation;
    private String consigneeAddress;
    private String consigneeArea;
    private String consigneeCity;
    private String consigneeContact;
    private String consigneeCountry;
    private String consigneeLatitude;
    private String consigneeLongitude;
    private String consigneePhone;
    private String consigneeProvince;

    private String consigneeRuta;

    private String consigneeZipCode;

    private String consignor;
    private String consignorAddress;
    private String consignorArea;
    private String consignorCity;
    private String consignorContact;
    private String consignorCountry;
    private String consignorProvince;
    private String consignorPhone;
    private String consignorZipCode;

}
