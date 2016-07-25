package com.weixin.service.userdetails;


import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.weixin.domain.Users;
import com.weixin.service.UserService;



@Transactional
@Service
public class CustomUserDetailsService implements UserDetailsService {

	
	@Autowired
	private UserService userService;
	
	/**
	 * security用户登录
	 */
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		final Users user = userService.findByUserName(username);
		if(null == user){
			throw new UsernameNotFoundException("用户不存在");
		}
		UserDetailsImpl userDetail = new UserDetailsImpl(user.getId());
		BeanUtils.copyProperties(user, userDetail);
		return userDetail;
	}
	
	/**
	 * 将用户信息写入Security
	 * @param openId
	 */
	public void pushUserToSecurity(final String openId) {
		Users user = userService.findUserByOpenId(openId);
		if(user == null){
			user = new Users();
			user.setOpenId(openId);
			userService.save(user);
		}
		final Long id = user.getId();
		UserDetailsImpl userDetails = new UserDetailsImpl(id);
		userDetails.setOpenId(user.getOpenId());
		userDetails.setUserName(user.getUserName());
		userDetails.setMobile(user.getMobile());
		UsernamePasswordAuthenticationToken newAuthentication = new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(newAuthentication);
	}
	
}
