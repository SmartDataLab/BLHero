package com.xiugou.x1.game.server.module.recruit.service;

import static com.xiugou.x1.design.constant.Common.WEIGHTBASE;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.gaming.ruler.util.DropUtil;
import org.gaming.tool.RandomUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xiugou.x1.design.module.BattleConstCache;
import com.xiugou.x1.design.module.DrawRateCache;
import com.xiugou.x1.design.module.DrawRateCache.DrawRateConfig;
import com.xiugou.x1.design.module.HeroPrizedrawCache;
import com.xiugou.x1.design.module.HeroRecruitSpecialCache;
import com.xiugou.x1.design.module.HeroTypeCache;
import com.xiugou.x1.design.module.HeroTypeCache.HeroTypeConfig;
import com.xiugou.x1.design.module.autogen.HeroPrizedrawAbstractCache.HeroPrizedrawCfg;
import com.xiugou.x1.design.module.autogen.HeroRecruitSpecialAbstractCache.HeroRecruitSpecialCfg;
import com.xiugou.x1.design.module.autogen.HeroTypeAbstractCache.HeroTypeCfg;
import com.xiugou.x1.design.struct.Keyv;
import com.xiugou.x1.game.server.foundation.service.PlayerOneToOneResetableService;
import com.xiugou.x1.game.server.module.hero.model.Hero;
import com.xiugou.x1.game.server.module.hero.service.HeroService;
import com.xiugou.x1.game.server.module.recruit.constant.RefreshType;
import com.xiugou.x1.game.server.module.recruit.model.Recruit;
import com.xiugou.x1.game.server.module.recruit.struct.RecruitData;
import com.xiugou.x1.game.server.module.server.service.ServerInfoService;

/**
 * @author yh
 * @date 2023/6/19
 * @apiNote
 */
@Service
public class RecruitService extends PlayerOneToOneResetableService<Recruit> {
	@Autowired
	private HeroTypeCache heroTypeCache;
	@Autowired
	private BattleConstCache battleConstCache;
	@Autowired
	private HeroPrizedrawCache heroPrizedrawCache;
	@Autowired
	private DrawRateCache drawRateCache;
	@Autowired
	private ServerInfoService serverInfoService;
	@Autowired
	private HeroService heroService;
	@Autowired
	private HeroRecruitSpecialCache heroRecruitSpecialCache;

	@Override
	protected Recruit createWhenNull(long entityId) {
		Recruit recruit = new Recruit();
		recruit.setPid(entityId);
		recruit.setRecruitData(firstSystemRefresh(recruit));
		recruit.setStage(getNextStage(0));
		return recruit;
	}

	@Override
	protected void doDailyReset(Recruit entity) {
		entity.setDiscountNum(0);
		entity.setTodayRecruit(0);
	}

	private int getNextStage(int currStage) {
		HeroPrizedrawCfg nextCfg = heroPrizedrawCache.getOrNull(currStage + 1);
		if (nextCfg != null && nextCfg.getUpStageNum() > 0) {
			return nextCfg.getQuality();
		}
		for (HeroPrizedrawCfg cfg : heroPrizedrawCache.all()) {
			if (cfg.getUpStageNum() <= 0) {
				continue;
			}
			// 刚创号，使用首个有次数要求的必出阶段
			return cfg.getQuality();
		}
		return 0;
	}

	/**
	 * 系统首次刷新
	 * 
	 * @param recruit
	 * @return
	 */
	private List<RecruitData> firstSystemRefresh(Recruit recruit) {
		List<RecruitData> result = new ArrayList<>();
		HeroRecruitSpecialCfg heroRecruitSpecialCfg = heroRecruitSpecialCache.getOrThrow(0);
		for (Keyv keyv : heroRecruitSpecialCfg.getHeroPool()) {
			result.add(build(keyv.getKey(), true, keyv.getValue()));
		}
		return result;
	}

	/**
	 * 根据每日招募次数获取到随机抽奖池配置
	 * 
	 * @param todayRecruit
	 * @return
	 */
	private List<DrawRateConfig> getDrawRateConfig(int todayRecruit) {
		List<DrawRateConfig> cfgList = drawRateCache.getInDrawTypeCollector(1);

		List<DrawRateConfig> result = new ArrayList<>();
		for (DrawRateConfig config : cfgList) {
			if ((config.getDrawNumMin() == 0 || config.getDrawNumMin() <= todayRecruit)
					&& (config.getDrawNumMax() == 0 || config.getDrawNumMax() >= todayRecruit)) {
				result.add(config);
			}
		}
		return cfgList;
	}

	public List<RecruitData> getNewRefreshHeroList(Recruit recruit, RefreshType refreshType) {
		if(refreshType.isCountHand()) {
			recruit.setHandRefresh(recruit.getHandRefresh() + 1);
			//手刷特殊奖池
			HeroRecruitSpecialCfg specialCfg = heroRecruitSpecialCache.getOrNull(recruit.getHandRefresh());
			if(specialCfg != null) {
				List<RecruitData> recruitDatas = new ArrayList<>();
				for(Keyv keyv : specialCfg.getHeroPool()) {
					recruitDatas.add(build(keyv.getKey(), true, keyv.getValue()));
				}
				return recruitDatas;
			}
		}
		
		List<DrawRateConfig> cfgList = this.getDrawRateConfig(recruit.getTodayRecruit());
		// 结果列表
		List<DrawRateConfig> resultList = new ArrayList<>();
		// 已经随机出的奖励组
		Set<Integer> outGroups = new HashSet<>();
		
		int openDay = serverInfoService.getOpenedDay();
		
		//处理手动刷新各阶段的特殊触发
		if (refreshType.isCountHand() && recruit.isStageUpWeight()) {
			// 手动刷新时，如果当前满足招募10次后出现指定品质的情况，则过滤出指定品质的奖池
			List<DrawRateConfig> specialRandomList = new ArrayList<>();
			for (DrawRateConfig cfg : cfgList) {
				if (cfg.getOpenDay() > openDay) {
					continue;
				}
				// 判断玩家是否已经拥有该卡
				if (cfg.getPrecondition() == 1) {
					Hero hero = heroService.getEntity(recruit.getPid(), cfg.getDrawId());
					if (hero == null) {
						continue;
					}
				}
				HeroTypeCfg heroTypeCfg = heroTypeCache.getOrThrow(cfg.getDrawId());
				if (heroTypeCfg.getQuality() == recruit.getStage()) {
					specialRandomList.add(cfg);
				}
			}
			if (!specialRandomList.isEmpty()) {
				DrawRateConfig cfg = DropUtil.randomDrop(specialRandomList);
				resultList.add(cfg);
				outGroups.add(cfg.getGroup());
				
				//重置相同分组的抽奖次数
				resetSameGroupDrawNum(recruit, cfg, cfgList);
			}
			recruit.setStageUpWeight(false);
			// 提升至下一阶段
			recruit.setStage(getNextStage(recruit.getStage()));
			recruit.setStageNum(0);
		}

		// 随机奖项
		List<DrawRateConfig> randomList = new ArrayList<>();
		// 必中奖项
		List<DrawRateConfig> mustList = new ArrayList<>();
		while (resultList.size() < 3) {
			randomList.clear();
			mustList.clear();
			for (DrawRateConfig cfg : cfgList) {
				if (cfg.getOpenDay() > openDay) {
					continue;
				}
				if(outGroups.contains(cfg.getGroup())) {
					continue;
				}
				// 判断玩家是否已经拥有该卡
				if (cfg.getPrecondition() == 1) {
					Hero hero = heroService.getEntity(recruit.getPid(), cfg.getDrawId());
					if (hero == null) {
						continue;
					}
				}
				if (cfg.getDrawMayme() <= 0) {
					randomList.add(cfg);
				} else {
					int drawNum = recruit.getDrawNums().getOrDefault(cfg.getDrawId(), 0);
					if (drawNum >= cfg.getDrawMayme()) {
						if (cfg.getDrawMust() > 0 && drawNum >= cfg.getDrawMust()) {
							mustList.add(cfg);
						} else {
							randomList.add(cfg);
						}
					}
				}
			}
			DrawRateConfig drawedCfg = null;
			if (!mustList.isEmpty()) {
				drawedCfg = DropUtil.randomDrop(mustList);
			} else {
				drawedCfg = DropUtil.randomDrop(randomList);
			}
			resultList.add(drawedCfg);
			outGroups.add(drawedCfg.getGroup());
			//重置相同分组的抽奖次数
			resetSameGroupDrawNum(recruit, drawedCfg, cfgList);
		}

		List<RecruitData> recruitDatas = new ArrayList<>();
		for (DrawRateConfig data : resultList) {
			recruitDatas.add(build(data.getDrawId(), false, 0));
		}
		return recruitDatas;
	}
	
	private void resetSameGroupDrawNum(Recruit recruit, DrawRateConfig cfg, List<DrawRateConfig> cfgList) {
		if (cfg.getDrawMayme() <= 0 && cfg.getDrawMust() <= 0) {
			return;
		}
		//同组的抽奖计数全部重置
		for(DrawRateConfig sameGroupCfg : cfgList) {
			if(sameGroupCfg.getGroup() == cfg.getGroup()) {
				recruit.getDrawNums().put(sameGroupCfg.getDrawId(), 0);
			}
		}
	}
	
	private RecruitData build(int heroIdentity, boolean useSpecialWeight, int drawWeight) {
		RecruitData recruitData = new RecruitData();
		recruitData.setIdentity(heroIdentity);
		recruitData.setTake(false);
		recruitData.setMultiple(1);

		HeroTypeConfig heroTypeConfig = heroTypeCache.getOrThrow(heroIdentity);
		// 随碎片
		HeroPrizedrawCfg heroPrizedrawCfg = heroPrizedrawCache.getOrThrow(heroTypeConfig.getQuality());
		List<Integer> fragment = heroPrizedrawCfg.getFragment();
		recruitData.setFragment(RandomUtil.closeClose(fragment.get(0), fragment.get(1)));

		// 随积分
		long points = heroPrizedrawCfg.getPoints();
		boolean flag = RandomUtil.closeClose(1, WEIGHTBASE) <= battleConstCache.getRecruit_points_chance();
		// 命中概率则加3倍积分
		recruitData.setScore(points + (flag ? points * 3 : 0));
		recruitData.setCrit(flag);
		if(useSpecialWeight) {
			recruitData.setWeight(drawWeight);
		} else {
			recruitData.setWeight(heroPrizedrawCfg.getWeight());
		}
		return recruitData;
	}
}
