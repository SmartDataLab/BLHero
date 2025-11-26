/**
 *
 */
package com.xiugou.x1.game.server.module.mail.log;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.gaming.db.repository.BaseRepository;
import org.gaming.db.usecase.SlimDao;
import org.gaming.tool.GsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.xiugou.x1.game.server.module.mail.module.Mail;
import com.xiugou.x1.game.server.module.player.service.PlayerService;

import pojo.xiugou.x1.pojo.log.mail.MailLog;

/**
 * @author YY
 *
 */
@Component
public class MailLogger {

	@Autowired
	private PlayerService playerService;
	
    private BaseRepository<MailLog> repository;
    protected BaseRepository<MailLog> repository() {
        if (repository == null) {
            repository = SlimDao.getRepository(MailLog.class);
        }
        return repository;
    }

    public void logOnDelete(List<Mail> mails) {
    	List<MailLog> logs = new ArrayList<>();
        for (Mail mail : mails) {
        	logs.add(buildLog(mail));
        }
        repository().insertAll(logs);
    }

    private MailLog buildLog(Mail mail) {
        MailLog log = new MailLog();
        log.setOriMailId(mail.getId());
        log.setOwnerId(mail.getReceiver());
        log.setOwnerName(playerService.getEntity(mail.getReceiver()).getNick());
        log.setTemplate(mail.getTemplate());
        log.setTitleArgs(GsonUtil.toJson(mail.getTitleArgs()));
        log.setContentArgs(GsonUtil.toJson(mail.getContentArgs()));
        log.setAttachment(mail.getAttachment());
        log.setRead(mail.isRead());
        log.setReceive(mail.isReceive());
        log.setExpireTime(mail.getExpireTime());
        log.setGameCause(mail.getGameCause());
        log.setGameCauseText(mail.getGameCauseText());
        log.setFromPoolId(mail.getFromPoolId());
        log.setTime(LocalDateTime.now());
        return log;
    }
}
