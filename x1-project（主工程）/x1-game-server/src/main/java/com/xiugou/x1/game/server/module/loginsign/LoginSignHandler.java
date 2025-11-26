/**
 * 
 */
package com.xiugou.x1.game.server.module.loginsign;

import org.gaming.fakecmd.annotation.PlayerCmd;
import org.gaming.prefab.exception.Asserts;
import org.gaming.prefab.thing.NoticeType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.xiugou.x1.design.constant.GameCause;
import com.xiugou.x1.design.constant.TipsCode;
import com.xiugou.x1.design.module.LoginGiftCache;
import com.xiugou.x1.design.module.SignGiftCache;
import com.xiugou.x1.design.module.autogen.LoginGiftAbstractCache.LoginGiftCfg;
import com.xiugou.x1.design.module.autogen.SignGiftAbstractCache.SignGiftCfg;
import com.xiugou.x1.game.server.foundation.player.PlayerContext;
import com.xiugou.x1.game.server.module.bag.service.ThingService;
import com.xiugou.x1.game.server.module.loginsign.model.LoginSign;
import com.xiugou.x1.game.server.module.loginsign.service.LoginSignService;
import com.xiugou.x1.game.server.module.player.AbstractModuleHandler;

import pb.xiugou.x1.protobuf.loginsign.LoginSign.LoginSignInfoResponse;
import pb.xiugou.x1.protobuf.loginsign.LoginSign.LoginSignTakeLoginRewardRequest;
import pb.xiugou.x1.protobuf.loginsign.LoginSign.LoginSignTakeLoginRewardResponse;
import pb.xiugou.x1.protobuf.loginsign.LoginSign.LoginSignTakeSignRewardRequest;
import pb.xiugou.x1.protobuf.loginsign.LoginSign.LoginSignTakeSignRewardResponse;

/**
 * @author YY
 * 登录与签到
 */
@Controller
public class LoginSignHandler extends AbstractModuleHandler {

	@Autowired
	private LoginSignService loginSignService;
	@Autowired
	private ThingService thingService;
	@Autowired
	private LoginGiftCache loginGiftCache;
	@Autowired
	private SignGiftCache signGiftCache;
	
	@Override
	public InfoPriority infoPriority() {
		return InfoPriority.DETAIL;
	}

	@Override
	public void pushInfo(PlayerContext playerContext) {
		long playerId = playerContext.getId();
		LoginSign loginSign = loginSignService.getEntity(playerId);
		
		LoginSignInfoResponse.Builder response = LoginSignInfoResponse.newBuilder();
		response.setLoginDay(loginSign.getLoginDay());
		if(loginSign.isTakeLoginReward()) {
			response.setOpenServerDay(loginSign.getLoginDay());
		} else {
			response.setOpenServerDay(loginSign.getLoginDay() + 1);
		}
		response.setSignDay(loginSign.getSignDay());
		if(loginSign.isTakeSignReward()) {
			response.setCanSignDay(0);
		} else {
			response.setCanSignDay(loginSign.getSignDay() + 1);
		}
		playerContextManager.push(playerId, LoginSignInfoResponse.Proto.ID, response.build());
	}
	
	@PlayerCmd
	public LoginSignTakeLoginRewardResponse takeLoginReward(PlayerContext playerContext, LoginSignTakeLoginRewardRequest request) {
		LoginSign loginSign = loginSignService.getEntity(playerContext.getId());
		
		Asserts.isTrue(!loginSign.isTakeLoginReward(), TipsCode.LOGINSIGN_LOGIN_REWARD_TOOK);
		int loginDay = loginSign.getLoginDay() + 1;
		LoginGiftCfg loginGiftCfg = loginGiftCache.getOrNull(loginDay);
		Asserts.isTrue(loginGiftCfg != null, TipsCode.LOGINSIGN_LOGIN_REWARD_MAX);
		
		loginSign.setLoginDay(loginDay);
		loginSign.setTakeLoginReward(true);
		loginSignService.update(loginSign);
		
		thingService.add(playerContext.getId(), loginGiftCfg.getReward(), GameCause.LOGINSIGN_LOGIN, NoticeType.TIPS);
		
		LoginSignTakeLoginRewardResponse.Builder response = LoginSignTakeLoginRewardResponse.newBuilder();
		response.setLoginDay(loginSign.getLoginDay());
		return response.build();
	}
	
	@PlayerCmd
	public LoginSignTakeSignRewardResponse takeSignReward(PlayerContext playerContext, LoginSignTakeSignRewardRequest request) {
		LoginSign loginSign = loginSignService.getEntity(playerContext.getId());
		Asserts.isTrue(!loginSign.isTakeSignReward(), TipsCode.LOGINSIGN_SIGN_REWARD_TOOK);
		
		int signDay = loginSign.getSignDay() + 1;
		SignGiftCfg signGiftCfg = signGiftCache.getOrNull(signDay);
		
		loginSign.setSignDay(loginSign.getSignDay() + 1);
		loginSign.setTakeSignReward(true);
		loginSignService.update(loginSign);
		
		thingService.add(playerContext.getId(), signGiftCfg.getReward(), GameCause.LOGINSIGN_SIGN);
		
		LoginSignTakeSignRewardResponse.Builder response = LoginSignTakeSignRewardResponse.newBuilder();
		response.setSignDay(loginSign.getSignDay());
		response.setCanSignDay(0);
		return response.build();
	}
	
	

}
