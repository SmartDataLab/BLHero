/**
 * 
 */
package com.xiugou.x1.backstage.module.mailsetting.service;

import java.util.Date;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.gaming.backstage.service.AbstractService;
import org.gaming.ruler.lifecycle.Lifecycle;
import org.springframework.stereotype.Service;

import com.xiugou.x1.backstage.module.clientlog.model.ClientLog;
import com.xiugou.x1.backstage.module.mailsetting.model.MailSetting;

/**
 * @author YY
 *
 */
@Service
public class MailSettingService extends AbstractService<MailSetting> implements Lifecycle {

	
	public void sendMail(ClientLog clientLog) throws Exception {
		MailSetting mailSetting = this.getById(1L);
		
		Properties props = new Properties();
		props.setProperty("mail.transport.protocol", mailSetting.getProtocol());
		props.setProperty("mail.smtp.host", mailSetting.getHost());
		props.setProperty("mail.smtp.port", mailSetting.getHost());
		
		props.setProperty("mail.smtp.auth", "true");
		props.setProperty("mail.smtp.ssl.enable", "true");
		props.setProperty("mail.smtp.ssl.protocols", "TLSv1.2");
		props.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		
		props.setProperty("mail.smtp.connectiontimeout", "10000");
		props.setProperty("mail.smtp.timeout", "10000");
		props.setProperty("mail.smtp.writetimeout", "10000");
		
		Session session = Session.getDefaultInstance(props);
		session.setDebug(false);
		
		MimeMessage message = new MimeMessage(session);
		message.addHeader("X-Mailer", "Microsoft Outlook Express 6.00.2900.2869");
		//发件人信息
		message.setFrom(new InternetAddress(mailSetting.getAccount(), "后台告警系统", "UTF-8"));
		//收件人信息
		message.setRecipient(Message.RecipientType.TO, new InternetAddress(mailSetting.getClientMasterMail(), mailSetting.getClientMasterMail(), "UTF-8"));
		message.setSubject(clientLog.getTitle(), "UTF-8");
		message.setContent(clientLog.getContent(), "text/html;charset=UTF-8");
		message.setSentDate(new Date());
		message.saveChanges();
		
		Transport transport = session.getTransport();
		transport.connect(mailSetting.getAccount(), mailSetting.getAuthCode());
		transport.sendMessage(message, message.getAllRecipients());
		transport.close();
	}

	@Override
	public void start() throws Exception {
		MailSetting mailSetting = this.getById(1L);
		if(mailSetting != null) {
			return;
		}
		mailSetting = new MailSetting();
		mailSetting.setId(1L);
		mailSetting.setProtocol("smtp");
		mailSetting.setHost("smtp.qq.com");
		mailSetting.setPort(465);
		mailSetting.setAccount("3462921572@qq.com");
		mailSetting.setAuthCode("kefizuhspramdbae");
		mailSetting.setClientMasterMail("89662961@qq.com");
		mailSetting.setServerMasterMail("89662961@qq.com");
		this.insert(mailSetting);
	}
}
