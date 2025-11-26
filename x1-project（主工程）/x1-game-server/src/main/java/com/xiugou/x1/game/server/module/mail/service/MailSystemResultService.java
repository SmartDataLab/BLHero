/**
 * 
 */
package com.xiugou.x1.game.server.module.mail.service;

import org.springframework.stereotype.Service;

import com.xiugou.x1.game.server.foundation.service.OneToOneService;
import com.xiugou.x1.game.server.module.mail.module.MailSystemResult;

/**
 * @author YY
 *
 */
@Service
public class MailSystemResultService extends OneToOneService<MailSystemResult> {

	@Override
	protected MailSystemResult createWhenNull(long entityId) {
		return null;
	}
}
