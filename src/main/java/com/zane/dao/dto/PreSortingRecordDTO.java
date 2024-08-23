package com.zane.dao.dto;

import lombok.Data;

@Data
public class PreSortingRecordDTO {
    private String logisticsType;
    private String businessNumber;
    private String orgId;
    private String country;
    private String city;
    private String province;
    private String area;
    private String zone;
    private StationInfo stationInfo;
    private PointInfoDTO pointInfo;
    private String sortRecordId;
    private String recordId;
    private String fenceCode;
    private WriteBackResultDTO writeBackResultDTO;
    private Boolean isSuccess;
    private String timeStamp;
    private String threeCodeFirst;
    private String threeCodeSecond;
    private String threeCodeThird;
    private String threeCodeFour;
    private String sortType;

}
