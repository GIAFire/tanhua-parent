package com.itheima;

import com.itheima.autoconfig.face.AipFaceProperties;
import com.itheima.autoconfig.face.AipFaceTemplate;
import com.itheima.autoconfig.huanxin.HuanXinProperties;
import com.itheima.autoconfig.huanxin.HuanXinTemplate;
import com.itheima.autoconfig.lvwang.AliyunGreenTemplate;
import com.itheima.autoconfig.lvwang.GreenProperties;
import com.itheima.autoconfig.oss.OssProperties;
import com.itheima.autoconfig.oss.OssTemplate;
import com.itheima.autoconfig.sms.SmsProperties;
import com.itheima.autoconfig.sms.SmsTemplate;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({
        SmsProperties.class,//加载短信配置
        OssProperties.class,//加载oss配置
        AipFaceProperties.class,//加载人脸识别配置
        HuanXinProperties.class,//加载环信配置
        GreenProperties.class
})
public class TanhuaAutoConfiguration {

    @Bean
    public AliyunGreenTemplate aliyunGreenTemplate(GreenProperties properties) {
        return new AliyunGreenTemplate(properties);
    }

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

    @Bean
    public HuanXinTemplate huanXinTemplate(HuanXinProperties huanXinProperties) {
        return new HuanXinTemplate(huanXinProperties);
    }
}
