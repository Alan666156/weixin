package com.weixin.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {
	private static final Logger LOGGER = LoggerFactory.getLogger(IndexController.class);
	@RequestMapping("/")
	public String index(Model model){
		LOGGER.info("===index===");
		return "index";
	}
	
	/**
	 * 注册
	 * @param model
	 * @return
	 */
	@RequestMapping("/mobileRegister")
	public String mobileRegister(Model model){
		LOGGER.info("===index===");
		return "phone";
	}
}
