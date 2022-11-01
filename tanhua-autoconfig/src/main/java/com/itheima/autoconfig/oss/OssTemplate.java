package com.itheima.autoconfig.oss;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.ObjectMetadata;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class OssTemplate {

    private OssProperties ossProperties;

    public OssTemplate(OssProperties properties) {
        this.ossProperties = properties;
    }

    public String upload(String fileName, InputStream inputStream) {

        // Endpoint以杭州为例，其它Region请按实际情况填写。
        String endpoint = ossProperties.getEndpoint();

        // 云账号AccessKey有所有API访问权限，
        String accessKeyId = ossProperties.getAccessKey();
        String accessKeySecret = ossProperties.getSecret();

        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        // 上传文件流。
        // <yourObjectName>表示上传文件到OSS时需要指定包含文件后缀在内的完整路径，例如 images/2020/11/11/asdf.jpg。
        String objectName = "images/" + new SimpleDateFormat("yyyy/MM/dd").format(new Date())
                + "/" + System.currentTimeMillis() + fileName.substring(fileName.lastIndexOf("."));

        // meta设置请求头,解决访问图片地址直接下载
        ObjectMetadata meta = new ObjectMetadata();
        meta.setContentType(getContentType(fileName.substring(fileName.lastIndexOf("."))));
        ossClient.putObject(ossProperties.getBucketName(), objectName, inputStream,meta);

        // 关闭OSSClient。
        ossClient.shutdown();

        return ossProperties.getUrl() + "/" + objectName;
    }

    public  String getContentType(String FilenameExtension) {
        if (FilenameExtension.equalsIgnoreCase(".bmp")) {
            return "image/bmp";
        }
        if (FilenameExtension.equalsIgnoreCase(".gif")) {
            return "image/gif";
        }
        if (FilenameExtension.equalsIgnoreCase(".jpeg") ||
                FilenameExtension.equalsIgnoreCase(".jpg") ||
                FilenameExtension.equalsIgnoreCase(".png")) {
            return "image/jpg";
        }
        return "image/jpg";
    }

}
