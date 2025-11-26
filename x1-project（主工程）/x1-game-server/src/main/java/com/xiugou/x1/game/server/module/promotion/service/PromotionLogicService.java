/**
 * 
 */
package com.xiugou.x1.game.server.module.promotion.service;

import java.time.LocalDateTime;

import org.gaming.tool.LocalDateTimeUtil;
import org.springframework.beans.factory.annotation.Autowired;

import com.xiugou.x1.design.module.ActiveTemplateCache;
import com.xiugou.x1.design.module.autogen.ActiveTemplateAbstractCache.ActiveTemplateCfg;
import com.xiugou.x1.game.server.TimeSetting;
import com.xiugou.x1.game.server.foundation.starting.ApplicationSettings;
import com.xiugou.x1.game.server.module.promotion.constant.PromotionLogicType;
import com.xiugou.x1.game.server.module.promotion.model.PromotionControl;
import com.xiugou.x1.game.server.module.server.model.ServerInfo;
import com.xiugou.x1.game.server.module.server.service.ServerInfoService;

/**
 * @author YY
 * 活动循环时间线：开始时间->沉寂时间->结束时间->空闲时间->开始时间->...
 * 
 * 当没有具体的沉寂阶段时，沉寂时间=结束时间
 * 当没有具体的空闲阶段时，空闲时间=结束时间
 */
public abstract class PromotionLogicService {
	
	@Autowired
	protected PromotionControlService promotionControlService;
	@Autowired
	protected ApplicationSettings applicationSettings;
	@Autowired
	protected ServerInfoService serverInfoService;
	@Autowired
	protected TimeSetting timeSetting;
	@Autowired
	protected ActiveTemplateCache activeTemplateCache;
	
	public PromotionLogicService() {
		PromotionControlService.register(this);
	}
	
	public abstract PromotionLogicType promotionLogicType();
	
	public PromotionControl whenInit(ActiveTemplateCfg activeTemplateCfg) {
		ServerInfo serverInfo = serverInfoService.getMain();

		LocalDateTime openOTime = timeSetting.theDayOTime(serverInfo.getOpenTime());

		PromotionControl control = new PromotionControl();
		control.setLogicType(promotionLogicType().getValue());
		control.setTypeId(activeTemplateCfg.getId());
		control.setName(activeTemplateCfg.getName());
		control.setServerId(applicationSettings.getGameServerId());

		if (activeTemplateCfg.getOpType() == 1) {
			//开服活动
			control.setConfigId(0);
			control.setTurns(1);
			control.setStartTime(openOTime.plusDays(activeTemplateCfg.getOpenDay() - 1));
			control.setStillTime(control.getStartTime().plusDays(activeTemplateCfg.getOpenDuration()));
			control.setEndTime(control.getStillTime());
			control.setNextTime(control.getStillTime());
		} else if (activeTemplateCfg.getOpType() == 2) {
			//循环活动
			LocalDateTime firstOpenOTime = openOTime.plusDays(activeTemplateCfg.getOpenDay() - 1);
			int openDuration = activeTemplateCfg.getOpenPeriod().get(0);
			int openGap = activeTemplateCfg.getOpenPeriod().get(1);
			LocalDateTime nowOTime = timeSetting.theDayOTime(LocalDateTimeUtil.now());
			LocalDateTime targetTime = null;
			for (int i = 0; i < 10000; i++) {
				//比如在1月1日10点开服，那么开服“零”点openOTime=1月1日“零”点，此时nowOTime=1月1日“零”点
				//活动的首次开放时间firstOpenOTime=开服“零”点+（开服天数-1）
				//则下面逻辑是找到未来一段需要提供给新一轮活动进行的可用时间，加上1分钟是为了满足活动配置希望在以开服天数来准的第一段时间内开启活动
				targetTime = firstOpenOTime.plusDays(i * (openDuration + openGap)).plusMinutes(1);
				if (targetTime.isAfter(nowOTime)) {
					break;
				}
			}
			control.setConfigId(0);
			control.setTurns(1);
			control.setStartTime(targetTime);
			control.setStillTime(control.getStartTime().plusDays(openDuration));
			control.setEndTime(control.getStillTime());
			control.setNextTime(control.getEndTime().plusDays(openGap));
		} else {
			//不知道什么类型的活动，直接终止，策划乱填
			control.setTerminate(true);
		}
		return control;
	}
	/**
	 * 活动开始时被调用
	 * @param control
	 */
	public abstract void whenStart(PromotionControl control);
	/**
	 * 活动进入沉寂时被调用
	 * @param control
	 */
	public abstract void whenStill(PromotionControl control);
	/**
	 * 活动结算时被调用
	 * @param control
	 * @return
	 */
	public abstract void whenEnd(PromotionControl control);
	/**
	 * 活动进入新一轮时被调用（不是活动开始）
	 * @param control
	 */
	public void whenNext(PromotionControl control) {
		ActiveTemplateCfg activeTemplateCfg = activeTemplateCache.getOrThrow(control.getTypeId());
		
		if(activeTemplateCfg.getOpType() == 1) {
			//开服活动
			control.setTerminate(true);
			return;
		} else if(activeTemplateCfg.getOpType() == 2) {
			//循环活动
			int openDuration = activeTemplateCfg.getOpenPeriod().get(0);
			int openGap = activeTemplateCfg.getOpenPeriod().get(1);
			LocalDateTime targetTime = LocalDateTimeUtil.now();
			
			control.setTurns(control.getTurns() + 1);
			control.setStartTime(targetTime);
			control.setStillTime(control.getStartTime().plusDays(openDuration));
			control.setEndTime(control.getStillTime());
			control.setNextTime(control.getEndTime().plusDays(openGap));
		} else {
			//不知道什么类型的活动，直接终止，策划乱填
			control.setTerminate(true);
			return;
		}
	}
	/**
	 * 活动
	 * @param playerId
	 * @param control
	 */
	public abstract void handlePromotionEnd(long playerId, PromotionControl control);
	/**
	 * 是否显示红点
	 * @param playerId
	 * @return
	 */
	public abstract boolean showLoginRedPoint(long playerId, int typeId);
}
