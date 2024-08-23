package com.zane.dao;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class UserInfoDo {

    @TableId(value = "id")
    private Long id;
    private Long standardId;
    private String userCode;
    private String phone;
    private String mobile;
    private String country;
    private String province;
    private String city;
    private String area;
    private String zipCode;
    private String suburb;
    private String street;
    private String internalNumber;
    private String externalNumber;
    private String detailAddress;
    private String addressCode;
    private String point;
    private Integer pointSource;
    private String trustValue;
    private String extend;
    private String createTime;
    private String updateTime;
    private Integer isDelete;
}
