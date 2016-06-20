package com.weixin.utils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
  
  
/**
 * 直接通过Spring 上下文获取SpringBean,用于多线程环境
 * @author Alan
 * @date 2016年5月17日
 * @version V1.0.0
 */
@Component
public class SpringBeanUtil implements ApplicationContextAware{  
      
    private static ApplicationContext applicationContext = null;  
  
    @Override  
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {  
        SpringBeanUtil.applicationContext = applicationContext;  
    }  
  
    public static Object getBeanByName(String beanName) {  
        if (applicationContext == null){  
            return null;  
        }  
        return applicationContext.getBean(beanName);  
    }  
  
    public static <T> T getBean(Class<T> type) {  
        return applicationContext.getBean(type);  
    }  
  
}