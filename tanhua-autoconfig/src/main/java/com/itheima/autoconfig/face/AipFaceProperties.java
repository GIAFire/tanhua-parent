package com.itheima.autoconfig.face;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties("tanhua.aip")
public class AipFaceProperties {
    private String appId;
    private String apiKey;
    private String secretKey;
}
