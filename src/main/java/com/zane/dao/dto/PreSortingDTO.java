package com.zane.dao.dto;

import lombok.Data;

@Data
// 预分拣接口必传参数
public class PreSortingDTO {

    private String area;
    private String businessNumber;
    private String city;
    private String country;
    private String detailAddress;
    private Integer orgId;
    private String phone;
    private String province;
    private String zipCode;
    private PointInfoDTO point;
}
