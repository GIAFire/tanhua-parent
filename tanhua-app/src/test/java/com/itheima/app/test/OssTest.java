package com.itheima.app.test;

import com.itheima.autoconfig.oss.OssTemplate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OssTest {

    @Autowired
    private OssTemplate ossTemplate;

    @Test
    public void testUploadFile() throws FileNotFoundException {
        String file = "C:\\Users\\lenovo\\Desktop\\吉他谱\\程艾影1.jpg";
        String url = ossTemplate.upload(file, new FileInputStream(file));
        System.out.println(url);
    }

}
