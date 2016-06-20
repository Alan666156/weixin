package com.weixin.service.userdetails;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.weixin.dao.UsersDao;
import com.weixin.domain.Users;

@Transactional
@Service
public class CustomerService {
	
	@Autowired
	private UsersDao usersDao;
	
	/**
	 * 获取当前用户
	 * 
	 * @return current user
	 */
	public Users getCurrentUser() {
		Authentication authen = SecurityContextHolder.getContext().getAuthentication();
		if (!"anonymousUser".equals(authen.getPrincipal())) {
			return loadById(((Users) authen.getPrincipal()).getId());
		} else {
			return null;
		}
	}

	public UserDetailsImpl getUserDetails() {
		Authentication authen = SecurityContextHolder.getContext().getAuthentication();
		if (!"anonymousUser".equals(authen.getPrincipal())) {
			return (UserDetailsImpl) authen.getPrincipal();
		} else {
			return null;
		}
	}

	/**
	 * 根据用户ID，查询用户
	 * 
	 * @param id 用户ID
	 * @return 用户
	 */
	public Users loadById(Long id) {
		return usersDao.findOne(id);
	}
	
	
	/**
	 * 保存用户登录会话
	 * @param openId	微信openId
	 * @param mobile	手机号码
	 * @param platform 登陆渠道
	 */
	public synchronized void pushUserToSecurity(String openId, String mobile, String platform) {
		
	}
}
