package com.zane.dao.dto;

import lombok.Data;
import org.locationtech.jts.util.IntArrayList;

@Data
public class WriteBackItemDTO {
    private String id;
    private Integer isAreaReturn;
    private Integer isCityReturn;
    private Integer isPointReturn;
    private Integer isProvinceReturn;
    private Integer isStationReturn;
    private Integer isZipCodeReturn;
    private Integer isZoneReturn;
    private String writeBackItem;
    private String writeBackItemDesc;
}
