package com.weixin.config;

import me.chanjar.weixin.mp.api.WxMpConfigStorage;
import me.chanjar.weixin.mp.api.WxMpInMemoryConfigStorage;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.WxMpServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
/**
 * 微信初始化相关配置
 * @author Alan Fu
 * @date 2016年6月20日
 * @version 0.0.1
 */
@Configuration
public class WeixinConfig {
	
	@Value("${spring.redis.host}")
	private String host;
	@Value("${spring.redis.port}")
	private int port;
	@Value("${weixin.app.id}")
	private String appid;
	@Value("${weixin.app.secret}")
	private String secret;
	@Value("${weixin.app.token}")
	private String token;
	@Value("${weixin.app.token}")
	private String appUrl;
	
//	@Autowired
//	private RedisTemplate<String, String> redisTemplate;
	
	/**
	 * 初始化微信相关配置
	 * @return
	 */
	@Bean
	public WxMpConfigStorage wxMpConfigStorage(){
		WxMpInMemoryConfigStorage config = new WxMpInMemoryConfigStorage();
		config.setAppId(appid); // 设置微信公众号的appid
		config.setSecret(secret); // 设置微信公众号的app corpSecret
		config.setToken(token); // 设置微信公众号的token
		config.setOauth2redirectUri(appUrl + "/authorize-callback"); //回调地址
		return config;
	}
	
	@Autowired
	private WxMpConfigStorage wxMpInMemoryConfigStorage;
	
	@Bean
	public WxMpService wxMpService(){
		WxMpService wxMpService = new WxMpServiceImpl();
		wxMpService.setWxMpConfigStorage(wxMpConfigStorage());
		return wxMpService;
	}
}
