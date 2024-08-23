package com.zane.dao.dto;

import lombok.Data;

@Data
public class PageDTO {

    private String country;
    private Integer currentPage;
    private Integer showCount;
}
