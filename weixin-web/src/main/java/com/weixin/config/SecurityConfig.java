package com.weixin.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
/**
 * security 配置
 * @author Alan Fu
 * @date 2016年6月20日
 * @version 0.0.1
 */
//@Configuration
//@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	// @Autowired
	// private UserDetailsService localUserDetailServiceImpl;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable();
		http.authorizeRequests()
				.antMatchers("/css/**", "/js/**", "/images/**", "/html/**", "/index")
				.permitAll().anyRequest().fullyAuthenticated().and()
				.formLogin().loginPage("/").permitAll();
		http.logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout"));
	}

	/*
	 * @Override public void configure(AuthenticationManagerBuilder auth) throws
	 * Exception {
	 * auth.userDetailsService(localUserDetailServiceImpl).passwordEncoder(
	 * passwordEncoder); }
	 */
}
