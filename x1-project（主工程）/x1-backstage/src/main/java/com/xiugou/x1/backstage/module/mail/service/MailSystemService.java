/**
 * 
 */
package com.xiugou.x1.backstage.module.mail.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.gaming.backstage.PageData;
import org.gaming.backstage.service.AbstractService;
import org.gaming.db.util.QueryUtil.QuerySet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xiugou.x1.backstage.module.gameserver.model.GameServer;
import com.xiugou.x1.backstage.module.gameserver.service.GameServerService;
import com.xiugou.x1.backstage.module.mail.model.MailSystem;
import com.xiugou.x1.backstage.module.mail.model.MailSystemResult;
import com.xiugou.x1.backstage.util.X1HttpUtil;

import pojo.xiugou.x1.pojo.GameApi;
import pojo.xiugou.x1.pojo.module.mail.MailToGameServer;
import pojo.xiugou.x1.pojo.module.server.ServerResponse;

/**
 * @author YY
 *
 */
@Service
public class MailSystemService extends AbstractService<MailSystem> {

	@Autowired
	private GameServerService gameServerService;
	@Autowired
	private MailSystemResultService mailSystemResultService;
	
	private Queue<MailSystem> sendingQueue = new ConcurrentLinkedQueue<>();
	
	public PageData<MailSystem> query(QuerySet querySet) {
		List<MailSystem> list = this.repository().getBaseDao().queryListWhere(querySet.getWhere(), querySet.getParams());
		
		PageData<MailSystem> pageData = new PageData<>();
		pageData.setCount(this.repository().getBaseDao().count(querySet.getCountWhere(), querySet.getCountParams()));
		pageData.setData(list);
		return pageData;
	}
	
	public void updateAll(List<MailSystem> mails) {
		this.repository().updateAll(mails);
	}
	
	public void addToSending(MailSystem mail) {
		sendingQueue.add(mail);
	}
	
	public void runInSchedule() {
		MailSystem mail = sendingQueue.poll();
		if(mail == null) {
			return;
		}
		if(mail.getStatus() == 1) {
			tellSendMail(mail);
		} else if(mail.getStatus() == 3) {
			tellDeleteMail(mail);
		}
	}
	
	/**
	 * 通知游戏服发送邮件
	 * @param mail
	 */
	public void tellSendMail(MailSystem mail) {
		MailToGameServer mailToGameServer = new MailToGameServer();
		mailToGameServer.setId(mail.getId());
		mailToGameServer.setTitle(mail.getTitle());
		mailToGameServer.setContent(mail.getContent());
		mailToGameServer.setPlayerLevel(mail.getPlayerLevel());
		mailToGameServer.setType(mail.getType());
		mailToGameServer.setRewards(mail.getRewards());
		mailToGameServer.setPids(mail.getPids());
		
		try {
			logger.info("正在将邮件{}发向各服务器", mail.getId());
			for(int serverUid : mail.getServerUids()) {
				if(mail.getSendServerUids().contains(serverUid)) {
					continue;
				}
				
				MailSystemResult result = new MailSystemResult();
				result.setMailId(mail.getId());
				result.setServerUid(serverUid);
				result.setTypeText("发送邮件");
				
				GameServer gameServer = gameServerService.getEntity(serverUid);
				if(gameServer == null) {
					result.setMessage("通过唯一ID" + serverUid + "未找到服务器");
					result.setStatus(2);
				} else {
					result.setServerId(gameServer.getServerId());
					result.setServerName(gameServer.getName());
					
					ServerResponse serverResponse = X1HttpUtil.jsonPost(gameServer, GameApi.sendMailSystem, mailToGameServer);
					if(serverResponse == null) {
						result.setStatus(2);
						result.setMessage("未收到游戏服" + serverUid + "的响应");
					} else {
						result.setMessage(serverResponse.getMessage());
						if(serverResponse.getCode() == 0) {
							result.setStatus(1);
							mail.getSendServerUids().add(serverUid);
						} else {
							result.setStatus(2);
						}
					}
				}
				mailSystemResultService.insert(result);
			}
		} finally {
			this.update(mail);
		}
	}
	
	/**
	 * 通知游戏服删除邮件
	 * @param mail
	 */
	public void tellDeleteMail(MailSystem mail) {
		try {
			for(int serverUid : mail.getServerUids()) {
				if(mail.getDeleteServerUids().contains(serverUid)) {
					continue;
				}
				
				MailSystemResult result = new MailSystemResult();
				result.setMailId(mail.getId());
				result.setServerUid(serverUid);
				result.setTypeText("删除邮件");
				
				GameServer gameServer = gameServerService.getEntity(serverUid);
				if(gameServer == null) {
					result.setMessage("通过唯一ID" + serverUid + "未找到服务器");
				} else {
					result.setServerId(gameServer.getServerId());
					result.setServerName(gameServer.getName());
					
					Map<String, Object> parameter = new HashMap<>();
					parameter.put("mailSystemId", mail.getId());
					ServerResponse serverResponse = X1HttpUtil.formPost(gameServer, GameApi.deleteMailSystem, parameter);
					if(serverResponse == null) {
						result.setMessage("请求服务器返回null");
					} else {
						result.setMessage(serverResponse.getMessage());
						if(serverResponse.getCode() == 0) {
							//发送成功
							mail.getDeleteServerUids().add(serverUid);
						}
					}
					
				}
				mailSystemResultService.insert(result);
			}
		} finally {
			this.update(mail);
		}
	}
}
