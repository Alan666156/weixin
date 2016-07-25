package com.weixin.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.MessageFormat;

import me.chanjar.weixin.common.bean.WxMenu;
import me.chanjar.weixin.mp.bean.WxMpCustomMessage;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.weixin.common.WeiXinConstants;
import com.weixin.vo.AuthorizeAccessToken;
import com.weixin.vo.WxJsApiTicket;
import com.weixin.vo.WxMessageRes;

public class WeiXinConnectionUtil {
	
	private WeiXinConnectionUtil(){
		
	}
	
	private static final Logger LOGGER = LoggerFactory.getLogger(WeiXinConnectionUtil.class);

	/**
	 * 微信获取ACCESS_TOKEN MessageFormat.format {0}:appid {1}：secret
	 */
	public static final String GET_ACCESS_TOKEN = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid={0}&secret={1}";

	/**
	 * 微信发送消息(发向指定的用户) MessageFormat.format {0}:access_token
	 */
	public static final String SEND_CUSTOMER_MESSAGE = "https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token={0}";
	/**
	 * 微信发送模板消息(发向指定的用户) MessageFormat.format {0}:access_token
	 */
	public static final String SEND_TEMPLATE_MESSAGE = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token={0}";

	/**
	 * 采用http GET方式请求获得jsapi_ticket
	 */
	public static final String GET_JSAPI = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token={0}&type=jsapi";

	/**
	 * 微信创建菜单 url
	 */
	public static final String CREATE_MENU = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token={0}";

	/**
	 * 微信认证连接 url MessageFormat.format {0}:appid {1}：redirect_uri {2}：state
	 */
	public static final String CONNECT_AUTHORIZE = "https://open.weixin.qq.com/connect/oauth2/authorize?appid={0}&redirect_uri={1}/authorize-callback&response_type=code&scope=snsapi_base&state={2}#wechat_redirect";

	/**
	 * 通过code换取网页授权access_token URL <br>
	 * MessageFormat.format {0}:appid {1}：secret {2}：code
	 */
	public static final String OAUTH2_ACCESS_TOKEN = "https://api.weixin.qq.com/sns/oauth2/access_token?appid={0}&secret={1}&code={2}&grant_type=authorization_code";

	/** 创建二维码ticket URL */
	public static final String TICKET_CODE = "https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token={0}";

	/** 通过ticket生成带参数二维码URL */
	public static final String TWO_CODE = "https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket={0}";

	/** 获取用户信息 URL */
	public static final String USER_INFO = "https://api.weixin.qq.com/sns/userinfo?access_token={0}&openid={1}&lang=zh_CN";

	/** 获取微信临时素材 **/
	public static final String GET_MEDIA_FILE = "https://api.weixin.qq.com/cgi-bin/media/get?access_token={0}&media_id={1}";

	public static final String[] MESSAGE_TYPE = new String[] { "text", "image", "voice", "video", "shortvideo", "location" };

	/**
	 * 创建二维码ticket 获取二维码ticket后，可用ticket换取二维码图片
	 * 获取带参数的二维码的过程包括两步，首先创建二维码ticket，然后凭借ticket到指定URL换取二维码
	 * 
	 * @return
	 * @throws IOException
	 * @throws MalformedURLException
	 */
	public static String getTicket(String accessToken, String body) throws MalformedURLException, IOException {
		return sendPostRequest(MessageFormat.format(TICKET_CODE, accessToken), body);
	}

	/**
	 * 获取用户信息
	 * 
	 * @throws IOException
	 * @throws MalformedURLException
	 */
	public static String getUserInfo(String accessToken, String openid) throws MalformedURLException, IOException {
		return sendRequest(MessageFormat.format(USER_INFO, accessToken, openid));
	}

	/**
	 * 获取AccessToken
	 */
	public static AuthorizeAccessToken getAccessToken(String appid, String secret) throws IOException {
		return JSON.parseObject(sendRequest(MessageFormat.format(GET_ACCESS_TOKEN, appid, secret)), AuthorizeAccessToken.class);
	}

	/**
	 * 发送消息（文本、图文...）
	 */
	public static WxMessageRes sendCustomerMessage(String accessToken, WxMpCustomMessage message) throws IOException {
		return JSON.parseObject(sendPostRequest(MessageFormat.format(SEND_CUSTOMER_MESSAGE, accessToken), JSON.toJSONString(message)), WxMessageRes.class);
	}

	/**
	 * 发送模板消息
	 * 
	 * @param accessToken
	 * @param message
	 * @return
	 * @throws IOException
	 */
	public static String sendTemplateMessage(String accessToken, String message) throws IOException {
		return sendPostRequest(MessageFormat.format(SEND_TEMPLATE_MESSAGE, accessToken), message);
	}

	/**
	 * 创建菜单
	 */
	public static WxMessageRes createMenu(String accessToken, WxMenu menus) throws IOException {
		return JSON.parseObject(sendPostRequest(MessageFormat.format(CREATE_MENU, accessToken), JSON.toJSONString(menus)), WxMessageRes.class);
	}

	/**
	 * 通过code换取网页授权access_token
	 */
	public static AuthorizeAccessToken getAuthorizeAccessToken(String appid, String secret, String code) throws IOException {
		return JSON.parseObject(sendPostRequest(MessageFormat.format(OAUTH2_ACCESS_TOKEN, appid, secret, code), null), AuthorizeAccessToken.class);
	}
	
	/**
	 * 获取媒体文件
	 * @param accessToken
	 * @param mediaId
	 * @return
	 * @throws Exception
	 */
	public static File getMediaFile(String accessToken, String mediaId) throws Exception {
		return null;
	}
	

	public static String sendRequest(String url) throws MalformedURLException, IOException {
		StringBuilder sb = new StringBuilder();
		HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
		conn.setRequestMethod(WeiXinConstants.HTTP_REQUEST_TYPE_GET);
		conn.setConnectTimeout(30000);
		conn.setUseCaches(false);
		BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		String str = null;
		while ((str = reader.readLine()) != null) {
			sb.append(str);
		}
		return sb.toString();
	}

	public static InputStream getInputStream(String url) throws MalformedURLException, IOException {
		HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
		conn.setRequestMethod(WeiXinConstants.HTTP_REQUEST_TYPE_GET);
		conn.setConnectTimeout(30000);
		conn.setUseCaches(false);
		return conn.getInputStream();

	}

	public static String sendPostRequest(String url, String body) throws IOException {
		StringBuilder sb = new StringBuilder();
		HttpURLConnection conn;
		BufferedReader reader = null;
		OutputStream os = null;
		try {
			conn = (HttpURLConnection) new URL(url).openConnection();
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setUseCaches(false);
			conn.setRequestMethod(WeiXinConstants.HTTP_REQUEST_TYPE_POST);
			conn.setConnectTimeout(30000);
			if (StringUtils.isNotEmpty(body)) {
				os = conn.getOutputStream();
				os.write(body.getBytes(WeiXinConstants.BYTE_TYPE_UTF_8));
			}
			reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String str = null;
			while ((str = reader.readLine()) != null) {
				sb.append(str);
			}
			return sb.toString();
		} catch (IOException e) {
			throw e;
		} finally {
			IOUtils.closeQuietly(reader);
			IOUtils.closeQuietly(os);
			IOUtils.closeQuietly(os);
		}
	}
	
	/**
	 * 获取JsapiTicket
	 * @param accessToken
	 * @return
	 */
	public static WxJsApiTicket getJsapiTicket(String accessToken) {
		try {
			String content = sendPostRequest(MessageFormat.format(GET_JSAPI, accessToken), " ");
			return JSON.parseObject(content, WxJsApiTicket.class);
		} catch (Exception e) {
			LOGGER.error("Json解析异常",e);
		}
		return null;
	}
}