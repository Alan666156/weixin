package com.weixin.service.userdetails;


import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.weixin.dao.UsersDao;
import com.weixin.domain.Users;



@Transactional
@Service
public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
	private UsersDao usersDao;
	
	/**
	 * security用户登录
	 */
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		final Users user = usersDao.findByUserName(username);
		if(null == user){
			throw new UsernameNotFoundException("用户不存在");
		}
		UserDetailsImpl userDetail = new UserDetailsImpl(user.getId());
		BeanUtils.copyProperties(user, userDetail);
		return userDetail;
	}
	
}
