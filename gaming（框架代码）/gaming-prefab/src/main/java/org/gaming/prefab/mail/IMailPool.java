/**
 * 
 */
package org.gaming.prefab.mail;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

/**
 * @author YY
 *
 */
public interface IMailPool {

	long getId();
	
	int getTemplate();
	void setTemplate(int template);
	
	List<String> getTitleArgs();
	List<String> getContentArgs();
	//邮递时间，即邮件的新增时间
	int getPostTime();
	void setPostTime(int postTime);
	//邮件的过期时间
	LocalDateTime getExpireTime();
	void setExpireTime(LocalDateTime expireTime);
	//附件
	String getAttachment();
	void setAttachment(String attachment);
	//游戏原因
	void setGameCause(int gameCause);
	int getGameCause();
	void setGameCauseText(String gameCauseText);
	String getGameCauseText();
	
	MailBelong getBelong();
	void setBelong(MailBelong belong);
	
	Set<Long> getReceivers();
	//该邮件对哪些服务器的玩家开放
	Set<Integer> getServerIds();
	
	void setFromId(long fromId);
}
