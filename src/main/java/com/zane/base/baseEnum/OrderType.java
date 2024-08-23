package com.zane.base.baseEnum;

import lombok.Getter;

@Getter
public enum OrderType {

    FM_ORDER("fm订单","1200"),
    COMMON_ORDER("普通订单","100");


    private final String orderType;
    private final String orderTypeCode;

    OrderType(String orderType,String orderTypeCode){
        this.orderType = orderType;
        this.orderTypeCode = orderTypeCode;
    }
}
