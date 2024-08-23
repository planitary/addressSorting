package com.zane.dao.dto;

import lombok.Data;

@Data
public class CreateOrderDTO {

    private String country;
    // 是否带坐标
    private String isGeoCode;
    // 是否FM订单
    private String isFMOrder;
    // 分拣方式简码
    //    STANDARD_ADDRESS_SORTING("标准地址库","1"),
    //    USER_INFO_SORTING("个人地址库","2"),
    //    SELLER_ADDRESS_SORTING("卖家地址库","3");
    private String sortingCode;
}
