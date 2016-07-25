package com.weixin.exception;
/**
 * 异常定义
 * @author Alan Fu
 * @date 2016年6月25日
 * @version 0.0.1
 */
public class WeiXinConnectionException extends RuntimeException {

	private static final long serialVersionUID = 296512239737412203L;

	public WeiXinConnectionException(String message) {
		super(message);
	}

	public WeiXinConnectionException(String message, Throwable e) {
		super(message, e);
	}

	public WeiXinConnectionException(String message, String errcode, String errmsg) {
		super(message);
	}
}
