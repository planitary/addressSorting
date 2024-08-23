package com.zane.core.base;

import com.zane.dao.dto.LoginDTO;

public interface BaseService {

    /**
     * 登录密码加密
     */
    String encryptPassword(String password);


    /**
     * 登录
     * @param userCode          用户code
     * @param password          RSA加密后的密码
     * @return
     */
    void login(LoginDTO loginDTO);
}
