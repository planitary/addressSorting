package com.zane.dao.dto;

import lombok.Data;

//回写表中result字段映射
@Data
public class WriteMqJsonDTO {
    private String addressCode;
    private String addressId;
    private String belongsGeofenceCode;
    private String latitude;
    private String longitude;
    private String newArea;
    private String newCity;
    private String newZone;
    private String newProvince;
    private Integer orgId;
    private String trustValue;
    private Integer status;
    private Integer type;
    private String pointSource;
    private String stationCode;
    private String stationName;
    private String timeStamp;
    private String waybillNo;
    private String originalPointSource;



}
