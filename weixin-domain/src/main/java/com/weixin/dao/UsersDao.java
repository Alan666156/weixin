package com.weixin.dao;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.weixin.domain.Users;
@Repository
public interface UsersDao extends PagingAndSortingRepository<Users, Long>, JpaSpecificationExecutor<Users>{
	
	public Users findByUserName(String userName);
}
