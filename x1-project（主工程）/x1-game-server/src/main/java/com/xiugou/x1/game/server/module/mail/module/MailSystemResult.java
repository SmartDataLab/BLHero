/**
 * 
 */
package com.xiugou.x1.game.server.module.mail.module;

import org.gaming.db.annotation.Column;
import org.gaming.db.annotation.Id;
import org.gaming.db.annotation.Id.Strategy;
import org.gaming.db.annotation.JvmCache;
import org.gaming.db.annotation.Table;
import org.gaming.db.orm.AbstractEntity;
import org.springframework.stereotype.Repository;

/**
 * @author YY
 *
 */
@Repository
@JvmCache
@Table(name = "mail_system_result", comment = "系统邮件记录表", dbAlias = "game")
public class MailSystemResult extends AbstractEntity {
	@Id(strategy = Strategy.IDENTITY)
	@Column(comment = "后台邮件ID")
	private long id;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
}
