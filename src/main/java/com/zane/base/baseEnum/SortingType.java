package com.zane.base.baseEnum;

import lombok.Data;
import lombok.Getter;

@Getter
public enum SortingType {
    STANDARD_ADDRESS_SORTING("标准地址库","1"),
    USER_INFO_SORTING("个人地址库","2"),
    SELLER_ADDRESS_SORTING("卖家地址库","3");


    private final String sortingType;
    private final String sortingTypeCode;

    SortingType(String sortingType,String sortingTypeCode){
        this.sortingType = sortingType;
        this.sortingTypeCode = sortingTypeCode;
    }

}
