package com.zane.dao.dto;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("cs_address_sorting_write_record")
public class WriteBackResultDTO {
    private Long id;
    private Long orgId;
    private Boolean isProvinceReturn;
    private Boolean isCityReturn;
    private Boolean isStationReturn;
    private Boolean isZoneReturn;
    private Boolean isAreaReturn;
    private Boolean isPointReturn;
    private Boolean isZipCodeReturn;
    private Boolean isFirstReturn;
    private Boolean isSecondReturn;
    private Boolean isThirdReturn;
    private Boolean isFourReturn;
    private Long sortRecordId;
    private String waybillNo;
    private String result;

}
