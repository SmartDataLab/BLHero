/**
 * 
 */
package com.xiugou.x1.backstage.module.clientlog.service;

import java.util.List;

import javax.annotation.PostConstruct;

import org.gaming.backstage.PageData;
import org.gaming.backstage.service.AbstractService;
import org.gaming.db.util.QueryUtil.QuerySet;
import org.gaming.tool.ConsoleUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xiugou.x1.backstage.module.clientlog.model.ClientLog;
import com.xiugou.x1.backstage.module.mailsetting.service.MailSettingService;

/**
 * @author YY
 *
 */
@Service
public class ClientLogService extends AbstractService<ClientLog> {

	@Autowired
	private MailSettingService mailSettingService;
	
	public PageData<ClientLog> query(QuerySet querySet) {
		List<ClientLog> list = this.repository().getBaseDao().queryListWhere(querySet.getWhere(), querySet.getParams());
		
		PageData<ClientLog> pageData = new PageData<>();
		pageData.setCount(this.repository().getBaseDao().count(querySet.getCountWhere(), querySet.getCountParams()));
		pageData.setData(list);
		return pageData;
	}
	
	public void runInSchedule() {
		QuerySet querySet = new QuerySet();
		querySet.addCondition("level = ?", "fatal");
		querySet.addCondition("send = ?", "false");
		querySet.formWhere();
		List<ClientLog> list = this.repository().getBaseDao().queryListWhere(querySet.getWhere(), querySet.getParams());
		if(list.isEmpty()) {
			return;
		}
		for(ClientLog log : list) {
			try {
				mailSettingService.sendMail(log);
				
				log.setSend(true);
			} catch (Exception e) {
				logger.error("发送告警邮件" + log.getId() + "时发生异常", e);
			}
		}
		this.updateAll(list);
	}
	
	@PostConstruct
	public void test() {
		ConsoleUtil.addFunction("test_mail", () -> {
			sendMail();
		});
	}
	
	public void sendMail() {
		logger.info("发送");
		ClientLog log = new ClientLog();
		log.setTitle("ABC");
		log.setContent("大条了大条了");
		try {
			mailSettingService.sendMail(log);
			logger.info("发送成功");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
