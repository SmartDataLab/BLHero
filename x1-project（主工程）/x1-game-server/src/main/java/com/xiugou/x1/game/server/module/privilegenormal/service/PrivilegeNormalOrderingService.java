/**
 * 
 */
package com.xiugou.x1.game.server.module.privilegenormal.service;

import java.util.List;

import org.gaming.prefab.exception.Asserts;
import org.gaming.tool.DateTimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xiugou.x1.design.constant.TipsCode;
import com.xiugou.x1.design.module.PrivilegeNormalCache;
import com.xiugou.x1.design.module.autogen.PrivilegeNormalAbstractCache.PrivilegeNormalCfg;
import com.xiugou.x1.design.module.autogen.RechargeProductAbstractCache.RechargeProductCfg;
import com.xiugou.x1.design.struct.RewardThing;
import com.xiugou.x1.game.server.module.privilegenormal.model.PrivilegeNormal;
import com.xiugou.x1.game.server.module.privilegenormal.struct.PrivilegeNormalData;
import com.xiugou.x1.game.server.module.recharge.constant.ProductType;
import com.xiugou.x1.game.server.module.recharge.model.Recharge;
import com.xiugou.x1.game.server.module.recharge.service.RechargeOrderingService;

import pb.xiugou.x1.protobuf.privilegenormal.PrivilegeNormal.PrivilegeNormalRechargeMessage;

/**
 * @author YY
 *
 */
@Service
public class PrivilegeNormalOrderingService extends RechargeOrderingService {

	@Autowired
	private PrivilegeNormalService privilegeNormalService;
	@Autowired
	private PrivilegeNormalCache privilegeNormalCache;
	
	@Override
	public ProductType productType() {
		return ProductType.PRIVILEGE_NORMAL;
	}

	@Override
	public void checkOrdering(long playerId, RechargeProductCfg rechargeProductCfg, String productData) {
		PrivilegeNormalCfg privilegeNormalCfg = privilegeNormalCache.getInRechargeIdIndex(rechargeProductCfg.getId());
		
//		PrivilegeNormalChoose choose = GsonUtil.parseJson(productData, PrivilegeNormalChoose.class);
//		boolean choice = false;
//		for(RewardThing rewardThing : privilegeNormalCfg.getChooseRewards()) {
//			if(rewardThing.getThingId() == choose.getItem()) {
//				choice = true;
//				break;
//			}
//		}
//		Asserts.isTrue(choice, TipsCode.PRIVILEGE_NORMAL_NOT_CHOOSE);
		
		PrivilegeNormal privilegeNormal = privilegeNormalService.getEntity(playerId);
		PrivilegeNormalData privilegeNormalData = privilegeNormal.getDatas().get(privilegeNormalCfg.getId());
		if(privilegeNormalData != null) {
			Asserts.isTrue(
					privilegeNormalData.getEndTime() > 0
							&& privilegeNormalData.getEndTime() <= DateTimeUtil.currMillis(),
					TipsCode.PRIVILEGE_NORMAL_NOT_END);
		}
	}

	@Override
	public void buySuccess(long playerId, Recharge recharge, List<RewardThing> outRewards) {
		PrivilegeNormalCfg privilegeNormalCfg = privilegeNormalCache.getInRechargeIdIndex(recharge.getProductId());
		
		//充值奖励
		outRewards.addAll(privilegeNormalCfg.getRechargeRewards());
		outRewards.addAll(privilegeNormalCfg.getDailyRewards());
		
		//自选奖励
//		PrivilegeNormalChoose choose = GsonUtil.parseJson(recharge.getProductData(), PrivilegeNormalChoose.class);
//		for(RewardThing rewardThing : privilegeNormalCfg.getChooseRewards()) {
//			if(rewardThing.getThingId() == choose.getItem()) {
//				outRewards.add(rewardThing);
//				break;
//			}
//		}
		
		PrivilegeNormal privilegeNormal = privilegeNormalService.getEntity(playerId);
		PrivilegeNormalData privilegeNormalData = privilegeNormal.getDatas().get(privilegeNormalCfg.getId());
		if(privilegeNormalData == null) {
			privilegeNormalData = new PrivilegeNormalData();
			privilegeNormalData.setId(privilegeNormalCfg.getId());
			privilegeNormal.getDatas().put(privilegeNormalData.getId(), privilegeNormalData);
		}
		privilegeNormalData.setRewarded(true);
		if(privilegeNormalCfg.getDays() > 0) {
			privilegeNormalData.setEndTime(DateTimeUtil.currMillis() + DateTimeUtil.ONE_DAY_MILLIS * privilegeNormalCfg.getDays());
		}
		privilegeNormalService.update(privilegeNormal);
		
		PrivilegeNormalRechargeMessage.Builder builder = PrivilegeNormalRechargeMessage.newBuilder();
		builder.setPrivilege(privilegeNormalData.build());
		playerContextManager.push(playerId, PrivilegeNormalRechargeMessage.Proto.ID, builder.build());
	}

}
