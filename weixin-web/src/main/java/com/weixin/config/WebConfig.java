package com.weixin.config;



import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.ErrorPage;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;

import com.weixin.filter.HTMLCharacterFilter;


@Configuration
//@EnableRedisHttpSession
public class WebConfig {
	
	/**
	 * 自定义过滤器
	 * @param filter
	 * @return
	 */
	@Bean
	public FilterRegistrationBean registration(HTMLCharacterFilter filter) {
	    FilterRegistrationBean registration = new FilterRegistrationBean(filter);
	    registration.setEnabled(true);
	    return registration;
	}
	
	/**
	 * 错误异常页面
	 * @return
	 */
	@Bean
	public EmbeddedServletContainerCustomizer containerCustomizer() {

		return new EmbeddedServletContainerCustomizer() {
			@Override
			public void customize(ConfigurableEmbeddedServletContainer container) {
				ErrorPage error400Page = new ErrorPage(HttpStatus.BAD_REQUEST, "/error_page/400.html");
				ErrorPage error401Page = new ErrorPage(HttpStatus.UNAUTHORIZED, "/error_page/401.html");
				ErrorPage error404Page = new ErrorPage(HttpStatus.NOT_FOUND, "/html/404.html");
				ErrorPage error500Page = new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/html/500.html");
				container.addErrorPages(error400Page, error401Page, error404Page, error500Page); 
			}
		};
	}
	
}
