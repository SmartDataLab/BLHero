/**
 * 
 */
package com.xiugou.x1.game.server.module.promotions.p1010huodongmubiao.service;

import java.util.List;

import org.gaming.prefab.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xiugou.x1.game.server.module.promotion.constant.PromotionLogicType;
import com.xiugou.x1.game.server.module.promotion.model.PromotionControl;
import com.xiugou.x1.game.server.module.promotion.service.PromotionLogicService;
import com.xiugou.x1.game.server.module.promotions.p1010huodongmubiao.model.HuoDongMuBiao;

/**
 * @author yy
 *
 */
@Service
public class HuoDongMuBiaoPromotionService extends PromotionLogicService {

	@Autowired
	private HuoDongMuBiaoService huoDongMuBiaoService;
	
	@Override
	public PromotionLogicType promotionLogicType() {
		return PromotionLogicType.HUO_DONG_MU_BIAO;
	}

	@Override
	public void whenStart(PromotionControl control) {
		//什么都不用做
	}

	@Override
	public void whenStill(PromotionControl control) {
		//什么都不用做
	}

	@Override
	public void whenEnd(PromotionControl control) {
		//什么都不用做
	}

	@Override
	public void handlePromotionEnd(long playerId, PromotionControl control) {
		//什么都不用做
	}

	public static class HuoDongMuBiaoParam {
		public final int type;
		public final int rewardRound;
		public HuoDongMuBiaoParam(List<Integer> params) {
			this.type = params.get(0);
			this.rewardRound = params.get(1);
		}
	}

	@Override
	public boolean showLoginRedPoint(long playerId, int typeId) {
		HuoDongMuBiao entity = huoDongMuBiaoService.getEntity(playerId, typeId);
		for(Task task : entity.getTasks()) {
			if(task.isDone()) {
				return true;
			}
		}
		return false;
	}
}
