package com.weixin.controller;

import javax.servlet.http.HttpServletRequest;

import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.WxMpXmlOutMessage;
import me.chanjar.weixin.mp.bean.result.WxMpOAuth2AccessToken;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.weixin.common.WeiXinConstants;
import com.weixin.service.UserService;
import com.weixin.utils.Result;


@Controller
public class WeixinController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(WeixinController.class);
	
	
	@Autowired
	private UserService userService;
	@Value("${weixin.app.token}")
	private String token;
	
	@Autowired
	private WxMpService wxMpService;
	
	/**
	 * 微信签名 加密/校验流程如下： 
	 * 1. 将token、timestamp、nonce三个参数进行字典序排序 
	 * 2.将三个参数字符串拼接成一个字符串进行sha1加密 
	 * 3. 开发者获得加密后的字符串可与signature对比，标识该请求来源于微信
	 * @param signature 微信加密签名
	 * @param timestamp 生成签名的时间戳
	 * @param nonce 随机数
	 * @param echostr 生成签名的随机串
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/weixin", method = RequestMethod.GET)
	public String verify(String signature, String timestamp, String nonce, String echostr) {
		LOGGER.info("------------------微信签名校验----------------------");
		LOGGER.info("signature:" + signature);
		LOGGER.info("timestamp:" + timestamp);
		LOGGER.info("nonce:" + nonce);
		LOGGER.info("token:" + token);
		
		if (!wxMpService.checkSignature(timestamp, nonce, signature)) {
			  // 消息不合法
			LOGGER.error("微信签名验证失败...");
			return "";
		}else{
			LOGGER.info("微信签名验证成功...");
			return echostr;
		}
		
		/*if (SignUtil.checkSignature(signature, timestamp, nonce, token)) {
			LOGGER.info("微信签名验证成功...");
			return echostr;
		} else {
			LOGGER.info("微信签名验证失败...");
			return "";
		}*/
	}
	
	/**
	 * 微信事件推送
	 * 
	 * @param msg
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/weixin", method = RequestMethod.POST)
	public String index(@RequestBody String msg) {
		try {
			Document document = DocumentHelper.parseText(msg);
			Element root = document.getRootElement();
			LOGGER.info(root.asXML());
			String event = root.elementText("Event");
			String openId = root.elementText("FromUserName");
			String eventKey = root.elementText("EventKey");
			String messageType = root.elementText("MsgType");
			String content = root.elementText("Content");
			//单击注册菜单
			if (content != null && (WeiXinConstants.REGISTER.equals(content.replaceAll(" ", "")) || WeiXinConstants.I_WANT_REGISTER.equals(content.replaceAll(" ", "")))) {
				LOGGER.info("注册");
				String registerUrl = wxMpService.oauth2buildAuthorizationUrl(WxConsts.OAUTH2_SCOPE_USER_INFO, "/mobileRegister");
				return WxMpXmlOutMessage.TEXT().content("您好，这是你的快速注册通道\ue056，<a href='" + registerUrl + "'>我要注册</a>!").fromUser(root.elementText("ToUserName")).toUser(openId).build().toXml();
			}
			
			LOGGER.info("微信用户：" + openId);
		} catch (Exception e) {
			LOGGER.error("推送消息异常", e);
		}
		return "";
	}
	
	
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
		LOGGER.info("微信请求入口");
		//获得access token
		WxMpOAuth2AccessToken wxMpOAuth2AccessToken = wxMpService.oauth2getAccessToken(code);
		//验证access_token
		boolean flag = wxMpService.oauth2validateAccessToken(wxMpOAuth2AccessToken);
		return null;
	}
	
	
	@RequestMapping("/weixin/getWeixinJSConfig")
	@ResponseBody
	public Result<JSONObject> getWeixinJSConfig(String url) {
		return null;

	}
	
	
	
}
