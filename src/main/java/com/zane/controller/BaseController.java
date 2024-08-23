package com.zane.controller;

import com.alibaba.fastjson2.JSON;
import com.zane.base.GlobalVariables;
import com.zane.core.base.BaseService;
import com.zane.dao.dto.LoginDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
public class BaseController {

    @Resource
    BaseService baseService;

    @PostMapping("/base/login")
    public String login(@RequestBody LoginDTO loginDTO){
        loginDTO.setLang("zh_CN");
        loginDTO.setTimeZone("+8");
        log.debug("登录参数:{}",loginDTO);
        Map<String,Object> resMap = new HashMap<>();
        baseService.login(loginDTO);
        resMap.put("accessToken", GlobalVariables.AUTHORIZATION_CODE);
        String resJSON = JSON.toJSONString(resMap);
        return resJSON;
    }
}
