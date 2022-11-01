package com.itheima.app.test;

import cn.hutool.core.io.FileUtil;
import com.baidu.aip.face.AipFace;
import com.baidu.aip.util.Base64Util;
import com.itheima.autoconfig.face.AipFaceTemplate;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AipFaceTest {

    //设置APPID/AK/SK
    public static final String APP_ID = "27962994";
    public static final String API_KEY = "Cc2ROcO9yBttZy41KlDiiNR5";
    public static final String SECRET_KEY = "wDNF4SPPAHF2a95G7YpXGffhPhajwdcq";


    @Autowired
    private AipFaceTemplate template;

    @Test
    public void testAip() throws IOException {
        String filename = "C:\\Users\\lenovo\\Desktop\\Snipaste_2022-10-18_10-00-38.png";
        File file = new File(filename);
        byte[] bytes = FileUtil.readBytes(file);
        System.out.println(template.detect(bytes));
    }

    @Test
    public void test01() throws Exception {
        // 初始化一个AipFace
        AipFace client = new AipFace(APP_ID, API_KEY, SECRET_KEY);

        // 传入可选参数调用接口
        HashMap<String, String> options = new HashMap<String, String>();
        options.put("face_field", "age");
        options.put("max_face_num", "2");
        options.put("face_type", "LIVE");
        options.put("liveness_control", "NONE");

        // 需要审核图片
        String fileName = "C:\\Users\\lenovo\\Desktop\\Snipaste_2022-10-18_10-00-38.png";
        File file = new File(fileName);
        byte[] bytes = FileUtil.readBytes(file);
        String image = Base64Util.encode(bytes);
        // String image = "取决于image_type参数，传入BASE64字符串或URL字符串或FACE_TOKEN字符串";

        String imageType = "BASE64";

        // 人脸检测
        JSONObject res = client.detect(image, imageType, options);
        System.out.println(res.toString(2));
    }
}
