package com.weixin.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.weixin.domain.WeChatAccessToken;

public interface WeChatAccessTokenDao extends JpaRepository<WeChatAccessToken, Long>, JpaSpecificationExecutor<WeChatAccessToken> {

}
