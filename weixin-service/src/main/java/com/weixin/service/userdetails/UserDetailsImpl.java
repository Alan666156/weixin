package com.weixin.service.userdetails;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.weixin.domain.Users;

public class UserDetailsImpl extends Users implements UserDetails{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7442692309456432678L;
	
	private Map<String ,Object> transitData = new HashMap<String, Object>();
	private Long unionUserId;
	
	public UserDetailsImpl() {
		
	}
	
	public UserDetailsImpl(Long id) {
		super.setId(id);
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return null;
	}

	@Override
	public String getUsername() {
		return null;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	public Map<String, Object> getTransitData() {
		return transitData;
	}

	public void setTransitData(Map<String, Object> transitData) {
		this.transitData = transitData;
	}

	public Long getUnionUserId() {
		return unionUserId;
	}

	public void setUnionUserId(Long unionUserId) {
		this.unionUserId = unionUserId;
	}

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return null;
	}
	
 }