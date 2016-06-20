package com.weixin.controller;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.foxinmy.weixin4j.exception.WeixinException;
import com.foxinmy.weixin4j.mp.api.OauthApi;
import com.foxinmy.weixin4j.mp.model.OauthToken;
import com.foxinmy.weixin4j.token.TokenHolder;
import com.foxinmy.weixin4j.util.DigestUtil;
import com.foxinmy.weixin4j.util.MapUtil;
import com.weixin.service.userdetails.CustomerService;
import com.weixin.utils.Result;


@Controller
public class WeixController {
	private Logger LOGGER = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private OauthApi oauthApi;
	@Autowired
	private TokenHolder weixinJSTicketTokenHolder;
	@Autowired
	private CustomerService customerService;
	
	
	/**
	 * 微信入口(使用OAuth2.0认证)
	 * @param code
	 * @param state
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/authorizeCallback")
	public String authorizeCallback(String code, String state, HttpServletRequest request) throws Exception {
		OauthToken oauthToken = oauthApi.getOauthToken(code);
		//记录用户使用 的设备
//		customerService.setUserDevice(request, DianaDevice.JUPITER.name());
		return "redirect:" + state + "?openId=" + oauthToken.getOpenId();
	}
	
	@RequestMapping("/weixin/getWeixinJSConfig")
	@ResponseBody
	public Result<JSONObject> getWeixinJSConfig(String url) {
		Result<JSONObject> result = new Result<JSONObject>(Result.Type.SUCCESS);
		JSONObject conf = new JSONObject();
		try {
			conf.put("jsapi_ticket", weixinJSTicketTokenHolder.getAccessToken());
		} catch (WeixinException e) {
			result.setType(Result.Type.FAILURE);
			result.addMessage("获取sapi_ticket失败");
			LOGGER.error("获取sapi_ticket失败", e);
			return result;
		}
		conf.put("noncestr", "123456");
		conf.put("timestamp", new Date().getTime() / 1000 + "");
		conf.put("url", url);
		StringBuilder sb = new StringBuilder();
		// a--->string1
		sb.append(MapUtil.toJoinString(conf, false, false));
		String signature = DigestUtil.SHA1(sb.toString());
		conf.put("signature", signature);
		conf.put("appId", System.getProperty("weixin.app.appid"));
		result.setData(conf);
		return result;

	}
}
