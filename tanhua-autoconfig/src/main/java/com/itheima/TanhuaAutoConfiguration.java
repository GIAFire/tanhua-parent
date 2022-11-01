package com.itheima;

import com.itheima.autoconfig.face.AipFaceProperties;
import com.itheima.autoconfig.face.AipFaceTemplate;
import com.itheima.autoconfig.oss.OssProperties;
import com.itheima.autoconfig.oss.OssTemplate;
import com.itheima.autoconfig.sms.SmsProperties;
import com.itheima.autoconfig.sms.SmsTemplate;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({
        SmsProperties.class,
        OssProperties.class,
        AipFaceProperties.class
})
public class TanhuaAutoConfiguration {

    @Bean
    public SmsTemplate smsTemplate(SmsProperties smsProperties) {
        return new SmsTemplate(smsProperties);
    }

    @Bean
    public OssTemplate ossTemplate(OssProperties ossProperties) {
        return new OssTemplate(ossProperties);
    }

    @Bean
    public AipFaceTemplate aipFaceTemplate(AipFaceProperties aipFaceProperties) {
        return new AipFaceTemplate(aipFaceProperties);
    }
}
