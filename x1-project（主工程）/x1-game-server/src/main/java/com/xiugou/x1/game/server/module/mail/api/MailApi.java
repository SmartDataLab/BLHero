/**
 *
 */
package com.xiugou.x1.game.server.module.mail.api;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.gaming.prefab.mail.MailArgs;
import org.gaming.prefab.mail.MailBelong;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xiugou.x1.design.constant.GameCause;
import com.xiugou.x1.design.struct.RewardThing;
import com.xiugou.x1.game.server.foundation.starting.ApplicationSettings;
import com.xiugou.x1.game.server.module.mail.constant.MailTemplate;
import com.xiugou.x1.game.server.module.mail.module.Mail;
import com.xiugou.x1.game.server.module.mail.module.MailSystem;
import com.xiugou.x1.game.server.module.mail.module.MailSystemResult;
import com.xiugou.x1.game.server.module.mail.service.MailService;
import com.xiugou.x1.game.server.module.mail.service.MailSystemResultService;
import com.xiugou.x1.game.server.module.mail.service.MailSystemService;
import com.xiugou.x1.game.server.module.player.model.Player;
import com.xiugou.x1.game.server.module.player.service.PlayerService;

import pojo.xiugou.x1.pojo.GameApi;
import pojo.xiugou.x1.pojo.module.mail.MailAttachment;
import pojo.xiugou.x1.pojo.module.mail.MailTable;
import pojo.xiugou.x1.pojo.module.mail.MailToGameServer;
import pojo.xiugou.x1.pojo.module.server.ServerResponse;
import pojo.xiugou.x1.pojo.module.server.ServerResponseCode;

/**
 * @author YY
 *
 */
@Controller
public class MailApi {

	private static Logger logger = LoggerFactory.getLogger(MailApi.class);
	
	@Autowired
	private MailSystemService mailSystemService;
	@Autowired
	private MailSystemResultService mailSystemResultService;
	@Autowired
	private MailService mailService;
	@Autowired
	private ApplicationSettings applicationSettings;
	@Autowired
	private PlayerService playerService;

	/**
	 * 发送系统邮件
	 * @param mailToGameServer
	 * @return
	 */
	@RequestMapping(value = GameApi.sendMailSystem)
	@ResponseBody
	public String sendMailSystem(@RequestBody MailToGameServer mailToGameServer) {
		logger.info("收到系统邮件添加请求，后台邮件ID{}", mailToGameServer.getId());
		
		MailSystemResult mailSystemResult = mailSystemResultService.getEntity(mailToGameServer.getId());
		if (mailSystemResult != null) {
			logger.info("系统邮件{}重复请求", mailToGameServer.getId());
			return new ServerResponse(ServerResponseCode.MAIL_REPEAT).result();
		}
		

		MailBelong mailBelong = null;
		if (mailToGameServer.getType() == 1) {
			mailBelong = MailBelong.EVERYONE;
		} else {
			mailBelong = MailBelong.SOMEONE;
		}
		List<RewardThing> rewards = new ArrayList<>();
		if (mailToGameServer.getRewards() != null) {
			for (MailAttachment attachment : mailToGameServer.getRewards()) {
				rewards.add(RewardThing.of(attachment.getItem(), attachment.getNum()));
			}
		}
		try {
			Set<Long> pids = new HashSet<>();
			if(mailToGameServer.getPids() != null && !"".equals(mailToGameServer.getPids())) {
				String[] parts = mailToGameServer.getPids().split(";");
				for(String part : parts) {
					pids.add(Long.parseLong(part));
				}
			}
			
			mailSystemResult = new MailSystemResult();
			mailSystemResult.setId(mailToGameServer.getId());
			mailSystemResultService.insert(mailSystemResult);

			MailSystem mailSystem = mailSystemService.createMail(MailTemplate.SYS, MailArgs.build(mailToGameServer.getTitle()),
					MailArgs.build(mailToGameServer.getContent()), mailBelong, pids, rewards, GameCause.MAIL_SYS,
					applicationSettings.getGameServerIds(), mailToGameServer.getId());
			mailSystem.setLevel(mailToGameServer.getPlayerLevel());
			mailSystemService.saveMail(mailSystem);
			
			logger.info("系统邮件{}添加成功", mailToGameServer.getId());
			return ServerResponse.SUCCESES.result();
		} catch (Exception e) {
			return new ServerResponse(ServerResponseCode.FAIL.getValue(), e.getMessage()).result();
		}
	}

	/**
	 * 删除系统邮件
	 * @param mailSystemId
	 * @return
	 */
	@RequestMapping(value = GameApi.deleteMailSystem)
	@ResponseBody
	public String deleteMailSystem(long mailSystemId) {
		MailSystem mailSystem = mailSystemService.getMailSystem(mailSystemId);
		if (mailSystem != null) {
			mailSystemService.delete(mailSystem);
		}
		return ServerResponse.SUCCESES.result();
	}


	/**
	 * 查询玩家邮件
	 * @param playerId
	 * @return
	 */
	@RequestMapping(value = GameApi.queryPlayerMails)
	@ResponseBody
	public String queryPlayerMails(long playerId) {
		Player player = playerService.getEntity(playerId);
		List<Mail> mails = mailService.mails(playerId);
		List<MailTable.MailData> datas = new ArrayList<>();
		MailTable mailTable = new MailTable();
		for (Mail mail : mails) {
			MailTable.MailData mailData = new MailTable.MailData();
			mailData.setId(mail.getId());
			mailData.setReceiver(mail.getReceiver());
			mailData.setTemplate(mail.getTemplate());
			mailData.setTitleArgs(mail.getTitleArgs());
			mailData.setContentArgs(mail.getContentArgs());
			mailData.setAttachment(mail.getAttachment());
			mailData.setRead(mail.isRead());
			mailData.setReceive(mail.isReceive());
			mailData.setExpireTime(mail.getExpireTime());
			mailData.setGameCause(mail.getGameCause());
			mailData.setGameCauseText(mail.getGameCauseText());
			mailData.setFromPoolId(mail.getFromPoolId());
			mailData.setChannelId(player.getCreateChannel());
			datas.add(mailData);
		}
		mailTable.setDatas(datas);
		ServerResponse result = new ServerResponse(ServerResponseCode.SUCCESS);
		result.setData(mailTable);
		return result.result();
	}

	/**
	 * 删除玩家个人邮件
	 * @param playerId
	 * @param mailId
	 * @return
	 */
	@RequestMapping(value = GameApi.deletePlayerMail)
	@ResponseBody
	public String deletePlayerMail(long playerId, long mailId) {
		Mail mail = mailService.getMail(playerId, mailId);
		if (mail != null) {
			mailService.delete(Collections.singletonList(mail));
		}
		return ServerResponse.SUCCESES.result();
	}
}
