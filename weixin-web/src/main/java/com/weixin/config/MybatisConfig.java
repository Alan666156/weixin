package com.weixin.config;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import com.alibaba.druid.pool.DruidDataSource;
/**
 * mybatis相关配置
 * @author Alan
 * @date 2016年5月10日
 * @version V1.0.0
 */
@Configuration
@MapperScan("com.weixin.dao")
public class MybatisConfig {
	
	private final static Logger LOGGER = LoggerFactory.getLogger(MybatisConfig.class);
    @Value("${mybatis.datasource.driverClassName}")
    private String driverClassName;

    @Value("${mybatis.datasource.url}")
    private String url;

    @Value("${mybatis.datasource.username}")
    private String userName;

    @Value("${mybatis.datasource.password}")
    private String password;

//    @Value("${pool.minPoolSize}")
//    private int minPoolSize;

//    @Value("${pool.maxPoolSize}")
//    private int maxPoolSize;
	
	/**
	 * 数据库连接池
	 * Druid是一个用于大数据实时查询和分析的高容错、高性能开源分布式系统,旨在快速处理大规模的数据,并能够实现快速查询和分析
	 * @return
	 */
    @Bean(destroyMethod = "close")
    public DataSource dataSource(){
    	LOGGER.info("初始化Druid数据库连接池...");
    	DruidDataSource dataSource = new DruidDataSource();  
    	dataSource.setUrl(url);  
    	dataSource.setDriverClassName(driverClassName);  
    	dataSource.setUsername(userName);  
    	dataSource.setPassword(password);
        return dataSource;
    }
    
    /**
     * 事务处理
     * @return
     */
    @Bean
    public PlatformTransactionManager transactionManager() {
        return new DataSourceTransactionManager(dataSource());
    }

    @Bean
    @ConditionalOnMissingBean
    public SqlSessionFactory sqlSessionFactory() throws Exception {
        SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(dataSource());
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        //加载xml映射文件
        sessionFactory.setMapperLocations(resolver.getResources("classpath:/mybatis/*.xml"));
        return sessionFactory.getObject();
    }
    
    /**
     * mybatis分页插件
     * @return
     */
   /* @Bean
    public PageHelper pageHelper() {
        LOGGER.info("注册MyBatis分页插件PageHelper");
        PageHelper pageHelper = new PageHelper();
        Properties p = new Properties();
        //设置为true时，会将RowBounds第一个参数offset当成pageNum页码使用 
        p.setProperty("offsetAsPageNum", "true");
        //设置为true时，使用RowBounds分页会进行count查询
        p.setProperty("rowBoundsWithCount", "true");
        p.setProperty("reasonable", "true");
        pageHelper.setProperties(p);
        return pageHelper;
    }*/
    
    /*@Bean
    @ConditionalOnMissingBean
	public SqlSessionTemplate sqlSessionTemplate(){
		try {
			return new SqlSessionTemplate(sqlSessionFactory());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}*/
}