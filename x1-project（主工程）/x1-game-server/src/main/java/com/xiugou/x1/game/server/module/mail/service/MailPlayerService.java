/**
 * 
 */
package com.xiugou.x1.game.server.module.mail.service;

import org.gaming.tool.LocalDateTimeUtil;
import org.springframework.stereotype.Service;

import com.xiugou.x1.game.server.foundation.service.PlayerOneToOneService;
import com.xiugou.x1.game.server.module.mail.module.MailPlayer;

/**
 * @author YY
 *
 */
@Service
public class MailPlayerService extends PlayerOneToOneService<MailPlayer> {

	@Override
	protected MailPlayer createWhenNull(long entityId) {
		MailPlayer entity = new MailPlayer();
		entity.setPid(entityId);
		return entity;
	}

	@Override
	protected void onGet(MailPlayer t) {
		if(t.getLastReadTime() == 0) {
			t.setLastReadTime(LocalDateTimeUtil.toEpochSecond(t.getReadTime()));
			this.update(t);
		}
	}
}
