package com.zane.dao;

import lombok.Data;

@Data
public class AddressSituationDo {

    /**
     * Auto-generated: 2024-01-25 16:34:58
     *
     * @author json.cn (i@json.cn)
     * @website http://www.json.cn/java2pojo/
     */

    private String suburb;
    /**
     * FM截单一定为seller
     */
    private String addressType;
    private String backupPhone;
    private String contactCompany;
    private String businessCode;
    private String idCardFrontImg;
    private Double latitude;
    private String situationType;
    private String street;
    private String taxID;
    private String area;
    private String country;
    private String email;
    private String idCardBackImg;
    private String idNo;
    private Double longitude;
    private String province;
    private String zipCode;
    private String address;
    private String contacts;
    private String phone;
    private String stateRegisterNo;
    private String city;
    private Integer taxIDType;
}
