/**
 * 
 */
package com.xiugou.x1.game.server.module.promotion.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.gaming.prefab.exception.Asserts;
import org.gaming.prefab.promotion.PromotionStage;
import org.gaming.ruler.eventbus.Subscribe;
import org.gaming.ruler.lifecycle.Lifecycle;
import org.gaming.tool.ListMapUtil;
import org.gaming.tool.LocalDateTimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xiugou.x1.design.constant.TipsCode;
import com.xiugou.x1.design.module.ActiveTemplateCache;
import com.xiugou.x1.design.module.autogen.ActiveTemplateAbstractCache.ActiveTemplateCfg;
import com.xiugou.x1.game.server.foundation.player.PlayerContext;
import com.xiugou.x1.game.server.foundation.player.PlayerContextManager;
import com.xiugou.x1.game.server.foundation.service.OneToManyService;
import com.xiugou.x1.game.server.foundation.starting.ApplicationSettings;
import com.xiugou.x1.game.server.module.promotion.constant.PromotionLogicType;
import com.xiugou.x1.game.server.module.promotion.message.PromotionEndMessage;
import com.xiugou.x1.game.server.module.promotion.model.PromotionControl;
import com.xiugou.x1.game.server.module.server.event.ServerOpenEvent;
import com.xiugou.x1.game.server.module.server.model.ServerInfo;
import com.xiugou.x1.game.server.module.server.service.ServerInfoService;

/**
 * @author YY
 *
 */
@Service
public class PromotionControlService extends OneToManyService<PromotionControl> implements Lifecycle {

	@Autowired
	private PlayerContextManager playerContextManager;
	@Autowired
	private ActiveTemplateCache activeTemplateCache;
	@Autowired
	private ApplicationSettings applicationSettings;
	@Autowired
	private ServerInfoService serverInfoService;
	
	private static Logger logger = LoggerFactory.getLogger(PromotionControlService.class);
	
	private static ConcurrentMap<Integer, PromotionLogicService> serviceMap = new ConcurrentHashMap<>();
	protected static void register(PromotionLogicService logicService) {
		serviceMap.put(logicService.promotionLogicType().getValue(), logicService);
	}
	public static PromotionLogicService getLogicService(PromotionLogicType logicType) {
		return serviceMap.get(logicType.getValue());
	}
	public static Collection<PromotionLogicService> allLogicServices() {
		return serviceMap.values();
	}
	
	public List<PromotionControl> allControlsInServer(int serverId) {
		return this.getEntities(serverId);
	}
	
	public PromotionControl getControl(int serverId, int typeId) {
		return repository().getByKeys(serverId, typeId);
	}
	
	private List<PromotionControl> allPromotions() {
		return this.repository().getAllInCache();
	}
	
	protected void runInSchedule() {
		List<PromotionControl> allPromotions = allPromotions();
		
		LocalDateTime now = LocalDateTimeUtil.now();
		for(PromotionControl control : allPromotions) {
			PromotionLogicService service = serviceMap.get(control.getLogicType());
			if(service == null) {
				continue;
			}
			if(control.isTerminate()) {
				return;
			}
			try {
				checkStart(control, now, service);
				checkStill(control, now, service);
				checkEnd(control, now, service);
				checkNext(control, now, service);
				checkStop(control, now, service);
			} catch (Exception e) {
				logger.error("活动" + control.getName() + "处理异常", e);
			}
		}
	}
	
	
	private void checkStart(PromotionControl control, LocalDateTime now, PromotionLogicService service) {
		if(control.getStage() != PromotionStage.IDLE) {
			return;
		}
		if(now.isBefore(control.getStartTime())) {
			return;
		}
		try {
			ActiveTemplateCfg activeTemplateCfg = activeTemplateCache.getOrThrow(control.getTypeId());
			if(activeTemplateCfg.getStatus() == 1) {
				control.setStage(PromotionStage.RUNNING);
				logger.info(String.format("活动%s-%s执行whenStart", control.getTypeId(), control.getName()));
				service.whenStart(control);
			} else {
				control.setStage(PromotionStage.STOP);
				logger.info(String.format("活动%s-%s执行已暂停本轮", control.getTypeId(), control.getName()));
			}
		} catch (Exception e) {
			logger.error(String.format("活动%s-%s执行whenStart中发生异常", control.getTypeId(), control.getName()), e);
		}
		this.update(control);
	}
	
	private void checkStill(PromotionControl control, LocalDateTime now, PromotionLogicService service) {
		if (control.getStage() != PromotionStage.RUNNING) {
			return;
		}
		if (now.isBefore(control.getStillTime())) {
			return;
		}
		try {
			control.setStage(PromotionStage.STILL);
			logger.info(String.format("活动%s-%s执行whenStill逻辑", control.getTypeId(), control.getName()));
			service.whenStill(control);
		} catch (Exception e) {
			logger.error(String.format("活动%s-%s执行whenStill中发生异常", control.getTypeId(), control.getName()), e);
		} finally {
			this.update(control);
		}
	}
	
	private void checkEnd(PromotionControl control, LocalDateTime now, PromotionLogicService service) {
		if (control.getStage() != PromotionStage.STILL) {
			return;
		}
		if (now.isBefore(control.getEndTime())) {
			return;
		}
		try {
			control.setStage(PromotionStage.END);
			control.setSettleTurns(control.getTurns());
			control.setTurns(control.getTurns() + 1);
			logger.info(String.format("活动%s-%s执行whenEnd逻辑", control.getTypeId(), control.getName()));
			service.whenEnd(control);

			// 通知在线玩家进行结算处理
			for (PlayerContext playerContext : playerContextManager.onlines()) {
				if (playerContext.getServerId() != control.getServerId()) {
					continue;
				}
				playerContext.tell(PromotionEndMessage.of(playerContext.getId(), control));
			}
		} catch (Exception e) {
			logger.error(String.format("活动%s-%s执行whenEnd中发生异常", control.getTypeId(), control.getName()), e);
		} finally {
			this.update(control);
		}
	}
	
	private void checkNext(PromotionControl control, LocalDateTime now, PromotionLogicService service) {
		if (control.getStage() != PromotionStage.END) {
			return;
		}
		if (now.isBefore(control.getNextTime())) {
			return;
		}
		try {
			control.setStage(PromotionStage.IDLE);
			logger.info(String.format("活动%s-%s执行whenNext逻辑", control.getTypeId(), control.getName()));
			service.whenNext(control);
		} catch (Exception e) {
			logger.error(String.format("活动%s-%s执行whenNext中发生异常", control.getTypeId(), control.getName()), e);
		} finally {
			this.update(control);
		}
	}
	
	private void checkStop(PromotionControl control, LocalDateTime now, PromotionLogicService service) {
		if (control.getStage() != PromotionStage.STOP) {
			return;
		}
		if (now.isBefore(control.getNextTime())) {
			return;
		}
		try {
			control.setStage(PromotionStage.IDLE);
			logger.info(String.format("活动%s-%s执行从STOP状态执行whenNext逻辑", control.getTypeId(), control.getName()));
			service.whenNext(control);
		} catch (Exception e) {
			logger.error(String.format("活动%s-%s执行从STOP状态执行whenNext中发生异常", control.getTypeId(), control.getName()), e);
		} finally {
			this.update(control);
		}
	}
	
	@Override
	public void start() throws Exception {
		initPromotions();
	}
	
	/**
	 * 断言活动是否进行中，开始时间至沉寂时间之间为进行中
	 * @param serverId
	 * @param typeId
	 */
	public void assertRunning(int serverId, int typeId) {
		Asserts.isTrue(isRunning(serverId, typeId), TipsCode.PROMOTION_NOT_RUNNING, typeId);
	}
	/**
	 * 判断活动是否进行中，开始时间至沉寂时间之间为进行中
	 * @param serverId
	 * @param typeId
	 * @return
	 */
	public boolean isRunning(int serverId, int typeId) {
		PromotionControl control = this.getControl(serverId, typeId);
		if(control == null) {
			return false;
		}
		LocalDateTime now = LocalDateTimeUtil.now();
		return now.isAfter(control.getStartTime()) && now.isBefore(control.getStillTime()) && control.isRunning();
	}
	/**
	 * 断言活动是否开启中，开始时间至结束时间之间为开启中
	 * @param serverId
	 * @param typeId
	 */
	public void assertOpening(int serverId, int typeId) {
		Asserts.isTrue(isOpening(serverId, typeId), TipsCode.PROMOTION_NOT_OPENING, typeId);
	}
	/**
	 * 判断活动是否开启中，开始时间至结束时间之间为开启中
	 * @param serverId
	 * @param typeId
	 * @return
	 */
	public boolean isOpening(int serverId, int typeId) {
		PromotionControl control = this.getControl(serverId, typeId);
		if(control == null) {
			return false;
		}
		LocalDateTime now = LocalDateTimeUtil.now();
		return now.isAfter(control.getStartTime()) && now.isBefore(control.getEndTime());
	}
	
	/**
	 * 开服事件
	 * @param event
	 */
	@Subscribe
	private void listen(ServerOpenEvent event) {
		initPromotions();
	}
	
	private void initPromotions() {
		ServerInfo serverInfo = serverInfoService.getMain();
		if(!serverInfo.isOpened()) {
			return;
		}
		
		List<PromotionControl> allPromotions = allPromotions();
		Map<Integer, Map<Integer, PromotionControl>> serverTypeControls = ListMapUtil.fillTwoLayerMap(allPromotions, PromotionControl::getServerId, PromotionControl::getTypeId);
	
		List<PromotionControl> insertList = new ArrayList<>();
		for(int serverId : applicationSettings.getGameServerIds()) {
			Map<Integer, PromotionControl> typeMap = serverTypeControls.getOrDefault(serverId, Collections.emptyMap());
			for(ActiveTemplateCfg activeTemplateCfg : activeTemplateCache.all()) {
				PromotionControl control = typeMap.get(activeTemplateCfg.getId());
				if(control != null) {
					continue;
				}
				PromotionLogicService logicService = serviceMap.get(activeTemplateCfg.getLogicType());
				if(logicService == null) {
					logger.error("未找到逻辑类型为{}的活动实现", activeTemplateCfg.getLogicType());
					continue;
				}
				control = logicService.whenInit(activeTemplateCfg);
				insertList.add(control);
			}
		}
		this.repository().insertAll(insertList);
	}
}
