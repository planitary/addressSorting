package com.zane.dao;

import lombok.Data;

/**
 * 巴西下单用，extraInfo
 */
@Data
public class ExtraInfoDo {
    private Integer bagCount;
    private String bagNo;
    private String clientScanNo;
    private String clientSortCode;
}
