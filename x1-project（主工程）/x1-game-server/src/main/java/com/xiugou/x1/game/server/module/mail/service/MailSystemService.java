package com.xiugou.x1.game.server.module.mail.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.gaming.db.repository.CacheRepository;
import org.gaming.db.usecase.SlimDao;
import org.gaming.prefab.mail.IMailTemplateDesign;
import org.gaming.prefab.mail.MailArgs;
import org.gaming.prefab.mail.MailBelong;
import org.gaming.prefab.mail.MailPoolAbstractService;
import org.gaming.ruler.lifecycle.Lifecycle;
import org.gaming.tool.LocalDateTimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xiugou.x1.design.module.MailCache;
import com.xiugou.x1.game.server.foundation.player.PlayerContext;
import com.xiugou.x1.game.server.foundation.player.PlayerContextManager;
import com.xiugou.x1.game.server.module.mail.message.SystemMailMessage;
import com.xiugou.x1.game.server.module.mail.module.Mail;
import com.xiugou.x1.game.server.module.mail.module.MailPlayer;
import com.xiugou.x1.game.server.module.mail.module.MailSystem;
import com.xiugou.x1.game.server.module.player.model.Player;
import com.xiugou.x1.game.server.module.player.service.PlayerService;

/**
 * @author yh
 * @date 2023/6/1
 * @apiNote
 */
@Service
public class MailSystemService extends MailPoolAbstractService<MailSystem, Mail> implements Lifecycle {

    @Autowired
    private MailCache mailCache;
    @Autowired
    private MailPlayerService mailPlayerService;
    @Autowired
    private MailService mailService;
    @Autowired
    private PlayerService playerService;
    @Autowired
    private PlayerContextManager playerContextManager;
    
    private CacheRepository<MailSystem> repository;
    protected CacheRepository<MailSystem> repository() {
        if (repository == null) {
            repository = SlimDao.getCacheRepository(MailSystem.class);
        }
        return repository;
    }

    @Override
    public void saveMail(MailSystem mail) {
        repository().insert(mail);
        
        if(mail.getBelong() == MailBelong.EVERYONE) {
        	for(PlayerContext playerContext : playerContextManager.onlines()) {
        		SystemMailMessage message = SystemMailMessage.of(playerContext.getId());
        		playerContext.tell(message);
        	}
        } else {
        	for(long playerId : mail.getReceivers()) {
        		PlayerContext playerContext = playerContextManager.getContext(playerId);
        		if(playerContext != null) {
        			SystemMailMessage message = SystemMailMessage.of(playerContext.getId());
            		playerContext.tell(message);
        		}
        	}
        }
    }

    @Override
    protected Mail createMail() {
        return new Mail();
    }

    @Override
    protected MailSystem createMailPool() {
        return new MailSystem();
    }

    @Override
    protected List<MailSystem> allInPool() {
        return repository().getAllInCache();
    }

    public MailSystem getMailSystem(long id) {
        return repository().getByMainKey(id);
    }

    public void delete(MailSystem mailSystem) {
        repository().delete(mailSystem);
    }

    @Override
    protected IMailTemplateDesign getTemplate(int template) {
        return mailCache.getOrThrow(template);
    }

    @Override
    protected List<String> convertArgs(List<String> list, MailArgs mailArgs) {
        return mailService.changerArgs(list,mailArgs.getArgs());
    }

    public List<Mail> pullMailFromSystem(long playerId) {
        MailPlayer mailPlayer = mailPlayerService.getEntity(playerId);
        Mails mails = this.checkUnreadMails(mailPlayer.getPid(), mailPlayer.getLastReadTime());
        if (mails.getMails().isEmpty()) {
            return Collections.emptyList();
        }
        mailPlayer.setLastReadTime(mails.getLastReadTime());
        mailPlayer.setReadTime(LocalDateTimeUtil.ofEpochSecond(mails.getLastReadTime()));
        mailPlayerService.update(mailPlayer);
        return mails.getMails();
    }

	@Override
	protected boolean customCondition(long receiver, MailSystem mailPool) {
		Player player = playerService.getEntity(receiver);
		if(player.getLevel() >= mailPool.getLevel()) {
			return true;
		}
		return false;
	}

	@Override
	public void start() throws Exception {
		List<MailSystem> updateMails = new ArrayList<>();
		for(MailSystem mailSystem : this.allInPool()) {
			if(mailSystem.getPostTime() == 0) {
				mailSystem.setPostTime(LocalDateTimeUtil.toEpochSecond(mailSystem.getInsertTime()));
				updateMails.add(mailSystem);
			}
		}
		this.repository().updateAll(updateMails);
	}
}
