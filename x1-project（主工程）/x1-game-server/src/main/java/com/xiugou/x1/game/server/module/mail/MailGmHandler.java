package com.xiugou.x1.game.server.module.mail;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.gaming.fakecmd.annotation.PlayerGmCmd;
import org.gaming.prefab.mail.MailArgs;
import org.gaming.prefab.mail.MailBelong;
import org.gaming.prefab.thing.NoticeType;
import org.gaming.tool.DateTimeUtil;
import org.gaming.tool.LocalDateTimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.xiugou.x1.design.constant.GameCause;
import com.xiugou.x1.design.struct.RewardThing;
import com.xiugou.x1.game.server.foundation.player.PlayerContext;
import com.xiugou.x1.game.server.foundation.starting.ApplicationSettings;
import com.xiugou.x1.game.server.module.mail.constant.MailTemplate;
import com.xiugou.x1.game.server.module.mail.module.Mail;
import com.xiugou.x1.game.server.module.mail.module.MailPlayer;
import com.xiugou.x1.game.server.module.mail.module.MailSystem;
import com.xiugou.x1.game.server.module.mail.service.MailPlayerService;
import com.xiugou.x1.game.server.module.mail.service.MailService;
import com.xiugou.x1.game.server.module.mail.service.MailSystemService;

/**
 * @author yh
 * @date 2023/7/11
 * @apiNote
 */
@Controller
public class MailGmHandler {

	@Autowired
	private MailService mailService;
	@Autowired
	private MailSystemService mailSystemService;
	@Autowired
	private ApplicationSettings applicationSettings;
	@Autowired
	private MailPlayerService mailPlayerService;

	@PlayerGmCmd(command = "SED_MAIL")
	public void sendMail(PlayerContext playerContext, String[] params) {
		List<RewardThing> rewards = new ArrayList<>();
		rewards.add(RewardThing.of(1, 100));
		rewards.add(RewardThing.of(2, 100));
		rewards.add(RewardThing.of(3, 100));
		rewards.add(RewardThing.of(4, 100));
		rewards.add(RewardThing.of(5, 100));
		rewards.add(RewardThing.of(6, 100));
		// rewards.add(RewardThing.of(1, 100));
		Mail mail = mailService.newMail(playerContext.getId(), MailTemplate.EQUIP_LIMIT, rewards, GameCause.GM);
		mailService.sendMail(mail, NoticeType.NORMAL);

		Mail mail1 = mailService.newMail(playerContext.getId(), MailTemplate.SYS, MailArgs.build("系统邮件标题任意填"),
				MailArgs.build("系统邮件内容任意填<h1>大号的字</h1>，\n\t还有换行"), rewards, GameCause.GM);
		mailService.sendMail(mail1, NoticeType.NORMAL);

		rewards.add(RewardThing.of(3001011, 10));
		Mail mail2 = mailService.newMail(playerContext.getId(), MailTemplate.TEST,
				MailArgs.build(playerContext.getId()), MailArgs.build(playerContext.getId(), 1), rewards, GameCause.GM);
		mailService.sendMail(mail2, NoticeType.NORMAL);
		Mail mail3 = mailService.newMail(playerContext.getId(), MailTemplate.TEST,
				MailArgs.build(playerContext.getId()), MailArgs.build(playerContext.getId(), 1), null, GameCause.GM);
		mailService.sendMail(mail3, NoticeType.NORMAL);

		Mail mail3200001 = mailService.newMail(playerContext.getId(), MailTemplate.SANQI_HUYU_GIFT,
				MailArgs.build(playerContext.getId()), MailArgs.build(playerContext.getId(), 1), rewards, GameCause.GM);
		mailService.sendMail(mail3200001, NoticeType.NORMAL);

		Mail mail3200002 = mailService.newMail(playerContext.getId(), MailTemplate.SANQI_HUYU_ASK,
				MailArgs.build(playerContext.getId()), MailArgs.build(playerContext.getId(), 1), rewards, GameCause.GM);
		mailService.sendMail(mail3200002, NoticeType.NORMAL);

	}

	@PlayerGmCmd(command = "TEST_MAIL")
	public void testMail(PlayerContext playerContext, String[] params) {
		List<RewardThing> rewards = new ArrayList<>();
		rewards.add(RewardThing.of(1, 100));
		rewards.add(RewardThing.of(2, 100));

		MailSystem mailSystem;
		try {
			mailSystem = mailSystemService.createMail(MailTemplate.SYS, MailArgs.build("AAA"), MailArgs.build("AAA"),
					MailBelong.EVERYONE, Collections.emptySet(), rewards, GameCause.MAIL_SYS,
					applicationSettings.getGameServerIds(), DateTimeUtil.currSecond());
			mailSystem.setLevel(0);
			mailSystemService.saveMail(mailSystem);
		} catch (Exception e) {
			e.printStackTrace();
		}

		MailPlayer mailPlayer = mailPlayerService.getEntity(playerContext.getId());
		System.out.println(mailPlayer.getReadTime());

		List<Mail> mail1s = mailSystemService.pullMailFromSystem(playerContext.getId());
		mailService.sendMail(mail1s, NoticeType.NORMAL);
		System.out.println("mail1s " + mail1s.size());
		System.out.println(mailPlayer.getReadTime() + " " + mailPlayer.getLastReadTime());
		List<Mail> mail2s = mailSystemService.pullMailFromSystem(playerContext.getId());
		System.out.println("mail2s " + mail2s.size());
		System.out.println(mailPlayer.getReadTime() + " " + mailPlayer.getLastReadTime());
		mailService.sendMail(mail2s, NoticeType.NORMAL);
	}

	public static void main(String[] args) {
		long now = 1713964525999L;
		System.out.println(now);
		System.out.println(LocalDateTimeUtil.ofEpochMilli(now));

		System.out.println(LocalDateTimeUtil.toEpochSecond(LocalDateTimeUtil.ofEpochMilli(now)));
	}
}
