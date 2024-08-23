package com.zane.dao;

import lombok.Data;

@Data
public class OrderCreateParamDo {
    private OrderDetailDo orderParam;
    private String sign;
    private String customerId;
    private String format;
    private String version;
    private String signMethod;
    private String timeStamp;
    private String accessToken;

}
