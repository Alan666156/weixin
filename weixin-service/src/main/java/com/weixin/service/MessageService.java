package com.weixin.service;

import java.io.File;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
/**
 * message email
 * @author Alan Fu
 * @date 2016年7月21日
 * @version 0.0.1
 */
@Service
public class MessageService {
	
	@Autowired
    private JavaMailSender mailSender;//邮件发送对象
	
	@Value("${spring.mail.username}")
	private String email;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(MessageService.class);
	
	/**
	 * 发送短信
	 * @return
	 */
	public String sendMessage(){
		LOGGER.info("======短信发送=======");
		return null;
	}
	
	/**
	 * 发送邮件(无附件)
	 * @return
	 */
	public String sendEmail(){
		LOGGER.info("======发送邮件=======");
		SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(email);
        message.setTo("469672872@qq.com");
        message.setSubject("主题：Spring boot Email Test");
        message.setText("邮件测试!!!");
        mailSender.send(message);
		return null;
	}
	/**
	 * 发送邮件(有附件)
	 * @return
	 */
	public String sendEmail(File file){
		LOGGER.info("======发送邮件(有附件)=======");
		MimeMessage mimeMessage = mailSender.createMimeMessage();

        try {
			MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
			helper.setFrom(email);
			helper.setTo("469672872@qq.com");
			helper.setSubject("主题：Spring boot Email 有附件");
			helper.setText("Spring boot Email有附件的邮件");
			//读取文件并添加到邮件附件中
			FileSystemResource files = new FileSystemResource(new File("weixin.jpg"));
			helper.addAttachment("附件-1.jpg", files);
			helper.addAttachment("附件-2.jpg", files);
		} catch (MessagingException e) {
			LOGGER.error("邮件发送异常!", e);
		}

        mailSender.send(mimeMessage);
		return null;
	}
	
	/**
	 * 模板邮件发送
	 * @throws Exception
	 */
	public void sendTemplateMail() throws Exception {
		LOGGER.info("======发送邮件(模板)=======");
       /* MimeMessage mimeMessage = mailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
        helper.setFrom("dyc87112@qq.com");
        helper.setTo("dyc87112@qq.com");
        helper.setSubject("主题：模板邮件");

        Map<String, Object> model = new HashedMap();
        model.put("username", "didi");
        String text = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, "template.vm", "UTF-8", model);
        helper.setText(text, true);

        mailSender.send(mimeMessage);*/
    }
}
