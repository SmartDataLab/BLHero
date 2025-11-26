/**
 * 
 */
package com.xiugou.x1.backstage.module.gamelog;

import org.gaming.backstage.PageData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xiugou.x1.backstage.module.gamelog.service.LogTableService;
import com.xiugou.x1.backstage.module.gamelog.struct.LogQuery;

import pojo.xiugou.x1.pojo.log.bag.ItemLog;
import pojo.xiugou.x1.pojo.log.equip.EquipLog;
import pojo.xiugou.x1.pojo.log.hero.HeroFightingLog;
import pojo.xiugou.x1.pojo.log.hero.HeroLog;
import pojo.xiugou.x1.pojo.log.home.MeatLog;
import pojo.xiugou.x1.pojo.log.home.MineLog;
import pojo.xiugou.x1.pojo.log.home.WoodLog;
import pojo.xiugou.x1.pojo.log.mail.MailLog;
import pojo.xiugou.x1.pojo.log.player.DiamondLog;
import pojo.xiugou.x1.pojo.log.player.GoldLog;
import pojo.xiugou.x1.pojo.log.player.PlayerExpLog;
import pojo.xiugou.x1.pojo.log.player.PlayerFightingLog;

/**
 * @author YY
 *
 */
@Controller
public class GameLogController {

	@Autowired
	private LogTableService logTableService;
	
	@RequestMapping(value = "/gamelog/itemLogs.auth")
	@ResponseBody
	public PageData<ItemLog> itemLogs(LogQuery query) {
		return logTableService.queryDatas(ItemLog.class, query);
	}
	
	@RequestMapping(value = "/gamelog/equipLogs.auth")
	@ResponseBody
	public PageData<EquipLog> equipLogs(LogQuery query) {
		return logTableService.queryDatas(EquipLog.class, query);
	}
	
	
	@RequestMapping(value = "/gamelog/goldLogs.auth")
	@ResponseBody
	public PageData<GoldLog> goldLogs(LogQuery query) {
		return logTableService.queryDatas(GoldLog.class, query);
	}
	
	@RequestMapping(value = "/gamelog/diamondLogs.auth")
	@ResponseBody
	public PageData<DiamondLog> diamondLogs(LogQuery query) {
		return logTableService.queryDatas(DiamondLog.class, query);
	}
	
	
	@RequestMapping(value = "/gamelog/playerExpLogs.auth")
	@ResponseBody
	public PageData<PlayerExpLog> playerExpLogs(LogQuery query) {
		return logTableService.queryDatas(PlayerExpLog.class, query);
	}
	
	
	@RequestMapping(value = "/gamelog/heroLogs.auth")
	@ResponseBody
	public PageData<HeroLog> heroLogs(LogQuery query) {
		return logTableService.queryDatas(HeroLog.class, query);
	}
	
	
	@RequestMapping(value = "/gamelog/heroFightingLogs.auth")
	@ResponseBody
	public PageData<HeroFightingLog> heroFightingLogs(LogQuery query) {
		return logTableService.queryDatas(HeroFightingLog.class, query);
	}
	
	
	@RequestMapping(value = "/gamelog/meatLogs.auth")
	@ResponseBody
	public PageData<MeatLog> meatLogs(LogQuery query) {
		return logTableService.queryDatas(MeatLog.class, query);
	}
	
	
	@RequestMapping(value = "/gamelog/woodLogs.auth")
	@ResponseBody
	public PageData<WoodLog> woodLogs(LogQuery query) {
		return logTableService.queryDatas(WoodLog.class, query);
	}
	
	
	@RequestMapping(value = "/gamelog/mineLogs.auth")
	@ResponseBody
	public PageData<MineLog> mineLogs(LogQuery query) {
		return logTableService.queryDatas(MineLog.class, query);
	}
	
	
	@RequestMapping(value = "/gamelog/mailLogs.auth")
	@ResponseBody
	public PageData<MailLog> mailLogs(LogQuery query) {
		return logTableService.queryDatas(MailLog.class, query);
	}
	
	@RequestMapping(value = "/gamelog/playerFightingLogs.auth")
	@ResponseBody
	public PageData<PlayerFightingLog> playerFightingLogs(LogQuery query) {
		return logTableService.queryDatas(PlayerFightingLog.class, query);
	}
}
