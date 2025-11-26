/**
 * 
 */
package com.xiugou.x1.game.server.module.handbook;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.gaming.fakecmd.annotation.PlayerCmd;
import org.gaming.prefab.exception.Asserts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.xiugou.x1.design.constant.GameCause;
import com.xiugou.x1.design.constant.TipsCode;
import com.xiugou.x1.design.module.HandbookCatalogCache;
import com.xiugou.x1.design.module.HandbookIdentityCache;
import com.xiugou.x1.design.module.HandbookLevelCache;
import com.xiugou.x1.design.module.HandbookProcessorCache;
import com.xiugou.x1.design.module.HandbookSuitCache;
import com.xiugou.x1.design.module.autogen.HandbookCatalogAbstractCache.HandbookCatalogCfg;
import com.xiugou.x1.design.module.autogen.HandbookIdentityAbstractCache.HandbookIdentityCfg;
import com.xiugou.x1.design.module.autogen.HandbookLevelAbstractCache.HandbookLevelCfg;
import com.xiugou.x1.design.module.autogen.HandbookProcessorAbstractCache.HandbookProcessorCfg;
import com.xiugou.x1.design.module.autogen.HandbookSuitAbstractCache.HandbookSuitCfg;
import com.xiugou.x1.design.struct.CostThing;
import com.xiugou.x1.game.server.foundation.player.PlayerContext;
import com.xiugou.x1.game.server.module.bag.service.ThingService;
import com.xiugou.x1.game.server.module.handbook.model.Handbook;
import com.xiugou.x1.game.server.module.handbook.service.HandbookService;
import com.xiugou.x1.game.server.module.handbook.struct.BookDetail;
import com.xiugou.x1.game.server.module.hero.model.Hero;
import com.xiugou.x1.game.server.module.hero.service.HeroService;
import com.xiugou.x1.game.server.module.player.AbstractModuleHandler;

import pb.xiugou.x1.protobuf.handbook.Handbook.HandbookIdentityRewardRequest;
import pb.xiugou.x1.protobuf.handbook.Handbook.HandbookIdentityRewardResponse;
import pb.xiugou.x1.protobuf.handbook.Handbook.HandbookInfoResponse;
import pb.xiugou.x1.protobuf.handbook.Handbook.HandbookLevelUpRequest;
import pb.xiugou.x1.protobuf.handbook.Handbook.HandbookLevelUpResponse;
import pb.xiugou.x1.protobuf.handbook.Handbook.HandbookSuitActiveRequest;
import pb.xiugou.x1.protobuf.handbook.Handbook.HandbookSuitActiveResponse;
import pb.xiugou.x1.protobuf.handbook.Handbook.HandbookTakeRewardRequest;
import pb.xiugou.x1.protobuf.handbook.Handbook.HandbookTakeRewardResponse;
import pb.xiugou.x1.protobuf.handbook.Handbook.PbBookDetail;
import pb.xiugou.x1.protobuf.handbook.Handbook.PbHandbook;

/**
 * @author YY
 *
 */
@Controller
public class HandbookHandler extends AbstractModuleHandler {

	@Autowired
	private HandbookService handbookService;
	@Autowired
	private HandbookCatalogCache handbookCatalogCache;
	@Autowired
	private HandbookIdentityCache handbookIdentityCache;
	@Autowired
	private HandbookLevelCache handbookLevelCache;
	@Autowired
	private HandbookProcessorCache handbookProcessorCache;
	@Autowired
	private HeroService heroService;
	@Autowired
	private ThingService thingService;
	@Autowired
	private HandbookSuitCache handbookSuitCache;
	
	@Override
	public InfoPriority infoPriority() {
		return InfoPriority.DETAIL;
	}

	@Override
	public void pushInfo(PlayerContext playerContext) {
		List<Handbook> books = handbookService.getEntities(playerContext.getId());
		
		HandbookInfoResponse.Builder response = HandbookInfoResponse.newBuilder();
		for(Handbook book : books) {
			response.addHandbooks(build(book));
		}
		playerContextManager.push(playerContext.getId(), HandbookInfoResponse.Proto.ID, response.build());
	}
	
	@PlayerCmd
	public HandbookLevelUpResponse levelUp(PlayerContext playerContext, HandbookLevelUpRequest request) {
		Handbook handbook = handbookService.getEntity(playerContext.getId(), request.getBookIdentity());
		Asserts.isTrue(handbook != null, TipsCode.ERROR_PARAM, request.getBookIdentity());
		
		List<HandbookCatalogCfg> cfgs = handbookCatalogCache.getInTypeCollector(handbook.getIdentity());
		Set<Integer> elements = new HashSet<>();
		for(HandbookCatalogCfg cfg : cfgs) {
			List<HandbookIdentityCfg> elementCfgs = handbookIdentityCache.findInCatalogCollector(cfg.getId());
			if(elementCfgs == null) {
				continue;
			}
			for(HandbookIdentityCfg elementCfg : elementCfgs) {
				elements.add(elementCfg.getId());
			}
		}
		Asserts.isTrue(elements.contains(request.getEleIdentity()), TipsCode.HANDBOOK_NO_ELEMENT);
		BookDetail bookDetail = handbook.getBookDetails().get(request.getEleIdentity());
		if(bookDetail == null) {
			bookDetail = new BookDetail();
			bookDetail.setIdentity(request.getEleIdentity());
			handbook.getBookDetails().put(bookDetail.getIdentity(), bookDetail);
		}
		HandbookIdentityCfg elementCfg = handbookIdentityCache.getOrThrow(bookDetail.getIdentity());
		HandbookLevelCfg levelCfg = handbookLevelCache.findInQualityUpTypeLevelIndex(elementCfg.getQuality(), elementCfg.getUpType(), bookDetail.getLevel() + 1);
		Asserts.isTrue(levelCfg != null, TipsCode.HANDBOOK_MAX_LV);
		
		if(handbook.getIdentity() == 1) {
			//英雄图鉴
			if(levelCfg.getUpType() == 1) {
				Hero hero = heroService.getEntity(playerContext.getId(), request.getEleIdentity());
				Asserts.isTrue(hero != null, TipsCode.HERO_NOT_EXIST);
				Asserts.isTrue(hero.getLevel() >= levelCfg.getNeedLevel(), TipsCode.HANDBOOK_LEVEL_UP_UNREACH);
			} else {
				CostThing costThing = CostThing.of(elementCfg.getId(), levelCfg.getNeedCost());
				thingService.cost(playerContext.getId(), costThing, GameCause.HANDBOOK_LEVEL_UP);
			}
			bookDetail.setLevel(bookDetail.getLevel() + 1);
			handbookService.update(handbook);
		} else if(handbook.getIdentity() == 2) {
			//怪物图鉴
			CostThing costThing = CostThing.of(elementCfg.getId(), levelCfg.getNeedCost());
			thingService.cost(playerContext.getId(), costThing, GameCause.HANDBOOK_LEVEL_UP);
			bookDetail.setLevel(bookDetail.getLevel() + 1);
			handbookService.update(handbook);
			
		} else if(handbook.getIdentity() == 3) {
			//TODO
		} else if(handbook.getIdentity() == 4) {
			//TODO
		}
		
		heroService.calculateAllHeroAttr(playerContext.getId(), GameCause.HANDBOOK_LEVEL_UP);
		
		HandbookLevelUpResponse.Builder response = HandbookLevelUpResponse.newBuilder();
		response.addHandbooks(build(handbook));
		return response.build();
	}
	
	@PlayerCmd
	public HandbookTakeRewardResponse takeReward(PlayerContext playerContext, HandbookTakeRewardRequest request) {
		//只有总览里面有进度积分奖励
		Handbook handbook = handbookService.getEntity(playerContext.getId(), 0);
		int rewardLv = handbook.getRewardLv() + 1;
		HandbookProcessorCfg processorCfg  = handbookProcessorCache.getOrThrow(rewardLv);
		Asserts.isTrue(processorCfg != null, TipsCode.HANDBOOK_MAX_PROCESSOR);
		Asserts.isTrue(handbook.getPoint() >= processorCfg.getNeedPoint(), TipsCode.HANDBOOK_POINT_LACK);
		
		handbook.setRewardLv(handbook.getRewardLv() + 1);
		handbookService.update(handbook);
		
		thingService.add(playerContext.getId(), processorCfg.getRewards(), GameCause.HANDBOOK_REWARD);
		
		HandbookTakeRewardResponse.Builder response = HandbookTakeRewardResponse.newBuilder();
		response.setBookIdentity(handbook.getIdentity());
		response.setRewardLv(handbook.getRewardLv());
		return response.build();
	}
	
	@PlayerCmd
	public HandbookIdentityRewardResponse identityReward(PlayerContext playerContext, HandbookIdentityRewardRequest request) {
		Handbook handbook = handbookService.getEntity(playerContext.getId(), request.getBookIdentity());
		Asserts.isTrue(handbook != null, TipsCode.ERROR_PARAM, request.getBookIdentity());
		
		List<HandbookCatalogCfg> cfgs = handbookCatalogCache.getInTypeCollector(handbook.getIdentity());
		Set<Integer> elements = new HashSet<>();
		for(HandbookCatalogCfg cfg : cfgs) {
			List<HandbookIdentityCfg> elementCfgs = handbookIdentityCache.findInCatalogCollector(cfg.getId());
			if(elementCfgs == null) {
				continue;
			}
			for(HandbookIdentityCfg elementCfg : elementCfgs) {
				elements.add(elementCfg.getId());
			}
		}
		Asserts.isTrue(elements.contains(request.getEleIdentity()), TipsCode.HANDBOOK_NO_ELEMENT);
		BookDetail bookDetail = handbook.getBookDetails().get(request.getEleIdentity());
		Asserts.isTrue(bookDetail != null, TipsCode.HANDBOOK_REWARD_LIMIT);
		Asserts.isTrue(bookDetail.getRewardLv() < bookDetail.getLevel(), TipsCode.HANDBOOK_REWARD_TOOK);
		
		HandbookIdentityCfg elementCfg = handbookIdentityCache.getOrThrow(bookDetail.getIdentity());
		HandbookLevelCfg levelCfg = handbookLevelCache.findInQualityUpTypeLevelIndex(elementCfg.getQuality(), elementCfg.getUpType(), bookDetail.getRewardLv() + 1);
		Asserts.isTrue(levelCfg != null, TipsCode.HANDBOOK_MAX_LV);
		
		bookDetail.setRewardLv(bookDetail.getRewardLv() + 1);
		handbookService.update(handbook);
		
		Handbook handbook0 = handbookService.getEntity(playerContext.getId(), 0);
		handbook0.setPoint(handbook0.getPoint() + levelCfg.getPoint());
		handbookService.update(handbook0);
		
		HandbookIdentityRewardResponse.Builder response = HandbookIdentityRewardResponse.newBuilder();
		response.setBookIdentity(handbook.getIdentity());
		response.setEleIdentity(bookDetail.getIdentity());
		response.setRewardLv(bookDetail.getRewardLv());
		response.setPoint(handbook0.getPoint());
		return response.build();
	}
	
	@PlayerCmd
	public HandbookSuitActiveResponse activeSuit(PlayerContext playerContext, HandbookSuitActiveRequest request) {
		HandbookSuitCfg suitCfg = handbookSuitCache.getOrThrow(request.getSuitId());
		
		Handbook handbook0 = handbookService.getEntity(playerContext.getId(), 0);
		Asserts.isTrue(!handbook0.getSuits().contains(suitCfg.getId()), TipsCode.HANDBOOK_SUIT_ACTIVED);
		
		List<HandbookIdentityCfg> elementCfgs = handbookIdentityCache.getInSuitCollector(suitCfg.getId());
		boolean active = true;
		for(HandbookIdentityCfg elementCfg : elementCfgs) {
			HandbookCatalogCfg catalogCfg = handbookCatalogCache.getOrThrow(elementCfg.getCatalog());
			
			Handbook handbook = handbookService.getEntity(playerContext.getId(), catalogCfg.getType());
			BookDetail bookDetail = handbook.getBookDetails().get(elementCfg.getId());
			if(bookDetail == null || bookDetail.getLevel() <= 0) {
				active = false;
				break;
			}
		}
		Asserts.isTrue(active, TipsCode.HANDBOOK_SUIT_LACK);
		
		handbook0.getSuits().add(suitCfg.getId());
		handbookService.update(handbook0);
		
		heroService.calculateAllHeroAttr(playerContext.getId(), GameCause.HANDBOOK_SUIT);
		
		HandbookSuitActiveResponse.Builder response = HandbookSuitActiveResponse.newBuilder();
		response.setSuitId(suitCfg.getId());
		return response.build();
	}

	public PbHandbook build(Handbook book) {
		PbHandbook.Builder builder = PbHandbook.newBuilder();
		builder.setIdentity(book.getIdentity());
		builder.setPoint(book.getPoint());
		builder.setRewardLv(book.getRewardLv());
		for(BookDetail bookDetail : book.getBookDetails().values()) {
			PbBookDetail.Builder pbDetail = PbBookDetail.newBuilder();
			pbDetail.setIdentity(bookDetail.getIdentity());
			pbDetail.setLevel(bookDetail.getLevel());
			pbDetail.setRewardLv(bookDetail.getRewardLv());
			builder.addDetails(pbDetail.build());
		}
		builder.addAllSuits(book.getSuits());
		return builder.build();
	}
}
