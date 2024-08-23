package com.zane.core;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "app")
@Data
//全局url配置文件
public class GlobalUrlConfig {

    private String globalUrlPrefix;

}
