package com.weixin.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.foxinmy.weixin4j.exception.WeixinException;
import com.foxinmy.weixin4j.model.WeixinAccount;
import com.foxinmy.weixin4j.mp.WeixinProxy;
import com.foxinmy.weixin4j.mp.api.OauthApi;
import com.foxinmy.weixin4j.mp.token.WeixinTokenCreator;
import com.foxinmy.weixin4j.token.TokenHolder;
import com.foxinmy.weixin4j.token.TokenStorager;

@Configuration
public class WeixinConfig {

	@Autowired
	private TokenStorager redisTokenStorager;
	
	/**
	 * 初始化微信appid、secret
	 * @return
	 */
	@Bean
	public WeixinAccount weixinAccount(){
		WeixinAccount wxAccount = new WeixinAccount(System.getProperty("weixin.app.appid"),System.getProperty("weixin.app.secret"));
		return wxAccount;
	}
	
	@Bean
	public WeixinProxy weixinProxy() throws WeixinException{
		WeixinProxy proxy =  new WeixinProxy ();
		return proxy;
	}
	
	@Bean
	public OauthApi oauthApi(){
		return new OauthApi();
	}
	@Autowired
	private WeixinProxy proxy;
	
	@Bean 
	public TokenHolder weixinJSTicketTokenHolder() throws WeixinException{
		TokenHolder jsTokenHolder = new TokenHolder(new WeixinTokenCreator(System.getProperty("weixin.app.appid"),System.getProperty("weixin.app.secret")), redisTokenStorager);
		return jsTokenHolder;
	}
}
