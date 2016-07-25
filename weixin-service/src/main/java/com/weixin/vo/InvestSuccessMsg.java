package com.weixin.vo;

public class InvestSuccessMsg {
	
	private String openId;
	private String productName;
	private String interest;
	private String period;
	private String amount;
	private String rate;
	public InvestSuccessMsg(){
		
	}
	
public InvestSuccessMsg(String openId,String productName,String interest,String period, String amount,String rate){
		this.openId = openId;
	    this.productName = productName;
		this.period = period;
		this.amount = amount;
		this.rate = rate;
		this.interest = interest;
	}
	
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getInterest() {
		return interest;
	}
	public void setInterest(String interest) {
		this.interest = interest;
	}
	public String getPeriod() {
		return period;
	}
	public void setPeriod(String period) {
		this.period = period;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getRate() {
		return rate;
	}
	public void setRate(String rate) {
		this.rate = rate;
	}

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}
}
