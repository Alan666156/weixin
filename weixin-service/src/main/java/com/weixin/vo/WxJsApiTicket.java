package com.weixin.vo;
/**
 * 微信JsApiTicket
 * @author Alan Fu
 * @date 2016年7月22日
 * @version 0.0.1
 */
public class WxJsApiTicket{
	private String errcode;
	private String errmsg;
	private String ticket;
	private String expires_in;
	public String getErrcode() {
		return errcode;
	}
	public void setErrcode(String errcode) {
		this.errcode = errcode;
	}
	public String getErrmsg() {
		return errmsg;
	}
	public void setErrmsg(String errmsg) {
		this.errmsg = errmsg;
	}
	public String getTicket() {
		return ticket;
	}
	public void setTicket(String ticket) {
		this.ticket = ticket;
	}
	public String getExpires_in() {
		return expires_in;
	}
	public void setExpires_in(String expires_in) {
		this.expires_in = expires_in;
	}

}
