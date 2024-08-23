package com.zane.dao.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zane.dao.typeHandler.PointTypeHandler;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("cs_address_sorting_record")
public class OrderSortingRecordDTO {

    @TableId(value = "id")
    private Long id;
    private Long orgId;
    private String sortType;
    private String inputBusinessDate;
    private String inputCountry;
    private String inputBusinessNumber;
    private String inputCity;
    private String inputArea;
    private String inputAddress;
    private String inputZipCode;
//    @TableField(typeHandler = PointTypeHandler.class)
    private String point;

    private String inputStationCode;
    private String inputGeofenceCode;
    private String inputFullAddress;
    private Integer isSortSuccess;
    private String sortGeocodingSource;
    private String sortAddressCode;
    private String sortAddress;
    private String sortAddressSimilarity;
    private String sortGeofenceCode;
    private String sortProvince;
    private String sortCity;
    private String sortArea;
    private String sortZipCode;
    @TableLogic
    private Integer isDelete;
    private String sortStationCode;
    private LocalDateTime createDate;
    private String createUserCode;
    private String createUserName;
    private LocalDateTime lastUpdDate;
    private String lastUpdUserCode;
    private String lastUpdUserName;
    private String inputAddressCode;
    private String inputGeocodingSource;
    private Long inputAddressId;
    private String inputGeocodingFetchWay;
    private String sortZone;
    private String sortStrategy;
    private String sortRecordId;
    private Integer sortPath;
    private Long sortClientConfigId;
    private Long sortCountryConfigId;
    private String clientCode;
    private String clientName;
    private Integer addressType;


}
