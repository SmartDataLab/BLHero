/**
 * 
 */
package pojo.xiugou.x1.pojo.module.mail;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author YY
 *
 */
public class MailTable {
	private List<MailData> datas = new ArrayList<>();
	
	public static class MailData {
		private long id;
		private long receiver;
		private int template;
		private List<String> titleArgs = new ArrayList<>();
		private List<String> contentArgs = new ArrayList<>();
		private String attachment;
		private boolean read;
		private boolean receive;
		private LocalDateTime expireTime = LocalDateTime.now();
		private int gameCause;
		private String gameCauseText;
		private long fromPoolId;
		private long channelId;
		
		public long getId() {
			return id;
		}
		public void setId(long id) {
			this.id = id;
		}
		public long getReceiver() {
			return receiver;
		}
		public void setReceiver(long receiver) {
			this.receiver = receiver;
		}
		public int getTemplate() {
			return template;
		}
		public void setTemplate(int template) {
			this.template = template;
		}
		public List<String> getTitleArgs() {
			return titleArgs;
		}
		public void setTitleArgs(List<String> titleArgs) {
			this.titleArgs = titleArgs;
		}
		public List<String> getContentArgs() {
			return contentArgs;
		}
		public void setContentArgs(List<String> contentArgs) {
			this.contentArgs = contentArgs;
		}
		public String getAttachment() {
			return attachment;
		}
		public void setAttachment(String attachment) {
			this.attachment = attachment;
		}
		public boolean isRead() {
			return read;
		}
		public void setRead(boolean read) {
			this.read = read;
		}
		public boolean isReceive() {
			return receive;
		}
		public void setReceive(boolean receive) {
			this.receive = receive;
		}
		public LocalDateTime getExpireTime() {
			return expireTime;
		}
		public void setExpireTime(LocalDateTime expireTime) {
			this.expireTime = expireTime;
		}
		public int getGameCause() {
			return gameCause;
		}
		public void setGameCause(int gameCause) {
			this.gameCause = gameCause;
		}
		public String getGameCauseText() {
			return gameCauseText;
		}
		public void setGameCauseText(String gameCauseText) {
			this.gameCauseText = gameCauseText;
		}
		public long getFromPoolId() {
			return fromPoolId;
		}
		public void setFromPoolId(long fromPoolId) {
			this.fromPoolId = fromPoolId;
		}
		public long getChannelId() {
			return channelId;
		}
		public void setChannelId(long channelId) {
			this.channelId = channelId;
		}
	}


	public List<MailData> getDatas() {
		return datas;
	}


	public void setDatas(List<MailData> datas) {
		this.datas = datas;
	} 
}
