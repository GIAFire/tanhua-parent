package com.itheima.app.test;

import com.itheima.autoconfig.huanxin.HuanXinTemplate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class HuanXinTest {
    @Autowired
    private HuanXinTemplate huanXinTemplate;

    @Test
    public void test02()throws Exception{
        huanXinTemplate.register("HX4");
    }
}
