/**
 *
 */
package pojo.xiugou.x1.pojo.log.mail;

import java.time.LocalDateTime;

import org.gaming.db.annotation.Column;
import org.gaming.db.annotation.Id;
import org.gaming.db.annotation.Id.Strategy;
import org.gaming.db.annotation.LogTable;
import org.gaming.db.annotation.enuma.AsyncType;
import org.gaming.db.orm.AbstractEntity;
import org.springframework.stereotype.Repository;

/**
 * @author YY
 *
 */
@Repository
@LogTable(name = "mail_log", comment = "邮件日志表", dbAlias = "log", asyncType = AsyncType.INSERT, byColumn = "time")
public class MailLog extends AbstractEntity {
    @Id(strategy = Strategy.AUTO)
    @Column(comment = "日志ID")
    private long id;
    @Column(name = "ori_mail_id", comment = "原邮件ID")
    private long oriMailId;
    @Column(name = "owner_id", comment = "玩家ID")
    private long ownerId;
    @Column(name = "owner_name", comment = "玩家名字")
    private String ownerName;
    @Column(comment = "邮件模板ID")
    private int template;
    @Column(name = "title_args", comment = "标题参数", length = 2500)
    private String titleArgs;
    @Column(name = "content_args", comment = "内容参数", length = 2500)
    private String contentArgs;
    @Column(comment = "附件", readonly = true, length = 2500)
    private String attachment;
    @Column(comment = "是否已读")
    private boolean read;
    @Column(comment = "是否已领")
    private boolean receive;
    @Column(name = "expire_time", comment = "到期时间")
    private LocalDateTime expireTime = LocalDateTime.now();
    @Column(name = "game_cause", comment = "游戏事件")
    private int gameCause;
    @Column(name = "game_cause_text", comment = "游戏事件")
    private String gameCauseText;
    @Column(name = "from_pool_id", comment = "来自哪个系统邮件")
    private long fromPoolId;
    @Column(comment = "发生时间")
    private LocalDateTime time;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getTemplate() {
        return template;
    }

    public void setTemplate(int template) {
        this.template = template;
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

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(long ownerId) {
        this.ownerId = ownerId;
    }

	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	public long getOriMailId() {
		return oriMailId;
	}

	public void setOriMailId(long oriMailId) {
		this.oriMailId = oriMailId;
	}

	public String getTitleArgs() {
		return titleArgs;
	}

	public void setTitleArgs(String titleArgs) {
		this.titleArgs = titleArgs;
	}

	public String getContentArgs() {
		return contentArgs;
	}

	public void setContentArgs(String contentArgs) {
		this.contentArgs = contentArgs;
	}
}
