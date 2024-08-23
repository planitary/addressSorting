package com.zane.base.baseEnum;

import lombok.Data;
import lombok.Getter;

@Getter
public enum MatchFetchWay {
    USER_DEFINITION("1","自带经纬度"),
    USER_ADDRESS_TERM("2","历史地址"),
    KEY_WORD_TERM("3","关键词地址库"),
    STANDARD_ADDRESS_MATCH("4","官方地址模糊匹配"),
    USER_ADDRESS_MATCH("5","用户地址模糊匹配"),
    GOOGLE_MAPS("6","调用谷歌获取"),
    HISTORY_REASON("7","导入历史数据"),
    TEXT_PARSE_POINT("8","用户地址文本解析"),
    USER_APPOINTMENT("9","用户预约"),
    WHATS_APP("10","whatsUp"),
    DELIVERED("11","用户签收"),
    USER_INFO_MATCH("12","个人地址库匹配"),
    STANDARD_POINT_MATCH("13","标准地址库匹配"),
    MAP_SERVICE("40","地图服务商");

    private final String sortingType;
    private final String sortingTypeCode;

    MatchFetchWay(String sortingTypeCode,String sortingType){
        this.sortingTypeCode = sortingTypeCode;
        this.sortingType = sortingType;
    }

}
