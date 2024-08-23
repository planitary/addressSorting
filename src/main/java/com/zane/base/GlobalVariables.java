package com.zane.base;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

// 全局参数配置
@Component
public class GlobalVariables {

    // 登录token
    public static String AUTHORIZATION_CODE = "";

    public static List<?> SORT_TYPE_LIST;

    public static Map<String,Object> WRITE_BACK_ITEM_WHEN_SUCCESS = new HashMap<>();

    public static String USER_ADDRESS_TRUST_VALUE;
    public static String STANDARD_ADDRESS_TRUST_VALUE;



}
