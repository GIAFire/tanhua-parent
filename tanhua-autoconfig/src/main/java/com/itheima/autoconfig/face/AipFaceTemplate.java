package com.itheima.autoconfig.face;
import com.baidu.aip.face.AipFace;
import com.baidu.aip.util.Base64Util;
import org.json.JSONObject;

import java.util.HashMap;

public class AipFaceTemplate {

    private AipFaceProperties properties;

    public AipFaceTemplate(AipFaceProperties properties) {
        this.properties = properties;
    }

    public boolean detect(byte [] body) {

        AipFace client = new AipFace(properties.getAppId(), properties.getApiKey(), properties.getSecretKey());
        // 传入可选参数调用接口
        HashMap<String, String> options = new HashMap<String, String>();
        options.put("face_field", "age");
        options.put("max_face_num", "2");
        options.put("face_type", "LIVE");
        options.put("liveness_control", "NONE");


        String imageType = "BASE64";

        String image = Base64Util.encode(body);

        // 人脸检测
        JSONObject res = client.detect(image, imageType, options);

        int code = res.getInt("error_code");

        return code == 0;
    }
}
