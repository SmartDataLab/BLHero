package com.xiugou.x1.game.server.module.recruit;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.gaming.fakecmd.annotation.PlayerCmd;
import org.gaming.prefab.exception.Asserts;
import org.gaming.prefab.thing.NoticeType;
import org.gaming.ruler.eventbus.EventBus;
import org.gaming.ruler.util.DropUtil;
import org.gaming.tool.DateTimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.xiugou.x1.design.constant.Common;
import com.xiugou.x1.design.constant.GameCause;
import com.xiugou.x1.design.constant.ItemType;
import com.xiugou.x1.design.constant.TipsCode;
import com.xiugou.x1.design.module.BattleConstCache;
import com.xiugou.x1.design.module.DrawRateCache;
import com.xiugou.x1.design.module.DrawRateCache.DrawRateConfig;
import com.xiugou.x1.design.module.HeroPrizedrawCache;
import com.xiugou.x1.design.module.HeroTypeCache;
import com.xiugou.x1.design.module.HeroTypeCache.HeroTypeConfig;
import com.xiugou.x1.design.module.autogen.HeroPrizedrawAbstractCache.HeroPrizedrawCfg;
import com.xiugou.x1.design.struct.CostThing;
import com.xiugou.x1.design.struct.RewardThing;
import com.xiugou.x1.game.server.foundation.player.PlayerContext;
import com.xiugou.x1.game.server.module.bag.service.ThingService;
import com.xiugou.x1.game.server.module.function.constant.FunctionEnum;
import com.xiugou.x1.game.server.module.function.service.OpenFunctionService;
import com.xiugou.x1.game.server.module.home.constant.BuildingType;
import com.xiugou.x1.game.server.module.home.model.Home;
import com.xiugou.x1.game.server.module.home.service.HomeService;
import com.xiugou.x1.game.server.module.privilegenormal.model.PrivilegeNormal;
import com.xiugou.x1.game.server.module.privilegenormal.service.PrivilegeNormalService;
import com.xiugou.x1.game.server.module.recruit.constant.RefreshType;
import com.xiugou.x1.game.server.module.recruit.event.RecruitNumEvent;
import com.xiugou.x1.game.server.module.recruit.event.RecruitRefreshEvent;
import com.xiugou.x1.game.server.module.recruit.model.Recruit;
import com.xiugou.x1.game.server.module.recruit.service.RecruitService;
import com.xiugou.x1.game.server.module.recruit.struct.RecruitData;
import com.xiugou.x1.game.server.module.server.service.ServerInfoService;

import pb.xiugou.x1.protobuf.recruit.Recruit.PbRecruitSlot;
import pb.xiugou.x1.protobuf.recruit.Recruit.RecruitHeroRequest;
import pb.xiugou.x1.protobuf.recruit.Recruit.RecruitHeroResponse;
import pb.xiugou.x1.protobuf.recruit.Recruit.RecruitInfoRequest;
import pb.xiugou.x1.protobuf.recruit.Recruit.RecruitInfoResponse;
import pb.xiugou.x1.protobuf.recruit.Recruit.RecruitRefreshRequest;
import pb.xiugou.x1.protobuf.recruit.Recruit.RecruitRefreshResponse;

/**
 * @author yh
 * @date 2023/6/20
 * @apiNote
 */
@Controller
public class RecruitHandler {
	@Autowired
	private RecruitService recruitService;
	@Autowired
	private BattleConstCache battleConstCache;
	@Autowired
	private ThingService thingService;
	@Autowired
	private HomeService homeService;
	@Autowired
	private OpenFunctionService openFunctionService;
	@Autowired
	private PrivilegeNormalService privilegeNormalService;
	@Autowired
	private HeroPrizedrawCache heroPrizedrawCache;
	@Autowired
	private DrawRateCache drawRateCache;
	@Autowired
	private ServerInfoService serverInfoService;
	@Autowired
	private HeroTypeCache heroTypeCache;

	@PlayerCmd
	public RecruitInfoResponse info(PlayerContext playerContext, RecruitInfoRequest request) {
		Recruit recruit = recruitService.getEntity(playerContext.getId());
		if (recruit.getLastRefreshTime() == 0
				&& openFunctionService.isFunctionEnabled(playerContext.getId(), FunctionEnum.PUB)) {
			recruit.setLastRefreshTime(DateTimeUtil.currMillis());
			recruitService.update(recruit);
		}
		RecruitInfoResponse.Builder response = RecruitInfoResponse.newBuilder();
		response.setStage(recruit.getStage());
		response.setStageNum(recruit.getStageNum());
		response.setDiscountNum(recruit.getDiscountNum());
		response.setAdFreeNum(recruit.getAdFreeNum());
		response.setMultiple(recruit.getMultiple());
		response.setTotalNum(recruit.getRecruitNum());
		response.setRecruitPoint(recruit.getPoints());
		List<RecruitData> recruitData = recruit.getRecruitData();
		for (RecruitData rec : recruitData) {
			response.addSlots(build(rec));
		}
		response.setRefreshTime(recruit.getLastRefreshTime());
		return response.build();

	}

	/**
	 * 刷新招募列表 1放弃当前召唤，2使用刷新券，3使用钻石，4广告免费刷新 5抽完三个英雄 6系统刷新
	 */
	@PlayerCmd
	public RecruitRefreshResponse refresh(PlayerContext playerContext, RecruitRefreshRequest request) {
		RefreshType refreshType = RefreshType.valueOf(request.getCostType());

		Recruit recruit = recruitService.getEntity(playerContext.getId());

		// 检查是否激活
		checkActive(playerContext.getId());

		List<RecruitData> recruitDataList = recruit.getRecruitData();
		List<RecruitData> recruitPool = recruitDataList.stream().filter(r -> !r.isTake()).collect(Collectors.toList());

		if (refreshType == RefreshType.GIVE_UP) {
			Asserts.isTrue(recruitPool.size() != Common.RECRUIT_NUM, TipsCode.RECRUIT_REFRESH_WRONG);

		} else if (refreshType == RefreshType.USE_TICKET) {
			Asserts.isTrue(recruitPool.size() == Common.RECRUIT_NUM, TipsCode.RECRUIT_NO_NEED);

			thingService.cost(playerContext.getId(), battleConstCache.getRecruit_ticket(), GameCause.RECRUIT_REFRESH);

		} else if (refreshType == RefreshType.USE_DIAMOND) {
			Asserts.isTrue(recruitPool.size() == Common.RECRUIT_NUM, TipsCode.RECRUIT_NO_NEED);

			thingService.cost(playerContext.getId(), battleConstCache.getRecruit_diamond(), GameCause.RECRUIT_REFRESH);

		} else if (refreshType == RefreshType.ADVERTISEMENT) {
			Asserts.isTrue(recruitPool.size() == Common.RECRUIT_NUM, TipsCode.RECRUIT_NO_NEED);
			Asserts.isTrue(recruit.getAdFreeNum() < battleConstCache.getRecruit_ad_refresh_times(),
					TipsCode.RECRUIT_SEE_AD_EMPTY);

			recruit.setAdFreeNum(recruit.getAdFreeNum() + 1);

		} else if (refreshType == RefreshType.COMPLETED) {
			Asserts.isTrue(recruitPool.size() == 0, TipsCode.RECRUIT_REFRESH_WRONG);

		} else if (refreshType == RefreshType.SYSTEM_REFRESH) {
			boolean flag = (DateTimeUtil.currMillis() - recruit.getLastRefreshTime()) >= battleConstCache
					.getRecruit_reset_time();
			Asserts.isTrue(flag, TipsCode.RECRUIT_REFRESH_TYPE_WRONG);

		} else {
			Asserts.isTrue(false, TipsCode.RECRUIT_REFRESH_WRONG);
		}

		List<RecruitData> refreshHeroList = recruitService.getNewRefreshHeroList(recruit, refreshType);
		recruit.setRecruitData(refreshHeroList);
		recruit.setLastRefreshTime(DateTimeUtil.currMillis());
		recruit.setMultiple(1);
		recruitService.update(recruit);

		// 酒馆刷新事件 排除系统刷新
		EventBus.post(RecruitRefreshEvent.of(playerContext.getId(), refreshType));

		RecruitRefreshResponse.Builder response = RecruitRefreshResponse.newBuilder();
		for (RecruitData rec : refreshHeroList) {
			response.addSlots(build(rec));
		}
		response.setRefreshTime(recruit.getLastRefreshTime());
		response.setAdFreeNum(recruit.getAdFreeNum());
		response.setMultiple(recruit.getMultiple());
		response.setStage(recruit.getStage());
		response.setStageNum(recruit.getStageNum());
		return response.build();
	}

	@PlayerCmd
	public RecruitHeroResponse recruit(PlayerContext playerContext, RecruitHeroRequest request) {
		Recruit recruit = recruitService.getEntity(playerContext.getId());
		if (request.getMultiple() > 1) {
			Asserts.isTrue(recruit.getRecruitNum() >= battleConstCache.getRecruit_multiplicity(),
					TipsCode.RECRUIT_OPEN_MULTIPLE);
		}

		// 检查是否激活
		checkActive(playerContext.getId());

		List<RecruitData> recruitDataList = recruit.getRecruitData();
		List<RecruitData> recruitPool = recruitDataList.stream().filter(r -> !r.isTake()).collect(Collectors.toList());
		Asserts.isTrue(!recruitPool.isEmpty(), TipsCode.RECRUIT_POOL_ISEMPTY);
		// 使用钻石抽英雄
		boolean costDiamond = false;
		if (recruitPool.size() < Common.RECRUIT_NUM) {
			int diamondCostNum = battleConstCache.getRecruit_second() * recruit.getMultiple();
			if (recruitPool.size() == 1) {
				diamondCostNum = battleConstCache.getRecruit_third() * recruit.getMultiple();
			}
			if (recruit.getDiscountNum() == 0) {
				diamondCostNum = (int) (diamondCostNum * (battleConstCache.getRecruit_discount() / 10000.0f));
			}

			PrivilegeNormal entity = privilegeNormalService.getEntity(playerContext.getId());
			if (entity.isRecruitDiscount()) {
				diamondCostNum = (int) (diamondCostNum
						* (battleConstCache.getPrivilege_normal_recruit_discount() / 10000.0f));
			}

			thingService.cost(playerContext.getId(), CostThing.of(ItemType.DIAMOND, diamondCostNum), GameCause.RECRUIT);

			recruit.setDiscountNum(recruit.getDiscountNum() == 0 ? 1 : recruit.getDiscountNum());
			costDiamond = true;
		} else {
			// 属于第一次抽需要用金币
			int multiple = request.getMultiple();
			Asserts.isTrue(multiple >= 1, TipsCode.RECRUIT_INITIAL_MULTIPLE_WRONG);
			int recruitGold = battleConstCache.getRecruit_gold() * multiple;
			thingService.cost(playerContext.getId(), CostThing.of(ItemType.GOLD, recruitGold), GameCause.RECRUIT);

			recruit.setMultiple(multiple);
		}

		// 判断当前阶段，StageNum=10 下次刷新时对应权重会增加
		recruit.setRecruitNum(recruit.getRecruitNum() + 1);
		if (!recruit.isStageUpWeight()) {
			HeroPrizedrawCfg heroPrizedrawCfg = heroPrizedrawCache.getOrThrow(recruit.getStage());
			recruit.setStageNum(recruit.getStageNum() + 1);
			if (recruit.getStageNum() >= heroPrizedrawCfg.getUpStageNum()) {
				recruit.setStageUpWeight(true);
			}
		}
		
		if(recruitPool.size() < 3) {
			for(RecruitData recruitData : recruitPool) {
				if(recruitData.getWeight() > 0) {
					continue;
				}
				HeroTypeConfig heroTypeConfig = heroTypeCache.getOrThrow(recruitData.getIdentity());
				HeroPrizedrawCfg heroPrizedrawCfg = heroPrizedrawCache.getOrThrow(heroTypeConfig.getQuality());
				recruitData.setWeight(heroPrizedrawCfg.getWeight());
			}
		}
		
		// TODO 处理保底

		RecruitData recruitData = DropUtil.randomDrop(recruitPool);
		recruitData.setTake(true);

		List<RewardThing> rewardThing = new ArrayList<>();
		rewardThing.add(RewardThing.of(recruitData.getIdentity(), (long) recruitData.getFragment() * recruit.getMultiple()));
		rewardThing.add(RewardThing.of(ItemType.RECRUIT_POINT.getThingId(), recruitData.getScore() * recruit.getMultiple()));
		thingService.add(playerContext.getId(), rewardThing, GameCause.RECRUIT, NoticeType.SLIENT);

		// 抽一次积分3倍，抽两次积分5倍
		long count = recruitDataList.stream().filter(e -> e.isTake()).count();
		int magnification = 1;
		if (count == 1) {
			magnification = battleConstCache.getRecruit_three_times();
		} else if (count == 2) {
			magnification = battleConstCache.getRecruit_five_times();
		}
		for (RecruitData rec : recruitDataList) {
			if (!rec.isTake()) {
				rec.setMultiple(magnification);
			}
		}
		// 记录奖池机制的次数
		List<DrawRateConfig> cfgList = drawRateCache.getInDrawTypeCollector(1);
		int openDay = serverInfoService.getOpenedDay();
		for (DrawRateConfig cfg : cfgList) {
			if (cfg.getOpenDay() > openDay) {
				continue;
			}
			if (cfg.getDrawMayme() <= 0) {
				continue;
			}
			int drawNum = recruit.getDrawNums().getOrDefault(cfg.getDrawId(), 0);
			recruit.getDrawNums().put(cfg.getDrawId(), drawNum + 1);
		}
		recruitService.update(recruit);

		// 招募次数事件
		EventBus.post(RecruitNumEvent.of(playerContext.getId(), request.getMultiple(), costDiamond));

		// 构建respose 并且修改积分倍率
		RecruitHeroResponse.Builder response = RecruitHeroResponse.newBuilder();
		response.setMultiple(recruit.getMultiple());
		response.setDiscountNum(recruit.getDiscountNum());
		for (RecruitData rec : recruitDataList) {
			response.addSlot(build(rec));
		}
		response.setStage(recruit.getStage());
		response.setStageNum(recruit.getStageNum());
		response.setTotalNum(recruit.getRecruitNum());
		response.setRecruitPoint(recruit.getPoints());
		return response.build();
	}

	private PbRecruitSlot build(RecruitData recruitData) {
		PbRecruitSlot.Builder builder = PbRecruitSlot.newBuilder();
		builder.setIdentity(recruitData.getIdentity());
		builder.setFragment(recruitData.getFragment());
		builder.setScore(recruitData.getScore() * recruitData.getMultiple());
		builder.setTake(recruitData.isTake() ? 2 : 1);
		builder.setMultipleScore(recruitData.isCrit());
		return builder.build();
	}

	private void checkActive(long pid) {
		Home home = homeService.getEntity(pid);
		Asserts.isTrue(home.isBuildingOpen(BuildingType.GOD_EFFIGY), TipsCode.RECRUIT_NOT_ACTIVATE);
	}
}
