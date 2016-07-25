package com.weixin.service;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.weixin.dao.UsersDao;
import com.weixin.domain.Users;
import com.weixin.service.userdetails.UserDetailsImpl;
@Transactional
@Service
public class UserService {
	
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
			return findById(((Users) authen.getPrincipal()).getId());
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
	public Users findById(Long id) {
		return usersDao.findOne(id);
	}
	
	public List<Users> findAll() {
		return usersDao.findAll(new Specification<Users>() {
			
			@Override
			public Predicate toPredicate(Root<Users> root, CriteriaQuery<?> query,
					CriteriaBuilder cb) {
				
				return query.getRestriction();
			}
		});
	}
	/**
	 * 保存用户登录会话
	 * @param openId	微信openId
	 * @param mobile	手机号码
	 * @param platform 登陆渠道
	 */
	public synchronized void pushUserToSecurity(String openId, String mobile, String platform) {
		
	}

	public Users findUserByOpenId(String openId) {
		return null;
	}

	public void save(Users user) {
		usersDao.save(user);
	}

	public Users findByUserName(String username) {
		// TODO Auto-generated method stub
		return null;
	}
}
