/**
 * 
 */
package org.gaming.prefab.mail;

import java.util.Collections;
import java.util.List;

import org.gaming.prefab.IGameCause;
import org.gaming.prefab.thing.IRewardThing;
import org.gaming.tool.GsonUtil;
import org.gaming.tool.LocalDateTimeUtil;

/**
 * @author YY
 *
 */
public abstract class MailAbstractService<M extends IMail> {

	public abstract List<M> mails(long entityId);
	
	public abstract void insert(List<M> mails);
	
	public abstract void update(List<M> mails);
	
	public abstract void delete(List<M> mails);
	
	protected abstract M createMail();
	
	/**
     * 发送没有标题参数、没有内容参数的邮件
     *
     * @param receiver
     * @param mailScene
     * @param attachment
     * @param gameEvent
     * @return
     */
	public M newMail(long receiver, IMailTemplateEnum templateEnum, IRewardThing attachment, IGameCause gameCause) {
		List<IRewardThing> attachments = attachment == null ? Collections.emptyList()
				: Collections.singletonList(attachment);
		return newMail(receiver, templateEnum, null, null, attachments, gameCause);
	}
	
	public M newMail(long receiver, IMailTemplateEnum templateEnum, List<? extends IRewardThing> attachments, IGameCause gameCause) {
		return newMail(receiver, templateEnum, null, null, attachments, gameCause);
	}
	
	/**
     * 发送没有标题参数的邮件
     *
     * @param receiver
     * @param mailScene
     * @param content
     * @param attachment
     * @param gameEvent
     * @return
     */
	public M newMail(long receiver, IMailTemplateEnum templateEnum, MailArgs content,
			List<? extends IRewardThing> attachments, IGameCause gameCause) {
		return newMail(receiver, templateEnum, null, content, attachments, gameCause);
	}
    /**
     * @param receiver
     * @param mailScene 	邮件发送的场景，邮件配置表的ID
     * @param title     	标题的参数
     * @param content   	内容的参数
     * @param attachment	附件
     * @param gameEvent 	邮件发出时的游戏事件
     * @return
     */
	public M newMail(long receiver, IMailTemplateEnum templateEnum, MailArgs titleArgs, MailArgs contentArgs,
			List<? extends IRewardThing> attachments, IGameCause gameCause) {
        return newMail(receiver, templateEnum.getValue(), titleArgs, contentArgs, attachments, gameCause);
    }
	
	public M newMail(long receiver, int mailTemplate, MailArgs titleArgs, MailArgs contentArgs,
			List<? extends IRewardThing> attachments, IGameCause gameCause) {
        M mail = createMail();
        mail.setReceiver(receiver);
        mail.setTemplate(mailTemplate);
        mail.markUnread();
        mail.markUnreceive();
        mail.setGameCause(gameCause.getCode());
        mail.setGameCauseText(gameCause.getDesc());
        
        IMailTemplateDesign mailTemplateDesign = getTemplate(mailTemplate);
        if (titleArgs != null) {
            mail.getTitleArgs().addAll(convertArgs(mailTemplateDesign.getTitleFormatRules(), titleArgs));
        }
        if (contentArgs != null) {
            mail.getContentArgs().addAll(convertArgs(mailTemplateDesign.getContentFormatRules(), contentArgs));
        }
        if (attachments != null) {
            mail.setAttachment(GsonUtil.toJson(attachments));
        } else {
            mail.setAttachment("");
        }
        mail.setExpireTime(LocalDateTimeUtil.now().plusDays(mailTemplateDesign.getExpireDay()));
        return mail;
    }
    
    protected abstract IMailTemplateDesign getTemplate(int mailTemplate);
    
    protected abstract List<String> convertArgs(List<String> formatRules, MailArgs mailArgs);
}
