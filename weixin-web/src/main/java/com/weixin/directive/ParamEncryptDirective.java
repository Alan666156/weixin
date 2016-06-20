package com.weixin.directive;

import java.io.IOException;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import com.weixin.utils.AesUtils;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;

/**
 * 自定义freemark标签指令(用于加密参数)
 * @author: fuhongxing
 * @date:   2016年1月30日
 * @version 1.0.0
 */
public class ParamEncryptDirective implements TemplateDirectiveModel {
	
	private static final String PARAM_NAME_KEY	= "val";
	private final static Logger LOGGER = LoggerFactory.getLogger(ParamEncryptDirective.class);

	/**
	 * @param env：系统环境变量，通常用它来输出相关内容，如Writer out = env.getOut();
	 * @param params：自定义标签传过来的对象，其key=自定义标签的参数名，value值是TemplateModel类型
	 * @param loopVars  循环替代变量
	 * @param body 用于处理自定义标签中的内容
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body) throws TemplateException, IOException {
		if (!params.containsKey(PARAM_NAME_KEY)) {
			throw new TemplateModelException("Encrypt Param is Fail!");
		}
		if(!StringUtils.isEmpty(params.get(PARAM_NAME_KEY))){
			String value = AesUtils.encrypt(params.get(PARAM_NAME_KEY).toString());
			LOGGER.info("encrypt param==" + params.get(PARAM_NAME_KEY).toString() +"----->"+value);
			//输出到标签
			env.getOut().write(value);
		}
	}
	
}