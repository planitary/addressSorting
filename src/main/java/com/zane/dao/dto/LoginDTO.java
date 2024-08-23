package com.zane.dao.dto;

import lombok.Data;

@Data
public class LoginDTO {
    private String userCode;
    private String password;
    private String lang;
    private String timeZone;


}
