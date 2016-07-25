package com.weixin.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.bean.WxMenu;
import me.chanjar.weixin.common.bean.WxMenu.WxMenuButton;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.WxMpCustomMessage;
import me.chanjar.weixin.mp.bean.WxMpXmlOutMessage;
import me.chanjar.weixin.mp.bean.WxMpXmlOutNewsMessage;

import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.weixin.domain.Users;
import com.weixin.domain.WeChatAccessToken;
import com.weixin.exception.WeiXinConnectionException;
import com.weixin.service.userdetails.UserDetailsImpl;
import com.weixin.utils.WeiXinConnectionUtil;
import com.weixin.vo.AuthorizeAccessToken;
import com.weixin.vo.InvestSuccessMsg;
import com.weixin.vo.WxMessageRes;
import com.weixin.vo.WxUserInfo;

@Service
public class WeiXinService {

	private static final Logger LOGGER = LoggerFactory.getLogger(WeiXinService.class);

	@Value("${weixin.app.url}")
	private String systemAppUrl;
	@Value("${weixin.app.id}")
	private String appid; // 公众号的唯一标识
	@Value("${weixin.app.secret}")
	private String secret; // 公众号的appsecret

	@Value("${weixin.app.templateId}")
	private String templateId; // 消息模板ID
	
	@Autowired
	private AccessTokenService accessTokenService;
	
	@Autowired
	private UserService userService;

	@Autowired
	private UserDetailsService customUserDetailsService;

	@Autowired
	private WxMpService wxMpService;

	/**
	 * 刷新accesstoken
	 * 
	 * @return
	 */
	public WeChatAccessToken refreshAccessToken() {
		WeChatAccessToken accessToken = null;
		try {
			accessToken = accessTokenService.getAccessToken(false);
		} catch (Exception e) {
			accessToken = accessTokenService.getAccessToken(true);
		}
		return accessToken;
	}

	/**
	 * 返回 AccessToken
	 */
	public String getAccessToken() {
		return accessTokenService.getAccessToken(false).getAccessToken();
	}

	/**
	 * 返回 JsApiTicket
	 */
	public String getJsApi() {
		return accessTokenService.getAccessToken(false).getJsApiTicket();
	}

	/***
	 * 通过微信授权获取用户信息
	 * 
	 * @param code
	 * 
	 * @return
	 */
	public WxUserInfo getUserInfo(AuthorizeAccessToken authorizeAccessToken, String code) throws MalformedURLException, IOException {
		try {

			if (StringUtils.isEmpty(authorizeAccessToken.getAccess_token())) {
				LOGGER.info("换取换取网页授权access_token请求参数:{}", appid, secret, code);
				authorizeAccessToken = WeiXinConnectionUtil.getAuthorizeAccessToken(appid, secret, code);
				LOGGER.info("获取AuthorizeAccessToken:{}", authorizeAccessToken);
			}

			String userInfo = WeiXinConnectionUtil.getUserInfo(authorizeAccessToken.getAccess_token(), authorizeAccessToken.getOpenid());
			WxUserInfo wxUser = (WxUserInfo) JSON.parseObject(userInfo, WxUserInfo.class);
			LOGGER.info("获取userinfo:{}", userInfo);
			if (wxUser.getErrcode() != null) {
				LOGGER.info("get AccessToken ErrCode Info: " + JSON.toJSONString(authorizeAccessToken) + "--> Code:" + code);

				LOGGER.info(wxUser.getErrmsg());
				return null;
			}
			return wxUser;
		} catch (IOException e) {
			throw new WeiXinConnectionException("获取userinfo出错", e);
		}

	}

	/**
	 * 向指定用户发送一条普通文本消息
	 * 
	 * @param openId
	 *            客户微信号
	 * @param content
	 *            消息内容
	 * @return 发送结果
	 * @throws IOException
	 */
	public WxMessageRes sendTextMessage(String openId, String content) throws IOException {
		LOGGER.info("发送文本消(openId：{} | content:{})。", openId, content);
		WxMpCustomMessage msg = new WxMpCustomMessage();
		msg.setToUser(openId);
		msg.setContent(content);
		return WeiXinConnectionUtil.sendCustomerMessage(this.getAccessToken(), msg);
	}

	/**
	 * 发送微信模板消息
	 * 
	 * @param openId
	 * @param content
	 * @return
	 * @throws IOException
	 * @throws JSONException
	 */
	@Async
	public WxMessageRes sendTemplateMessage(InvestSuccessMsg msg) {
		JSONObject json = new JSONObject();
		try {
			json.put("touser", msg.getOpenId());
			json.put("template_id", templateId);
			json.put("url", "");
			JSONObject data = new JSONObject();
			JSONObject first = new JSONObject();
			first.put("value", "您好,您购买的理财产品已交易成功");
			first.put("color", "#173177");
			data.put("first", first);
			JSONObject keyword1 = new JSONObject();
			keyword1.put("value", msg.getProductName());
			keyword1.put("color", "#173177");
			data.put("keyword1", keyword1);
			JSONObject keyword2 = new JSONObject();
			keyword2.put("value", msg.getRate());
			keyword2.put("color", "#173177");
			data.put("keyword2", keyword2);
			JSONObject keyword3 = new JSONObject();
			keyword3.put("value", msg.getPeriod());
			keyword3.put("color", "#173177");
			data.put("keyword3", keyword3);
			JSONObject keyword4 = new JSONObject();
			keyword4.put("value", msg.getAmount());
			keyword4.put("color", "#173177");
			data.put("keyword4", keyword4);
			JSONObject keyword5 = new JSONObject();
			keyword5.put("value", msg.getInterest());
			keyword5.put("color", "#173177");
			data.put("keyword5", keyword5);
			JSONObject remark = new JSONObject();
			remark.put("value", "你可以到交易记录查看更多信息");
			remark.put("color", "#173177");
			data.put("remark", "remark");
			json.put("data", data);
			String content = WeiXinConnectionUtil.sendTemplateMessage(this.getAccessToken(), json.toString());
			LOGGER.info("微信推送消息响应:" + content);
		} catch (JSONException e) {
			LOGGER.error("投资成功推送消息失败", e);
		} catch (IOException e) {
			LOGGER.error("投资成功推送消息失败", e);
		}
		return null;
	}

	public WxMessageRes createMenu() throws IOException {
		WxMenu menu = new WxMenu();
		List<WxMenuButton> buttons = new ArrayList<WxMenuButton>();
		
		WxMenuButton bt = new WxMenuButton();
		bt.setName("惠赚钱");
		bt.setType("view");
		bt.setUrl(wxMpService.oauth2buildAuthorizationUrl(WxConsts.OAUTH2_SCOPE_BASE, null));
		menu.setButtons(buttons);
		LOGGER.info("发起创建菜单请求：{}", menu);
		WxMessageRes res = WeiXinConnectionUtil.createMenu(this.getAccessToken(), menu);
		LOGGER.info("收到创建菜单响应：{}", res);
		return res;
	}

	/**
	 * 通过code换取网页授权access_token
	 * 这里通过code换取的是一个特殊的网页授权access_token,与基础支持中的access_token
	 * （该access_token用于调用其他接口
	 * ）不同。公众号可通过下述接口来获取网页授权access_token。如果网页授权的作用域为snsapi_base
	 * ，则本步骤中获取到网页授权access_token的同时，也获取到了openid，snsapi_base式的网页授权流程即到此为止。
	 * 
	 * @param code
	 *            填写第一步获取的code参数
	 *            code作为换取access_token的票据，每次用户授权带上的code将不一样，code只能使用一次
	 *            ，5分钟未被使用自动过期。
	 * @return AuthorizeAccessToken
	 */
	public AuthorizeAccessToken getAuthorizeAccessToken(String code) {
		try {
			AuthorizeAccessToken authorizeAccessToken = new AuthorizeAccessToken();
			Authentication authen = SecurityContextHolder.getContext().getAuthentication();
			if (!"anonymousUser".equals(authen.getPrincipal())) {
				UserDetailsImpl userDetails = (UserDetailsImpl) authen.getPrincipal();
				authorizeAccessToken.setOpenid(userDetails.getOpenId());
//				authorizeAccessToken.setAccess_token(userDetails.getAccessToken());
				authorizeAccessToken.setErrcode(null);
			} else {
				LOGGER.info("换取换取网页授权access_token请求参数:{}", appid, secret, code);
				authorizeAccessToken = WeiXinConnectionUtil.getAuthorizeAccessToken(appid, secret, code);
			}
			LOGGER.info("获取AuthorizeAccessToken:{}", authorizeAccessToken);
			if (authorizeAccessToken.getErrcode() != null) {
				LOGGER.info("get AccessToken ErrCode Info: " + JSON.toJSONString(authorizeAccessToken) + "Code :" + code);
				LOGGER.info(authorizeAccessToken.getErrmsg());
				return null;
			}
			return authorizeAccessToken;
		} catch (Exception e) {
			throw new WeiXinConnectionException("获取AuthorizeAccessToken出错", e);
		}
	}

	public String getAppid() {
		return appid;
	}

	/**
	 * 首次关注用户推送图文消息
	 * 
	 * @param openId
	 * @param eventKey
	 * @param root
	 * @return
	 */
	public String userSubscribe(String openId, String eventKey, Element root) {
		Users user = userService.findUserByOpenId(openId);
		if (null == user) {
			user = new Users(openId);
			// 渠道来源用户 记录用户来源(二维码链接产生时写入渠道标识)
			if (StringUtils.isNotEmpty(eventKey)) {
				String code[] = eventKey.split("_");
//				user.setUserSource(code[1]);
				LOGGER.info("创建渠道用户,来源：" + code[1]);
			}
			userService.save(user);
			//写入security,开启redis后会自动写入redis
//			customUserDetailsService.pushUserToSecurity(openId);
			LOGGER.info("欢迎消息推送：");
		}
		
		/**推送信息*/
		
		WxMpXmlOutNewsMessage.Item activity = new WxMpXmlOutNewsMessage.Item();
		// 猜电影
		String yipoUrl = wxMpService.oauth2buildAuthorizationUrl(WxConsts.OAUTH2_SCOPE_BASE, "/diana/dianaLogin");
		activity.setDescription("电影票+特权+精美礼品，一步到位");
		activity.setPicUrl(systemAppUrl + "/images/yipiao.png");
		activity.setTitle("电影票+特权+精美礼品，一步到位");
		// activity.setUrl("http://mp.weixin.qq.com/s?__biz=MzI3MTAwMTk4NA==&mid=402739915&idx=1&sn=bc9bbac9d407a7a71e98e87d02d966ef#rd");
		activity.setUrl(yipoUrl);

		// 新手专项
		WxMpXmlOutNewsMessage.Item product = new WxMpXmlOutNewsMessage.Item();
		String productUrl = wxMpService.oauth2buildAuthorizationUrl(WxConsts.OAUTH2_SCOPE_BASE, "/initialProduct");
		product.setDescription("理财年化收益高达17%，让你打一份工，赚两份钱");
		product.setPicUrl(systemAppUrl + "/images/loveyijia/weixintuisong@1x.jpg");
		product.setTitle("理财年化收益高达17%，让你打一份工，赚两份钱");
		product.setUrl(productUrl);
		// 影业汇
		WxMpXmlOutNewsMessage.Item move = new WxMpXmlOutNewsMessage.Item();
		String moveUrl = wxMpService.oauth2buildAuthorizationUrl(WxConsts.OAUTH2_SCOPE_BASE, "/activity/movie/movieActivity");
		move.setDescription("影业汇，一年期13%收益再加480元超值好礼");
		move.setPicUrl(systemAppUrl + "/images/loveyijia/weixintuisong@4x.jpg");
		move.setTitle("影业汇，一年期13%收益再加480元超值好礼");
		move.setUrl(moveUrl);

		WxMpXmlOutNewsMessage message = WxMpXmlOutMessage.NEWS().fromUser(root.elementText("ToUserName")).toUser(openId).addArticle(activity).addArticle(product).addArticle(move).build();
		return message.toXml();
	}
	
	/**
	 * 自动回复信息
	 * @param openId
	 * @param document
	 * @return
	 * @throws IOException
	 */
	public String autoReply(String openId, Document document) throws IOException {
		Calendar calendar = Calendar.getInstance();
		int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
		int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
		Calendar endTime = Calendar.getInstance();
		endTime.set(Calendar.HOUR_OF_DAY, 17);
		endTime.set(Calendar.MINUTE, 30);
		Element root = document.getRootElement();
		if (dayOfWeek < 2 || dayOfWeek > 6 || hourOfDay < 9 || calendar.after(endTime)) {
			LOGGER.info("非工作时间自动回复");
			return WxMpXmlOutMessage.TEXT().content("主人您好呀，小易正在充电中~~小易工作时间为每周一至周五9:00-17:30，给您带来不便，小易非常抱歉。祝您生活愉快，笑口常开~~么么哒。").fromUser(root.elementText("ToUserName")).toUser(openId).build().toXml();
		} else {
			LOGGER.info("转发多客服");
			String msg = MessageFormat.format("<xml><ToUserName><![CDATA[{0}]]></ToUserName><FromUserName><![CDATA[{1}]]></FromUserName><CreateTime>{2}</CreateTime><MsgType><![CDATA[transfer_customer_service]]></MsgType></xml>", root.elementText("FromUserName"),
					root.elementText("ToUserName"), root.elementTextTrim("CreateTime"));
			LOGGER.info(msg);
			return msg;
		}
	}

	/**
	 * 获取用户二维码信息
	 * 
	 * @param param
	 * @return
	 */
	/*@Transactional
	public UserCode getUserCode(Long param) {
		String ticket = null;
		Long seconds = 604800L;
		UserCode userCode = null;
		Customer customer = customerService.findById(param);
		List<UserCode> list = userCodeDao.findByCustomerOrderByIdDesc(param);
		if (list != null && !list.isEmpty()) {
			userCode = list.get(0);
		}
		// 判读用户是否已经生成过二维码
		if (userCode == null) {
			if (customer.getStatus().equals(Customer.Status.认证.name())) { // 认证用户生成永久二维码
				ticket = getTicket(UserCode.Type.QR_LIMIT_STR_SCENE.name(), param, null);
				if (StringUtils.isNotEmpty(ticket)) {
					userCode = new UserCode(param, ticket, null, UserCode.Type.QR_LIMIT_STR_SCENE.name(), UserCode.Status.有效.name(), param.toString());
					userCodeDao.save(userCode);
					LOGGER.info("获取openId为" + customer.getOpenId() + "，状态为：" + customer.getStatus() + "的用户，永久二维码（首次）已生成......");
				}
			} else { // 非认证用户生成临时二维码
				ticket = getTicket(UserCode.Type.QR_SCENE.name(), param, seconds);
				if (StringUtils.isNotEmpty(ticket)) {
					userCode = new UserCode(param, ticket, getExpiryDate(seconds), UserCode.Type.QR_SCENE.name(), UserCode.Status.有效.name(), param.toString());
					userCodeDao.save(userCode);
					LOGGER.info("获取openId为：" + customer.getOpenId() + "，状态为：" + customer.getStatus() + "的用户，临时二维码（首次）已生成......");
				}
			}
			return userCode;
		}
		// 如果非认证用户的临时二维码失效，则重新生成
		if (!customer.getStatus().equals(Customer.Status.认证.name()) && System.currentTimeMillis() > userCode.getExpiryDate().getTime()) {
			ticket = getTicket(UserCode.Type.QR_SCENE.name(), param, seconds);
			if (StringUtils.isNotEmpty(ticket)) {
				userCode.setTicket(ticket);
				userCode.setExpiryDate(getExpiryDate(seconds));
				userCodeDao.save(userCode);
				LOGGER.info("获取openId为：" + customer.getOpenId() + "，状态为：" + customer.getStatus() + "的用户，临时二维码已失效，现已经重新生成......");
			}
		}
		// 如果认证用户仍为临时二维码，则更新为永久二维码
		if (customer.getStatus().equals(Customer.Status.认证.name()) && userCode.getType().equals(UserCode.Type.QR_SCENE.name())) {
			ticket = getTicket(UserCode.Type.QR_LIMIT_STR_SCENE.name(), param, null);
			if (StringUtils.isNotEmpty(ticket)) {
				userCode.setType(UserCode.Type.QR_LIMIT_STR_SCENE.name());
				userCode.setTicket(ticket);
				userCodeDao.save(userCode);
				LOGGER.info("获取openId为：" + customer.getOpenId() + "，状态为：" + customer.getStatus() + "的用户，临时二维码已更新为永久二维码......");
			}
		}
		return userCode;
	}*/

	/** 计算二维码失效日期 */
	public Date getExpiryDate(Long seconds) {
		Date date = null;
		if (seconds != null) {
			// 计算二维码URL失效期
			Long expiryDate = System.currentTimeMillis() + (seconds - 800) * 1000;
			date = new Date(expiryDate);
		}
		return date;
	}

	/**
	 * 返回二维码ticket,通过ticket获取带参数的二维码链接
	 * 
	 * @param type
	 *            二维码类型 (临时：QR_SCENE , 永久:QR_LIMIT_SCENE)
	 * @param param
	 *            二维码参数
	 * @param seconds
	 *            二维码有效时间，以秒为单位。 最大不超过604800（即7天）
	 * 
	 * @return
	 */
	public String getTicket(String type, Long param, Long seconds) {
		String ticket = null;
		try {
			JSONObject data = new JSONObject();
			JSONObject scene = new JSONObject();
			JSONObject parameter = new JSONObject();
			if ("QR_SCENE".equals(type)) {
				data.put("expire_seconds", seconds);
			}
			data.put("action_name", type);
			if ("QR_LIMIT_STR_SCENE".equals(type)) {
				parameter.put("scene_str", param + "");
			}
			if ("QR_SCENE".equals(type)) {
				parameter.put("scene_id", param);
			}

			scene.put("scene", parameter);
			data.put("action_info", scene);
			LOGGER.info("Ticket请求参数: {}", data.toString());
			AuthorizeAccessToken wxAccessToken = WeiXinConnectionUtil.getAccessToken(appid, secret);
			String message = WeiXinConnectionUtil.getTicket(wxAccessToken.getAccess_token(), data.toString());
			LOGGER.info("创建二维码ticket返回数据(message:{})", message);
			JSONObject jsonObject = JSON.parseObject(message);
			// 判读微信返回的数据是否包含ticket
			if (message.contains("ticket")) {
				ticket = URLEncoder.encode(jsonObject.getString("ticket"), "utf-8");
			}
		} catch (IOException e) {
			LOGGER.error("获取微信AccessToken失败", e);
		} catch (JSONException e) {
			LOGGER.error("解析微信Ticket返回数据异常", e);
		}
		return ticket;
	}

}
