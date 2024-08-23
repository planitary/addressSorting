package com.zane.dao;

import lombok.Data;

/**
 * 巴西专用，SenderInfo(FM订单需要)
 */
@Data
public class SenderInfoDo {
    /**
     * 卖家ID
     */
    private String sellerID;
    private String country;
    private String contactCompany;
    private String contacts;
    private String phone;
    private Double latitude;
    private Double longitude;
    private String backupPhone;
    private String province;
    private String city;
    private String area;
    private String address;
    /**
     * FM截单时type一定为seller
     */
    private String addressType;
    private String suburb;
    private String zipCode;
    private String taxID;
    private Integer taxIDType;
}
