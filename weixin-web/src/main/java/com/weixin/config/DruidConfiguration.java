package com.weixin.config;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.boot.context.embedded.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
/**
 * 使用druid配置监控统计功能
 * @author Alan Fu
 * @date 2016年3月11日
 * @version 0.0.1
 */
@Configuration
public class DruidConfiguration {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(DruidConfiguration.class);
	
	/**
	 * 初始化druid连接池
	 * Druid是一个用于大数据实时查询和分析的高容错、高性能开源分布式系统,旨在快速处理大规模的数据,并能够实现快速查询和分析
	 * @param driver
	 * @param url
	 * @param username
	 * @param password
	 * @return
	 */
    @Bean
    public DataSource druidDataSource(@Value("${spring.datasource.driverClassName}") String driver,@Value("${spring.datasource.url}") String url,
                                      @Value("${spring.datasource.username}") String username, @Value("${spring.datasource.password}") String password) {
    	
        DruidDataSource druidDataSource = new DruidDataSource();
        LOGGER.info("=====初始化Druid数据库连接池=====");
        try {
        	druidDataSource.setDriverClassName(driver);
	        druidDataSource.setUrl(url);
	        druidDataSource.setUsername(username);
	        druidDataSource.setPassword(password);
            druidDataSource.setFilters("stat, wall");
        } catch (SQLException e) {
        	LOGGER.error("初始化Druid异常.", e );
        }
        return druidDataSource;
    }
    
//  @Bean
//  public ServletRegistrationBean druidServlet() {
//      return new ServletRegistrationBean(new StatViewServlet(), "/druid/*");
//  }
    
    
    /**
     * 注册一个servlet
     * @return
     */
    @Bean
    public ServletRegistrationBean druidStatViewServle(){
    	LOGGER.info("注册Druid servlet设置黑白名单和账户信息");
       //org.springframework.boot.context.embedded.ServletRegistrationBean提供类的进行注册.
       ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean(new StatViewServlet(),"/druid/*");
      
       //添加初始化参数：initParams
      
       //白名单：
       servletRegistrationBean.addInitParameter("allow","127.0.0.1");
       //IP黑名单 (存在共同时，deny优先于allow) : 如果满足deny的话提示:Sorry, you are not permitted to view this page.
       servletRegistrationBean.addInitParameter("deny","192.168.85.128");
       //登录查看信息的账号密码.
       servletRegistrationBean.addInitParameter("loginUsername","admin");
       servletRegistrationBean.addInitParameter("loginPassword","admin123");
       //是否能够重置数据.
       servletRegistrationBean.addInitParameter("resetEnable","false");
       return servletRegistrationBean;
    }
    
    
    /**
     * 注册一个过滤器
     * @return
     */
    @Bean
    public FilterRegistrationBean filterRegistrationBean() {
    	LOGGER.info("注册Druid Filter添加过滤规则");
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(new WebStatFilter());
        //添加过滤规则
        filterRegistrationBean.addUrlPatterns("/*");
        //添加不需要忽略的格式信息
        filterRegistrationBean.addInitParameter("exclusions", "*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*");
        return filterRegistrationBean;
    }
}