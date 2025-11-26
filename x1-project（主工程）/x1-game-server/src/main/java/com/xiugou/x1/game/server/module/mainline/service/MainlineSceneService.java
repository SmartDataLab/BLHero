/**
 * 
 */
package com.xiugou.x1.game.server.module.mainline.service;

import org.gaming.prefab.exception.Asserts;
import org.gaming.ruler.eventbus.Subscribe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xiugou.x1.design.constant.TipsCode;
import com.xiugou.x1.design.module.MainlineSceneCache;
import com.xiugou.x1.game.server.foundation.service.OneToManyService;
import com.xiugou.x1.game.server.module.mainline.model.MainlinePlayer;
import com.xiugou.x1.game.server.module.mainline.model.MainlineScene;
import com.xiugou.x1.game.server.module.mainline.struct.SceneOpening;
import com.xiugou.x1.game.server.module.ministruct.PbHelper;
import com.xiugou.x1.game.server.module.player.event.PlayerCreateEvent;
import com.xiugou.x1.game.server.module.player.event.PlayerLoginEvent;

import pb.xiugou.x1.protobuf.mainline.Mainline.PbMainlineScene;

/**
 * @author YY
 *
 */
@Service
public class MainlineSceneService extends OneToManyService<MainlineScene> {

	@Autowired
	private MainlineSceneCache mainlineSceneCache;
	@Autowired
	private MainlinePlayerService mainlinePlayerService;
	
	public final MainlineScene getOrThrow(long playerId, int sceneIdentity) {
		MainlineScene entity = repository().getByKeys(playerId, sceneIdentity);
		Asserts.isTrue(entity != null, TipsCode.MAINLINE_LOCK, sceneIdentity);
		return entity;
	}
	
	public final MainlineScene getOrNull(long playerId, int sceneIdentity) {
		return repository().getByKeys(playerId, sceneIdentity);
	}
	
	
	@Subscribe
	private void listen(PlayerCreateEvent event) {
		MainlineScene scene = newScene(event.getPlayer().getId(), mainlineSceneCache.all().get(0).getId());
		
		MainlinePlayer entity = mainlinePlayerService.getEntity(event.getPlayer().getId());
		entity.setCurrScene(scene.getIdentity());
		mainlinePlayerService.update(entity);
	}
	
	@Subscribe
	private void listen(PlayerLoginEvent event) {
		MainlineScene scene = repository().getByKeys(event.getEntityId(), mainlineSceneCache.all().get(0).getId());
		if(scene != null) {
			return;
		}
		scene = newScene(event.getPlayer().getId(), mainlineSceneCache.all().get(0).getId());
	}
	
	public MainlineScene newScene(long playerId, int mainlineId) {
		MainlineScene entity = this.getOrNull(playerId, mainlineId);
		if(entity != null) {
			return entity;
		}
		entity = new MainlineScene();
		entity.setPid(playerId);
		entity.setIdentity(mainlineId);
		entity.setMaxStage(1);
		entity.setCurrStage(1);
		repository().insert(entity);
		
		return entity;
	}
	
	public static PbMainlineScene build(MainlineScene mainlineScene) {
        PbMainlineScene.Builder builder = PbMainlineScene.newBuilder();
        builder.setSceneId(mainlineScene.getIdentity());
        builder.setMaxStage(mainlineScene.getMaxStage());
        builder.setCurrStage(mainlineScene.getCurrStage());
        builder.addAllFirstStages(mainlineScene.getFirstStages());
        builder.addAllFogs(mainlineScene.getFogs());
        builder.addAllTeleports(mainlineScene.getTeleports());
        builder.addAllNpcs(mainlineScene.getNpcs());
        for (SceneOpening sceneOpening : mainlineScene.getOpeningTeleports().values()) {
            builder.addOpeningTeleports(PbHelper.build(sceneOpening));
        }
        for (SceneOpening sceneOpening : mainlineScene.getOpeningFogs().values()) {
            builder.addOpeningFogs(PbHelper.build(sceneOpening));
        }
        for (SceneOpening sceneOpening : mainlineScene.getOpeningNpcs().values()) {
            builder.addOpeningNpcs(PbHelper.build(sceneOpening));
        }
        builder.setPortalX(mainlineScene.getPortalX());
        builder.setPortalY(mainlineScene.getPortalY());
        builder.addAllDungeons(mainlineScene.getDungeons());
        builder.setOpenNext(mainlineScene.isOpenNext());
        return builder.build();
    }
}
