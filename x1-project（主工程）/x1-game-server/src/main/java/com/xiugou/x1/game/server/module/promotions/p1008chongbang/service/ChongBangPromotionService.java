/**
 * 
 */
package com.xiugou.x1.game.server.module.promotions.p1008chongbang.service;

import java.util.ArrayList;
import java.util.List;

import org.gaming.prefab.mail.MailArgs;
import org.gaming.prefab.thing.NoticeType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xiugou.x1.design.constant.GameCause;
import com.xiugou.x1.design.module.ActiveRankRewardsCache;
import com.xiugou.x1.design.module.autogen.ActiveRankRewardsAbstractCache.ActiveRankRewardsCfg;
import com.xiugou.x1.design.module.autogen.ActiveTemplateAbstractCache.ActiveTemplateCfg;
import com.xiugou.x1.game.server.module.mail.module.Mail;
import com.xiugou.x1.game.server.module.mail.service.MailService;
import com.xiugou.x1.game.server.module.promotion.constant.PromotionLogicType;
import com.xiugou.x1.game.server.module.promotion.model.PromotionControl;
import com.xiugou.x1.game.server.module.promotion.service.PromotionLogicService;
import com.xiugou.x1.game.server.module.rank.constant.RankType;
import com.xiugou.x1.game.server.module.rank.model.Ranker;
import com.xiugou.x1.game.server.module.rank.service.PlayerRankService;

/**
 * @author yy
 *
 */
@Service
public class ChongBangPromotionService extends PromotionLogicService {

	@Autowired
	private ActiveRankRewardsCache activeRankRewardsCache;
	@Autowired
	private MailService mailService;

	@Override
	public PromotionLogicType promotionLogicType() {
		return PromotionLogicType.CHONG_BANG;
	}

	@Override
	public void whenStart(PromotionControl control) {
		ActiveTemplateCfg activeTemplateCfg = activeTemplateCache.getOrThrow(control.getTypeId());
		ChongBangParam param = new ChongBangParam(activeTemplateCfg.getOpenParams());
		
		if (param.type != 2) {
			return;
		}
		PlayerRankService playerRankService = PlayerRankService.getService(RankType.getRankType(param.rankType));
		if(playerRankService != null) {
			playerRankService.clearRankAndDb();
		}
	}

	@Override
	public void whenStill(PromotionControl control) {
		// 什么都不用做
	}

	@Override
	public void whenEnd(PromotionControl control) {
		ActiveTemplateCfg activeTemplateCfg = activeTemplateCache.getOrThrow(control.getTypeId());

		ChongBangParam param = new ChongBangParam(activeTemplateCfg.getOpenParams());
		
		PlayerRankService playerRankService = PlayerRankService.getService(RankType.getRankType(param.rankType));
		if(playerRankService == null) {
			return;
		}
		List<Ranker> rankList = playerRankService.getAll();
		// 发放排名奖励
		List<Mail> mails = new ArrayList<>();
		for (int i = 0; i < rankList.size(); i++) {
			int rank = i + 1;
			Ranker ranker = rankList.get(i);
			ActiveRankRewardsCfg rewardCfg = activeRankRewardsCache.getRankReward(control.getTypeId(), rank);

			Mail mail = mailService.newMail(ranker.getEntityId(), param.mailId, MailArgs.build(), MailArgs.build(rank),
					rewardCfg.getRewards(), GameCause.KFCB_RANK_REWARD);
			mails.add(mail);
		}
		mailService.sendMail(mails, NoticeType.NORMAL);
	}

	@Override
	public void handlePromotionEnd(long playerId, PromotionControl control) {
		// 什么都不用做
	}
	
	public static class ChongBangParam {
		public final int type;
		public final int rankType;
		public final int mailId;
		public ChongBangParam(List<Integer> params) {
			this.type = params.get(0);
			this.rankType = params.get(1);
			this.mailId = params.get(2);
		}
	}

	@Override
	public boolean showLoginRedPoint(long playerId, int typeId) {
		return false;
	}
}
