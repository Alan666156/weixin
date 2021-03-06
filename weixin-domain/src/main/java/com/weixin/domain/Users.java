package com.weixin.domain;

import javax.persistence.Entity;
import javax.persistence.Table;
/**
 * 用户实体
 * @author Alan Fu
 * @date 2016年7月22日
 * @version 0.0.1
 */
@Entity
@Table(name="wx_users")
public class Users extends AbstractEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String name;//真实姓名
	private String userName;//昵称
	private String passward;//密码
	private String email;//邮箱
	private String mobile;//电话
	private String address;//地址
	private String openId;//openid(微信分配给每个用户的一个标识)
	
	public Users() {
		super();
	}
	public Users(String openId) {
		super();
		this.openId = openId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassward() {
		return passward;
	}
	public void setPassward(String passward) {
		this.passward = passward;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getOpenId() {
		return openId;
	}
	public void setOpenId(String openId) {
		this.openId = openId;
	}
	

}
