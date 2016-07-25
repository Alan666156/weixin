package com.weixin.vo;

public class WxMessageRes {
	/** 消息返回码 */
	private String errcode;
	/** 消息描述信息 */
	private String errmsg;

	public WxMessageRes() {
	}

	public WxMessageRes(String errcode, String errmsg) {
		super();
		this.errcode = errcode;
		this.errmsg = errmsg;
	}

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

}
