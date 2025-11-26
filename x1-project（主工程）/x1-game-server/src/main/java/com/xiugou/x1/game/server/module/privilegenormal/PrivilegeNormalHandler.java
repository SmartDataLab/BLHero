/**
 * 
 */
package com.xiugou.x1.game.server.module.privilegenormal;

import org.gaming.fakecmd.annotation.PlayerCmd;
import org.gaming.prefab.exception.Asserts;
import org.gaming.tool.DateTimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.xiugou.x1.design.constant.GameCause;
import com.xiugou.x1.design.constant.ItemType;
import com.xiugou.x1.design.constant.TipsCode;
import com.xiugou.x1.design.module.BattleConstCache;
import com.xiugou.x1.design.module.PlayerLevelCache;
import com.xiugou.x1.design.module.PrivilegeNormalCache;
import com.xiugou.x1.design.module.autogen.PrivilegeNormalAbstractCache.PrivilegeNormalCfg;
import com.xiugou.x1.design.struct.RewardThing;
import com.xiugou.x1.game.server.foundation.player.PlayerContext;
import com.xiugou.x1.game.server.module.bag.service.ThingService;
import com.xiugou.x1.game.server.module.player.AbstractModuleHandler;
import com.xiugou.x1.game.server.module.player.model.Player;
import com.xiugou.x1.game.server.module.player.service.PlayerService;
import com.xiugou.x1.game.server.module.privilegenormal.model.PrivilegeNormal;
import com.xiugou.x1.game.server.module.privilegenormal.service.PrivilegeNormalService;
import com.xiugou.x1.game.server.module.privilegenormal.struct.PrivilegeNormalData;

import pb.xiugou.x1.protobuf.privilegenormal.PrivilegeNormal.PrivilegeNormalInfoResponse;
import pb.xiugou.x1.protobuf.privilegenormal.PrivilegeNormal.PrivilegeNormalTakeExpRequest;
import pb.xiugou.x1.protobuf.privilegenormal.PrivilegeNormal.PrivilegeNormalTakeExpResponse;
import pb.xiugou.x1.protobuf.privilegenormal.PrivilegeNormal.PrivilegeNormalTakeRewardRequest;
import pb.xiugou.x1.protobuf.privilegenormal.PrivilegeNormal.PrivilegeNormalTakeRewardResponse;

/**
 * @author YY
 *
 */
@Controller
public class PrivilegeNormalHandler extends AbstractModuleHandler {

	@Autowired
	private PrivilegeNormalService privilegeNormalService;
	@Autowired
	private ThingService thingService;
	@Autowired
	private PrivilegeNormalCache privilegeNormalCache;
	@Autowired
	private PlayerService playerService;
	@Autowired
	private PlayerLevelCache playerLevelCache;
	@Autowired
	private BattleConstCache battleConstCache;
	
	@Override
	public InfoPriority infoPriority() {
		return InfoPriority.DETAIL;
	}

	@Override
	public void pushInfo(PlayerContext playerContext) {
		long playerId = playerContext.getId();
		PrivilegeNormal entity = privilegeNormalService.getEntity(playerId);
		
		PrivilegeNormalInfoResponse.Builder response = PrivilegeNormalInfoResponse.newBuilder();
		response.setExpPool(entity.getExpPool());
		for(PrivilegeNormalData data : entity.getDatas().values()) {
			response.addPrivileges(data.build());
		}
		playerContextManager.push(playerId, PrivilegeNormalInfoResponse.Proto.ID, response.build());
	}
	
	@PlayerCmd
	public PrivilegeNormalTakeRewardResponse takeReward(PlayerContext playerContext, PrivilegeNormalTakeRewardRequest request) {
		PrivilegeNormal entity = privilegeNormalService.getEntity(playerContext.getId());
		
		PrivilegeNormalData data = entity.getDatas().get(request.getPrivilegeId());
		Asserts.isTrue(data != null, TipsCode.PRIVILEGE_NORMAL_NOT_OPEN);
		Asserts.isTrue(data.getEndTime() == 0 || data.getEndTime() > DateTimeUtil.currMillis(), TipsCode.PRIVILEGE_NORMAL_NOT_OPEN);
		Asserts.isTrue(!data.isRewarded(), TipsCode.PRIVILEGE_NORMAL_TOOK);
		
		PrivilegeNormalCfg privilegeNormalCfg = privilegeNormalCache.getOrThrow(data.getId());
		
		data.setRewarded(true);
		privilegeNormalService.update(entity);
		
		thingService.add(playerContext.getId(), privilegeNormalCfg.getDailyRewards(), GameCause.PRIVILEGE_NORMAL_DAILY);
		
		PrivilegeNormalTakeRewardResponse.Builder response = PrivilegeNormalTakeRewardResponse.newBuilder();
		response.setPrivilegeId(data.getId());
		return response.build();
	}
	
	@PlayerCmd
	public PrivilegeNormalTakeExpResponse takeExp(PlayerContext playerContext, PrivilegeNormalTakeExpRequest request) {
		Player player = playerService.getEntity(playerContext.getId());
		Asserts.isTrue(player.getLevel() < playerLevelCache.all().size(), TipsCode.PLAYER_MAX_LV);
		
		PrivilegeNormal entity = privilegeNormalService.getEntity(playerContext.getId());
		Asserts.isTrue(entity.canGetExpPool(), TipsCode.PRIVILEGE_NORMAL_NOT_OPEN);
		Asserts.isTrue(entity.getExpPool() >= battleConstCache.getPrivilege_normal_exp_pool_max(), TipsCode.PRIVILEGE_NORMAL_EXP_NOT_FULL);
		
		thingService.add(playerContext.getId(), RewardThing.of(ItemType.EXP.getThingId(), entity.getExpPool()), GameCause.PRIVILEGE_NORMAL_EXP);
		
		PrivilegeNormalTakeExpResponse.Builder response = PrivilegeNormalTakeExpResponse.newBuilder();
		response.setExpPool(entity.getExpPool());
		return response.build();
	}
}
