package com.weixin.filter;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.weixin.utils.AesUtils;
/**
 * 自定义过滤器解密get请求参数
 * 一次请求只通过一次filter，而不需要重复执行。
 * @author: fuhongxing
 * @date:   2015年4月1日
 * @version 1.0.0
 */
@Component
public class ParameterDecodeFilter extends OncePerRequestFilter {
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		if ("GET".equals(request.getMethod()) || StringUtils.isNotEmpty(request.getQueryString())) {
			super.doFilter(new RequestDecodeWrapper(request), response, filterChain);
		} else {
			super.doFilter(request, response, filterChain);
		}
	}
}

/**
 * 拦截设置包装请求参数(装饰模式)
 * @author: fuhongxing
 * @date:   2016年4月1日
 * @version 1.0.0
 */
class RequestDecodeWrapper extends HttpServletRequestWrapper {
	
	public RequestDecodeWrapper(HttpServletRequest request) {
		super(request);
	}

	@Override
	public Map<String, String[]> getParameterMap() {
		return super.getParameterMap();
	}

	@Override
	public Enumeration<String> getParameterNames() {
		return super.getParameterNames();
	}
	
	@Override
	public String[] getParameterValues(String name) {
		String[] values = super.getParameterValues(name);
		if (values != null) {
			for (int i = 0; i < values.length; i++) {
				values[i] = AesUtils.valueDecrypt(values[i]);
			}
		}
		
		return values;
	}
	@Override
	public String getParameter(String name) {
		if(super.getParameter(name)==null){
			return super.getParameter(name);
		}
		String value = AesUtils.valueDecrypt(super.getParameter(name));
		return value;
	}

	@Override
	public ServletInputStream getInputStream() throws IOException {
		return super.getInputStream();
	}
}
