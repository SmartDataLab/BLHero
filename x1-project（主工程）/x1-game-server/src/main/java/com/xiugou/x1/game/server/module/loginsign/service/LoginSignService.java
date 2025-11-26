/**
 * 
 */
package com.xiugou.x1.game.server.module.loginsign.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xiugou.x1.design.module.SignGiftCache;
import com.xiugou.x1.game.server.foundation.service.PlayerOneToOneResetableService;
import com.xiugou.x1.game.server.module.loginsign.model.LoginSign;

/**
 * @author YY
 *
 */
@Service
public class LoginSignService extends PlayerOneToOneResetableService<LoginSign> {

	@Autowired
	private SignGiftCache signGiftCache;
	
	@Override
	protected LoginSign createWhenNull(long entityId) {
		LoginSign loginSign = new LoginSign();
		loginSign.setPid(entityId);
		return loginSign;
	}

	@Override
	protected void doDailyReset(LoginSign entity) {
		//TODO 重置签到天数
		if(entity.getSignDay() >= signGiftCache.all().size()) {
			entity.setSignDay(0);
		}
		entity.setTakeLoginReward(false);
		entity.setTakeSignReward(false);
	}
}
