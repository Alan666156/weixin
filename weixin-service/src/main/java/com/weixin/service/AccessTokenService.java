package com.weixin.service;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.weixin.dao.WeChatAccessTokenDao;
import com.weixin.domain.WeChatAccessToken;
import com.weixin.exception.WeiXinConnectionException;
import com.weixin.utils.WeiXinConnectionUtil;
import com.weixin.vo.AuthorizeAccessToken;
import com.weixin.vo.WxJsApiTicket;



@Service
@Transactional
public class AccessTokenService {
	/** log */
	private static final Logger LOGGER = LoggerFactory.getLogger(AccessTokenService.class);
	/** platform */
	private static String platform = "jupiter";

	@Value("${weixin.app.id}")
	private String appid;
	@Value("${weixin.app.secret}")
	private String secret;
	
	@Autowired
	private WeChatAccessTokenDao weChatAccessTokenDao;
	
//	@Autowired
//	private RedisService redisService;
	
	/**
	 * 获取AssessToken
	 */
	public WeChatAccessToken getAccessToken(Boolean create) {
		LOGGER.info("开始查询有效AcessToken........");
		// 查找未到期的 AccessToken
		List<WeChatAccessToken> webChatAccessTokenList = weChatAccessTokenDao.findAll(new Specification<WeChatAccessToken>() {
			@Override
			public Predicate toPredicate(Root<WeChatAccessToken> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				Predicate p1 = cb.greaterThan(root.<Date> get("invalidTime"), new Date());
				Predicate p2 = cb.equal(root.get("platform"), platform);
				query.orderBy(cb.desc(root.get("invalidTime")));
				query.where(p1,p2);
				return query.getRestriction();
			}
		});
		Boolean flag = webChatAccessTokenList == null||webChatAccessTokenList.isEmpty();
		if(!create&&flag){
			throw new RuntimeException("无法获取可用token!,需要手动更新accesstoken"); 
		}
		if (create||flag) {
			try {
				LOGGER.info("发起获取access_token请求(appid：{} | secret：{})。", appid, secret);
				Date date = new Date();
				AuthorizeAccessToken at = WeiXinConnectionUtil.getAccessToken(appid, secret);
				LOGGER.info("接收获取access_token响应：{}。", at);
				WxJsApiTicket jsApiTicket = WeiXinConnectionUtil.getJsapiTicket(at.getAccess_token());
				LOGGER.info("接收获取jsApiTicket响应：{}。", jsApiTicket);
				if (at.getErrcode() != null) {
					throw new WeiXinConnectionException("获取access_token失败", at.getErrcode(), at.getErrmsg());
				}
				WeChatAccessToken weChatAccessToken = new WeChatAccessToken();
				weChatAccessToken.setApplyTime(date);
				weChatAccessToken.setApplyCount(0);
				weChatAccessToken.setAccessToken(at.getAccess_token());
				weChatAccessToken.setInvalidTime(new Date(date.getTime() + (at.getExpires_in() * 1000)));
				weChatAccessToken.setPlatform(platform);
				weChatAccessToken.setJsApiTicket(jsApiTicket.getTicket());
				weChatAccessTokenDao.save(weChatAccessToken);
				
				// 写入redis
				LOGGER.info(JSON.toJSONString(weChatAccessToken));
				//使用redis保存accesstoken信息
//				redisService.set("wxAccessToken", JSON.toJSONString(weChatAccessToken));
				LOGGER.info("微信access_token write redis success!");
				
				return weChatAccessToken;
			} catch (IOException e) {
				LOGGER.error("从微信获取access_token失败。", e);
				throw new WeiXinConnectionException("获取access_token失败", e);
			} catch (ClassCastException e) {
				LOGGER.error("微信access_token写入redis异常!", e);
			}
		}else{
			LOGGER.info("获取"+webChatAccessTokenList.size()+"数据.....，返回第一条,数据ID："+webChatAccessTokenList.get(0).getId());
		}
		return webChatAccessTokenList.get(0);
	}
	
	
	
	
}