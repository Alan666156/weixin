package com.weixin.config;



import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.ErrorPage;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import com.weixin.directive.ParamEncryptDirective;
import com.weixin.filter.HTMLCharacterFilter;

import freemarker.template.TemplateModelException;


@Configuration
//@EnableRedisHttpSession
public class WebConfig {
	
	@Autowired
	private FreeMarkerConfigurer freeMarkerConfigurer;
	
	/**
	 * 加载自定义freemarker指令
	 */
	@Bean
	public ParamEncryptDirective paramEncryptDirective() {
		ParamEncryptDirective paramEncryptDirective = null;
		try {
			paramEncryptDirective = new ParamEncryptDirective();
			Map<String, Object> variables = new HashMap<String, Object>();
			variables.put("encrypt", paramEncryptDirective);
			freeMarkerConfigurer.setFreemarkerVariables(variables);
			freeMarkerConfigurer.getConfiguration().setSharedVariable("encrypt", paramEncryptDirective);
			freeMarkerConfigurer.getConfiguration().setSharedVaribles(variables);
		} catch (TemplateModelException e) {
			e.printStackTrace();
		}
		return paramEncryptDirective;
	}
	
	@Bean
	public FilterRegistrationBean registration(HTMLCharacterFilter filter) {
	    FilterRegistrationBean registration = new FilterRegistrationBean(filter);
	    registration.setEnabled(true);
	    return registration;
	}
	
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
