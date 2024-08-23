package com.zane.dao;

import lombok.Data;

import java.io.DataInputStream;
import java.util.List;

/**
 * 巴西专用
 */

@Data
public class ConsigneeInfoDo {
    private String country;
    private String idNo;
    private String birthDate;
    private String idExpiredDate;
    private String calendarType;
    private String consignee;
    private String contactCompany;
    private String contacts;
    private String phone;
    private String backupPhone;
    private String province;
    private String city;
    private String area;
    private String address;
    private String zipCode;
    private String consigneeEmail;
    private List<Integer> la;
    private Double latitude;
    private Double longitude;
    private String taxID;
    private int taxIDType;
    /**
     * FM截单时type一定为seller
     */
    private String addressType;
    private String sellerId;
}
