/**
 * 
 */
package org.gaming.prefab.mail;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.gaming.prefab.IGameCause;
import org.gaming.prefab.thing.IRewardThing;
import org.gaming.tool.DateTimeUtil;
import org.gaming.tool.GsonUtil;
import org.gaming.tool.LocalDateTimeUtil;

/**
 * @author YY
 *
 */
public abstract class MailPoolAbstractService<P extends IMailPool, M extends IMail> {

	public abstract void saveMail(P mail);
	
	protected abstract M createMail();
	protected abstract P createMailPool();
	
	protected abstract List<P> allInPool();
	
	public final class Mails {
		private List<M> mails = new ArrayList<>();
		/**
		 * 最后一封读取到的邮件时间
		 */
		private int lastReadTime;
		public List<M> getMails() {
			return mails;
		}
		public int getLastReadTime() {
			return lastReadTime;
		}
	}
	
	public final Mails checkUnreadMails(long receiver, int currLastReadTime) {
		Mails mails = new Mails();
		int nowTime = DateTimeUtil.currSecond();
		
		int maxLastReadTime = currLastReadTime;
		for(P mailPool : allInPool()) {
			//邮件已经过期
			int expireTime = LocalDateTimeUtil.toEpochSecond(mailPool.getExpireTime());
			if(expireTime <= nowTime) {
				continue;
			}
			//邮件已经被读取
			int postTime = mailPool.getPostTime();
			if(postTime <= currLastReadTime) {
				continue;
			}
			
			if (mailPool.getBelong() == MailBelong.EVERYONE) {
                //所有人都可以读取
            } else if (mailPool.getBelong() == MailBelong.SOMEONE && mailPool.getReceivers().contains(receiver)) {
                //某些人的邮件只能指定的人才可以读取
            } else {
                continue;
            }
			if(!customCondition(receiver, mailPool)) {
				continue;
			}
			
			M mail = toMail(receiver, mailPool);
			mails.mails.add(mail);
			if(postTime > maxLastReadTime) {
				maxLastReadTime = postTime;
			}
		}
		mails.lastReadTime = maxLastReadTime;
		return mails;
	}
	
	/**
     * 保存系统邮件
     * @param mailScene 邮件类型
     * @param title 邮件标题
     * @param content 邮件内容
     * @param belong 邮件归属
     * @param receivers 发送给的指定玩家
     * @param attachment 附件
     * @param gameCause 游戏起因
     */
	public final P createMail(IMailTemplateEnum templateEnum, MailArgs titleArgs, MailArgs contentArgs, MailBelong belong,
		Set<Long> receivers, List<? extends IRewardThing> attachment, IGameCause gameCause, Set<Integer> serverIds, long fromId) throws Exception {
		P mailPool = createMailPool();
		mailPool.setTemplate(templateEnum.getValue());

		IMailTemplateDesign mailTemplateDesign = getTemplate(templateEnum.getValue());
		if(titleArgs != null) {
			mailPool.getTitleArgs().addAll(convertArgs(mailTemplateDesign.getTitleFormatRules(), titleArgs));
		}
		if(contentArgs != null) {
			mailPool.getContentArgs().addAll(convertArgs(mailTemplateDesign.getContentFormatRules(), contentArgs));
		}
		mailPool.setBelong(belong);
		mailPool.getReceivers().addAll(receivers);
		if(attachment != null) {
			mailPool.setAttachment(GsonUtil.toJson(attachment));
		} else {
			mailPool.setAttachment("");
		}
		mailPool.setExpireTime(LocalDateTimeUtil.now().plusDays(mailTemplateDesign.getExpireDay()));
		mailPool.getServerIds().addAll(serverIds);
		mailPool.setGameCause(gameCause.getCode());
		mailPool.setGameCauseText(gameCause.getDesc());
		mailPool.setFromId(fromId);
		mailPool.setPostTime(DateTimeUtil.currSecond());
		return mailPool;
	}
    
    private M toMail(long receiver, IMailPool mailPool) {
    	int template = mailPool.getTemplate();
    	IMailTemplateDesign mailTemplateDesign = getTemplate(template);
    	
    	M mail = createMail();
    	mail.setReceiver(receiver);
        mail.setTemplate(template);
        mail.markUnread();
        mail.markUnreceive();
        mail.setGameCause(mailPool.getGameCause());
        mail.setGameCauseText(mailPool.getGameCauseText());
        
        mail.getTitleArgs().addAll(mailPool.getTitleArgs());
        mail.getContentArgs().addAll(mailPool.getContentArgs());
        mail.setAttachment(mailPool.getAttachment());
        mail.setExpireTime(LocalDateTimeUtil.now().plusDays(mailTemplateDesign.getExpireDay()));
        mail.setFromPoolId(mailPool.getId());
    	return mail;
    }
    
    protected abstract IMailTemplateDesign getTemplate(int template);
    
    protected abstract List<String> convertArgs(List<String> formatRules, MailArgs mailArgs);
    
    /**
     * 自定义的收取到邮件的限制判断
     * @param receiver
     * @param mailPool
     * @return
     */
    protected abstract boolean customCondition(long receiver, P mailPool);
}
