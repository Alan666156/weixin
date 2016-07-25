package com.weixin.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSON;
import com.weixin.domain.Users;
import com.weixin.service.UserService;

@Controller
public class IndexController {
	private static final Logger LOGGER = LoggerFactory.getLogger(IndexController.class);
	@Autowired
	private UserService userService;
	
	@RequestMapping("/")
	public String index(Model model){
		Long id = 1l;
		Users user = userService.findById(id);
		List<Users> list  = userService.findAll();
		LOGGER.info("用户总人数:"+list.size());
		LOGGER.info("用户信息==" + JSON.toJSONString(user));
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
