package com.weixin.service.dubbo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.weixin.WeiXinApplication;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = WeiXinApplication.class)
@WebAppConfiguration
public class DubboServiceTest {

	@Test
	public void test() {
		
	}

}
