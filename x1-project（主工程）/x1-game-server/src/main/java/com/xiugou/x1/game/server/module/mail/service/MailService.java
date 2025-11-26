package com.xiugou.x1.game.server.module.mail.service;

import java.util.ArrayList;
import java.util.List;

import org.gaming.db.repository.CacheRepository;
import org.gaming.db.usecase.SlimDao;
import org.gaming.prefab.mail.IMailTemplateDesign;
import org.gaming.prefab.mail.MailAbstractService;
import org.gaming.prefab.mail.MailArgs;
import org.gaming.prefab.thing.NoticeType;
import org.gaming.tool.GsonUtil;
import org.gaming.tool.LocalDateTimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xiugou.x1.design.module.MailCache;
import com.xiugou.x1.design.struct.RewardThing;
import com.xiugou.x1.game.server.foundation.player.PlayerContextManager;
import com.xiugou.x1.game.server.module.mail.constant.MailConstant;
import com.xiugou.x1.game.server.module.mail.log.MailLogger;
import com.xiugou.x1.game.server.module.mail.module.Mail;
import com.xiugou.x1.game.server.module.ministruct.PbHelper;
import com.xiugou.x1.game.server.module.player.model.Player;
import com.xiugou.x1.game.server.module.player.service.PlayerService;

import pb.xiugou.x1.protobuf.mail.Mail.MailPushMessage;
import pb.xiugou.x1.protobuf.mail.Mail.PbMailData;

/**
 * @author yh
 * @date 2023/5/31
 * @apiNote
 */

@Service
public class MailService extends MailAbstractService<Mail> {
    @Autowired
    private PlayerContextManager playerContextManager;
    @Autowired
    private MailCache mailCache;
    @Autowired
    private MailLogger mailLogger;
    @Autowired
    private MailSystemService mailSystemService;
    @Autowired
    private PlayerService playerService;

    private CacheRepository<Mail> repository;

    protected CacheRepository<Mail> repository() {
        if (repository == null) {
            repository = SlimDao.getCacheRepository(Mail.class);
        }
        return repository;
    }

    @Override
    public List<Mail> mails(long entityId) {
        List<Mail> newMails = mailSystemService.pullMailFromSystem(entityId);
        if (!newMails.isEmpty()) {
            repository().insertAll(newMails);
        }
        return repository().listByKeys(entityId);
    }

    public Mail getMail(long entityId, long mailId) {
        return repository().getByKeys(entityId, mailId);
    }

    @Override
    protected Mail createMail() {
        return new Mail();
    }

    @Override
    protected List<String> convertArgs(List<String> formatRules, MailArgs mailArgs) {
       return changerArgs(formatRules, mailArgs.getArgs());
    }

    /**
     * 邮件参数转化规则
     * */
    public List<String> changerArgs(List<String> formatRules, Object[] args){
        List<String> result = new ArrayList<>();
        for (int i = 0; i < formatRules.size() && i < args.length; i++) {
            String rule = formatRules.get(i);
            if (MailConstant.ID_TO_NICK.equals(rule)) {
                long playerId = Long.parseLong(args[i].toString());
                Player player = playerService.getEntity(playerId);
                result.add(player.getNick());
            } else {
                //不用转化
                result.add(args[i].toString());
            }//后续扩展
        }
        return result;
    }



    @Override
    public void insert(List<Mail> mails) {
        repository().insertAll(mails);
    }

    public void insert(Mail mail) {
        repository().insert(mail);
    }

    @Override
    public void update(List<Mail> mails) {
        repository().updateAll(mails);
    }

    public void update(Mail mail) {
        repository().update(mail);
    }

    @Override
    public void delete(List<Mail> mails) {
        repository().deleteAll(mails);
        mailLogger.logOnDelete(mails);
    }

    /**
     * 发邮
     */
    public void sendMail(Mail mail, NoticeType noticeType) {
        this.insert(mail);
        if (noticeType != NoticeType.SLIENT) {
        	MailPushMessage.Builder builder = MailPushMessage.newBuilder();
            builder.addMails(build(mail));
            playerContextManager.push(mail.getReceiver(), MailPushMessage.Proto.ID, builder.build());
        }
    }

    public void sendMail(List<Mail> mails, NoticeType noticeType) {
        this.insert(mails);
        if (noticeType != NoticeType.SLIENT) {
            for (Mail mail : mails) {
            	if(!playerContextManager.isOnline(mail.getReceiver())) {
            		continue;
            	}
            	MailPushMessage.Builder builder = MailPushMessage.newBuilder();
                builder.addMails(build(mail));
                playerContextManager.push(mail.getReceiver(), MailPushMessage.Proto.ID, builder.build());
            }
        }
    }


    public PbMailData build(Mail mail) {
        PbMailData.Builder builder = PbMailData.newBuilder();
        builder.setId(mail.getId());
        builder.setTemplateId(mail.getTemplate());
        builder.addAllTitle(mail.getTitleArgs());
        builder.addAllContent(mail.getContentArgs());
        String attachment = mail.getAttachment();
        List<RewardThing> rewardThings = GsonUtil.getList(attachment, RewardThing.class);
        for (RewardThing rewardThing : rewardThings) {
            builder.addAttachment(PbHelper.build(rewardThing));
        }
        builder.setRead(mail.isRead());
        builder.setReceive(mail.isReceive());
        builder.setSendTime(LocalDateTimeUtil.toEpochMilli(mail.getInsertTime()));
        return builder.build();
    }

	@Override
	protected IMailTemplateDesign getTemplate(int mailTemplate) {
		return mailCache.getOrThrow(mailTemplate);
	}
}
