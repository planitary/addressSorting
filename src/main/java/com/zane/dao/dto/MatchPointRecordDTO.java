package com.zane.dao.dto;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zane.dao.typeHandler.PointTypeHandler;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("cs_address_match_point_record")
public class MatchPointRecordDTO {
    @TableId(value = "id")
    private Long id;
    private String businessType;
    private String businessNumber;
    private String country;
    private String province;
    private String city;
    private String area;
    private String zipCode;
    private String streetName;
    private String suburb;
    private String internalNumber;
    private String externalNumber;
    private String detailAddress;
    private Integer isMatchSuccess;
//    @TableField(typeHandler = PointTypeHandler.class)
    private String point;


    private String pointSource;
    private String addressCode;
    private String trustValue;
    private String addressSimilary;
    private String addressMatchStrategy;
    @TableLogic
    private Integer isDelete;
    private LocalDateTime createDate;
    private String createUserCode;
    private String createUserName;
    private String lastUpdDate;
    private String lastUpdUserCode;
    private String lastUpdUserName;
    private Long addressId;
    private Integer addressMatchPath;
    private Integer addressType;


}
