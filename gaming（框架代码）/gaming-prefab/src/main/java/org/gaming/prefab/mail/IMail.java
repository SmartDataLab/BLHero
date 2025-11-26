/**
 * 
 */
package org.gaming.prefab.mail;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author YY
 *
 */
public interface IMail {

	long getId();
	int getTemplate();
	void setTemplate(int template);
	//邮件接收者
	long getReceiver();
	void setReceiver(long receiver);
	
	boolean isRead();
	void markUnread();
	void markRead();
	
	boolean isReceive();
	void markUnreceive();
	void markReceive();
	
	
	List<String> getTitleArgs();
	List<String> getContentArgs();
	//邮件的过期时间
	LocalDateTime getExpireTime();
	void setExpireTime(LocalDateTime expireTime);
	//附件
	String getAttachment();
	void setAttachment(String attachment);
	//游戏原因
	void setGameCause(int gameCause);
	void setGameCauseText(String gameCauseText);
	
	void setFromPoolId(long fromPoolId);
}
