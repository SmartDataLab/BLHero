/**
 * 
 */
package pojo.xiugou.x1.pojo.module.chat;

import org.gaming.db.annotation.Column;
import org.gaming.db.annotation.Id;
import org.gaming.db.annotation.Id.Strategy;
import org.gaming.db.annotation.MappedSuperclass;
import org.gaming.db.annotation.MappedSuperclass.Priority;
import org.gaming.db.orm.AbstractEntity;
import org.gaming.tool.LocalDateTimeUtil;

import pb.xiugou.x1.protobuf.chat.Chat.PbChatData;

/**
 * @author yy
 *
 */
@MappedSuperclass(sort = Priority._2)
public abstract class ChatEntity extends AbstractEntity {
	@Id(strategy = Strategy.AUTO)
    @Column(comment = "消息ID")
    private long id;
	@Column(name = "speaker_id", comment = "说话者ID")
	private long speakerId;
	@Column(name = "speaker_nick", comment = "说话者昵称")
	private String speakerNick;
	@Column(comment = "头像")
	private String head;
	@Column(comment = "性别")
	private int sex;
	@Column(comment = "聊天内容")
	private String content;
	@Column(name = "ori_content", comment = "聊天原始内容")
	private String oriContent;
	@Column(comment = "是否为可疑聊天内容")
	private boolean doubt;
	@Column(comment = "等级")
	private int level;
	@Column(name = "vip_level", comment = "vip等级")
	private int vipLevel;
	
	public long getSpeakerId() {
		return speakerId;
	}
	public void setSpeakerId(long speakerId) {
		this.speakerId = speakerId;
	}
	public String getSpeakerNick() {
		return speakerNick;
	}
	public void setSpeakerNick(String speakerNick) {
		this.speakerNick = speakerNick;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getOriContent() {
		return oriContent;
	}
	public void setOriContent(String oriContent) {
		this.oriContent = oriContent;
	}
	public String getHead() {
		return head;
	}
	public void setHead(String head) {
		this.head = head;
	}
	public int getSex() {
		return sex;
	}
	public void setSex(int sex) {
		this.sex = sex;
	}
	public boolean isDoubt() {
		return doubt;
	}
	public void setDoubt(boolean doubt) {
		this.doubt = doubt;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public int getVipLevel() {
		return vipLevel;
	}
	public void setVipLevel(int vipLevel) {
		this.vipLevel = vipLevel;
	}
	
	public PbChatData build() {
		PbChatData.Builder builder = PbChatData.newBuilder();
		builder.setId(this.id);
		builder.setSpeakerId(this.speakerId);
		builder.setSpeakerNick(this.speakerNick);
		builder.setSex(this.sex);
		builder.setHead(this.head);
		builder.setContent(this.content);
		builder.setLevel(this.level);
		builder.setVipLevel(this.vipLevel);
		builder.setTime(LocalDateTimeUtil.toEpochMilli(this.getInsertTime()));
		return builder.build();
	}
}
